package com.whotw.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.whotw.rest.ResponseWrapper;
import feign.Feign;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;

/**
 * 让Feign正确返回业务异常
 *
 * @author EdisonXu
 * @date 2019-08-07
 */
@Configuration
@ConditionalOnClass({Feign.class})
public class FeignErrorConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(FeignErrorConfiguration.class);
    private ThreadLocal<ObjectMapper>  mapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);

    @Bean
    public ErrorDecoder errorDecoder(){
        return new CustomFeignErrorDecoder();
    }


    public class CustomFeignErrorDecoder extends ErrorDecoder.Default {

        @Override
        public Exception decode(String methodKey, Response response) {
            try {
                if (response.body() != null) {
                    String json = Util.toString(response.body().asReader());
                    ObjectMapper objectMapper = mapperThreadLocal.get();
                    ResponseWrapper responseWrapper = objectMapper.readValue(json.getBytes("UTF-8"), ResponseWrapper.class);
                    String exceptionName = responseWrapper.getException();
                    if(StringUtils.isNotBlank(exceptionName)){
                        Class clazz = Class.forName(responseWrapper.getException());
                        Exception exception = null;
                        try {
                            exception = (Exception) clazz.getDeclaredConstructor(String.class).newInstance(responseWrapper.getMsg());
                        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException  e) {
                            exception = new RuntimeException(responseWrapper.getMsg());
                        }
                        if(400<= responseWrapper.getCode() || responseWrapper.getCode() < 500)
                            // 用HystrixBadRequestException包装一层，不触发熔断
                            exception = new HystrixBadRequestException(responseWrapper.getMsg(), exception);
                        return exception;
                    }
                }
            } catch (Exception exception) {
                //do nothing
                logger.error("", exception);
            }
            return super.decode(methodKey, response);
        }
    }
}
