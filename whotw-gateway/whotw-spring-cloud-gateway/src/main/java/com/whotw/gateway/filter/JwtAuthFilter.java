package com.whotw.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whotw.gateway.utils.RandomValueStringGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.*;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author EdisonXu
 * @date 2019-08-08
 */
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    public static final String BEARER_TYPE = "Bearer";

    public static final String OAUTH2_TYPE = "OAuth2";

    public static final String EXP = "exp";

    public static final String AUTHORITIES = "authorities";

    public static final String ERROR_DESCRIPTION_AUTH_FAIL = "Full authentication is required to access this resource";

    public static final String ERROR_DESCRIPTION_TOKEN_EXPIRED = "Access token expired";

    //TODO: IgnoreUrl应该从配置文件读取而非HardCode
    private String[] ignoreUrls = {
            "/actuator/**",
            "/swagger-ui.html/**",
            "/**/v2/api-docs",
            "/oauth/**",
            "/api/demo/**",
            "/register/**"
    };

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    private ThreadLocal<ObjectMapper> mapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);

    private String verifierKey;

    private Signer signer;

    @Value("${xsh.security.oauth2.jwt-sign-key:@null}")
    private String signingKey;

    private SignatureVerifier verifier;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getURI().getPath();/*
        logger.info("请求地址： {}", requestPath);
        boolean skip = false;
        for (String ignoreUrl : ignoreUrls) {
            skip = antPathMatcher.match(ignoreUrl, requestPath);
            if (skip)
                break;
        }
        if (skip)
            return chain.filter(exchange);

        //检查头部Token
        try {
            String jwtToken = extractJWTToken(request);
            //TODO: 用JJWT替换spring-jwt，后者的异常处理太粗糙了
            Jwt jwt = JwtHelper.decodeAndVerify(jwtToken, verifier);
            String claimsStr = jwt.getClaims();
            Map<String, Object> claims = mapperThreadLocal.get().readValue(claimsStr, Map.class);

            //检查是否到期
            if (claims.containsKey(EXP)) {
                Long expireValue = null;
                Object exp = claims.get(EXP);
                if (exp instanceof Integer)
                    expireValue = new Long((Integer) exp);
                else if (exp instanceof Long)
                    expireValue = (Long) exp;
                Date expireTime = new Date(expireValue * 1000L);
                if (expireTime.before(new Date())) {
                    return onError(exchange, "token_expired", ERROR_DESCRIPTION_TOKEN_EXPIRED);
                }
            }

            if (claims.containsKey(SecurityConstants.ACCOUNT_KEY)) {
                ObjectMapper mapper = mapperThreadLocal.get();
                Account account = mapper.convertValue(claims.get(SecurityConstants.ACCOUNT_KEY), Account.class);
                //黑名单验证
                String tokenValue = jwtToken.substring(jwtToken.length() - 8);
                if (isBlack(account.getMobile(), tokenValue)) {
                    logger.debug("手机号{}因被加入黑名单，准备重新登陆", account.getMobile());
                    return onError(exchange, "token_expired", ERROR_DESCRIPTION_TOKEN_EXPIRED);
                }
                ServerHttpRequest.Builder builder =
                        request
                                .mutate();
                if(account.getInstitutionId()!=null)
                    builder.header(SecurityConstants.HEADER_KEY_INSTITUTION_ID, account.getInstitutionId().toString());
                if(account.getId()!=null)
                    builder.header(SecurityConstants.HEADER_KEY_ACCOUNT_ID, account.getId().toString());
                if(account.getUserId()!=null)
                    builder.header(SecurityConstants.HEADER_KEY_USER_ID, account.getUserId().toString());
                if(account.getUserFullName()!=null)
                    builder.header(SecurityConstants.HEADER_KEY_USER_NAME, URLEncoder.encode(account.getUserFullName(), "UTF-8"));
                if(account.getMobile()!=null)
                    builder.header(SecurityConstants.HEADER_KEY_MOBILE, account.getMobile());
                if(account.getSystemRole()!=null)
                    builder.header(SecurityConstants.HEADER_KEY_SYSTEM_ROLE, account.getSystemRole().toString());

                //获取权限
                if (claims.containsKey(AUTHORITIES)) {
                    Object authorities = claims.get(AUTHORITIES);
                    Collection<String> roles = null;
                    if (authorities instanceof String) {
                        roles = commaSeparatedStringToAuthorityList((String) authorities);
                    }
                    if (authorities instanceof Collection) {
                        roles = (Collection) authorities;
                    }
                    roles = roles
                            .stream()
                            .map(r -> Constants.REDIS_KEY_ROLE_ID + r)
                            .collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(roles)) {
                        List<String> permissions = redisTemplate
                                .opsForValue()
                                .multiGet(roles);
                        if (!CollectionUtils.isEmpty(permissions)) {
                            String joinResult = permissions.stream().collect(Collectors.joining(","));
                            joinResult = "[" + joinResult + "]";
                            builder = builder.header(
                                    SecurityConstants.HEADER_KEY_ACCOUNT_PERMISSION,
                                    joinResult
                            );
                        }
                    }
                }

                ServerHttpRequest muted = builder.build();
                return chain.filter(exchange.mutate().request(muted).build());
            }

        } catch (IllegalArgumentException | InvalidSignatureException e) {
            logger.error("", e);
            return onAuthFailed(exchange, "invalid_token");
        } catch (UnauthorizedException e) {
            return onAuthFailed(exchange, "unauthorized");
        } catch (Exception e) {
            logger.error("", e);
            return onAuthFailed(exchange, "unauthorized");
        }*/
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }

    private List<String> commaSeparatedStringToAuthorityList(
            String authorityString) {
        return createAuthorityList(org.springframework.util.StringUtils
                .tokenizeToStringArray(authorityString, ","));
    }

    private List<String> createAuthorityList(String... roles) {
        List<String> authorities = new ArrayList<>(roles.length);

        for (String role : roles) {
            authorities.add(role);
        }
        return authorities;
    }

/*    private String extractJWTToken(ServerHttpRequest request) {
        List<String> headers = request.getHeaders().get("Authorization");
        if (headers != null && !headers.isEmpty()) {
            for (String header : headers) {
                if ((header.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))) {
                    String authHeaderValue = header.substring(BEARER_TYPE.length()).trim();
                    int commaIndex = authHeaderValue.indexOf(',');
                    if (commaIndex > 0) {
                        authHeaderValue = authHeaderValue.substring(0, commaIndex);
                    }
                    return authHeaderValue;
                }
            }
        }
        throw new UnauthorizedException("JWT Header is null");
    }*/

    private Mono<Void> onError(ServerWebExchange exchange, String err, String errorDescription) {
        try {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            Map<String, String> errors = new HashMap<>();
            errors.put("error", err);
            errors.put("error_description", errorDescription);
            DataBuffer db = new DefaultDataBufferFactory().wrap(mapperThreadLocal.get().writeValueAsBytes(errors));
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
            return response.writeWith(Mono.just(db));
        } catch (JsonProcessingException e) {
            logger.error("", e);
        }
        return Mono.empty();
    }

    private Mono<Void> onAuthFailed(ServerWebExchange exchange, String err) {
        return onError(exchange, err, ERROR_DESCRIPTION_AUTH_FAIL);
    }

    public void setSigningKey(String key) {
        Assert.hasText(key);
        key = key.trim();

        this.signingKey = key;

        if (isPublic(key)) {
            signer = new RsaSigner(key);
            logger.info("Configured with RSA signing key");
        } else {
            // Assume it's a MAC key
            this.verifierKey = key;
            signer = new MacSigner(key);
        }
    }

    /**
     * @return true if the key has a public verifier
     */
    private boolean isPublic(String key) {
        return key.startsWith("-----BEGIN");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (verifier != null) {
            // Assume signer also set independently if needed
            return;
        }
        if (StringUtils.isNotBlank(signingKey))
            setSigningKey(signingKey);
        else
            this.verifierKey = new RandomValueStringGenerator().generate();
        signer = new MacSigner(verifierKey);
        SignatureVerifier verifier = new MacSigner(verifierKey);
        try {
            verifier = new RsaVerifier(verifierKey);
        } catch (Exception e) {
            logger.warn("Unable to create an RSA verifier from verifierKey (ignoreable if using MAC)");
        }
        // Check the signing and verification keys match
        if (signer instanceof RsaSigner) {
            byte[] test = "test".getBytes();
            try {
                verifier.verify(test, signer.sign(test));
                logger.info("Signing and verification RSA keys match");
            } catch (InvalidSignatureException e) {
                logger.error("Signing and verification RSA keys do not match");
            }
        } else if (verifier instanceof MacSigner) {
            // Avoid a race condition where setters are called in the wrong order. Use of
            // == is intentional.
            Assert.state(this.signingKey == this.verifierKey,
                    "For MAC signing you do not need to specify the verifier key separately, and if you do it must match the signing key");
        }
        this.verifier = verifier;
    }
}
