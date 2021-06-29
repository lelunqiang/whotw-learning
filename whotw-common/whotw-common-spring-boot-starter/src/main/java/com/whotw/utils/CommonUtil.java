package com.whotw.utils;


import cn.hutool.core.date.DatePattern;
import com.whotw.common.data.Constants;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.*;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author EdisonXu
 * @date 2019-07-16
 */

public final class CommonUtil {

    static final String[] CHINESE_NUM = {"零","一","二","三","四","五","六","七","八","九"};
    static final String[] CHINESE_NUM_UNITS = {"","十","百","千","万","十","百","千","亿","十","百","千"};
    static final String FILE_NAME_REG = ".+/(.+)$";
    static final char[] DIGITS =
            { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
                    'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
                    'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
                    'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
    static final Pattern MOBILE_MASK_PATTERN = Pattern.compile("(?<=[\\d]{3})\\d(?=[\\d]{4})");
    static final String MASK_CHARACTER = "*";

    public static <D> D toVO(Object source, Class<D> destinationType){
        return map(source, destinationType);
    }

    public static <D> D toEntity(Object source, Class<D> destinationType){
        return map(source, destinationType);
    }

    public static <D> D map(Object source, Class<D> destinationType){
        if(source==null)
            return null;
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFullTypeMatchingRequired(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Provider<LocalDate> localDateProvider = new AbstractProvider<LocalDate>() {
            @Override
            public LocalDate get() {
                return LocalDate.now(ZoneId.systemDefault());
            }
        };
        Converter<String, LocalDate> localDateConverter = new AbstractConverter<String, LocalDate>() {
            @Override
            protected LocalDate convert(String source) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN);
                LocalDate localDate = LocalDate.parse(source, format);
                return localDate;
            }
        };
        modelMapper.createTypeMap(String.class, LocalDate.class);
        modelMapper.addConverter(localDateConverter);
        modelMapper.getTypeMap(String.class, LocalDate.class).setProvider(localDateProvider);

        Provider<LocalTime> localTimeProvider = new AbstractProvider<LocalTime>() {
            @Override
            public LocalTime get() {
                return LocalTime.now(ZoneId.systemDefault());
            }
        };
        Converter<String, LocalTime> localTimeConverter = new AbstractConverter<String, LocalTime>() {
            @Override
            protected LocalTime convert(String source) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalTime localTime = LocalTime.parse(source, format);
                return localTime;
            }
        };
        modelMapper.createTypeMap(String.class, LocalTime.class);
        modelMapper.addConverter(localTimeConverter);
        modelMapper.getTypeMap(String.class, LocalTime.class).setProvider(localTimeProvider);

        Provider<LocalDateTime> localDateTimeProvider = new AbstractProvider<LocalDateTime>() {
            @Override
            public LocalDateTime get() {
                return LocalDateTime.now(ZoneId.systemDefault());
            }
        };
        Converter<String, LocalDateTime> localDateTimeConverter = new AbstractConverter<String, LocalDateTime>() {
            @Override
            protected LocalDateTime convert(String source) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN);
                LocalDateTime localDateTime = LocalDateTime.parse(source, format);
                return localDateTime;
            }
        };
        modelMapper.createTypeMap(String.class, LocalDateTime.class);
        modelMapper.addConverter(localDateTimeConverter);
        modelMapper.getTypeMap(String.class, LocalDateTime.class).setProvider(localDateTimeProvider);
        return modelMapper.map(source, destinationType);
    }

    public static <D> List<D> toVOList(List<?> sourceList, Class<D> destinationType){
        return mapToList(sourceList, destinationType);
    }

    public static <D> List<D> mapToList(List<?> sourceList, Class<D> destinationType){
        if(sourceList==null)
            return null;
        if(sourceList.isEmpty())
            return Collections.emptyList();
        ModelMapper modelMapper = new ModelMapper();
        List<D> resultList = new ArrayList<>();
        sourceList.stream().forEach(s->resultList.add(modelMapper.map(s, destinationType)));
        return resultList;
    }

    public static boolean isValidPhoneNo(String mobile){
        if(StringUtils.isBlank(mobile))
            return false;
        return Pattern.matches(Constants.PHONE_REGEX, mobile);
    }

    public static void checkStringNotBlank(String paramName, String paramValue){
        Assert.notNull(paramName, "Paramname cannot be null!");
        if(StringUtils.isBlank(paramValue))
            throw new IllegalArgumentException(paramName + " cannot be empty!");
    }

    public static Map<String, Object> buildIdMap(String idName, Long idValue){
        Assert.notNull(idName, "idName cannot be null!");
        Assert.notNull(idValue, "idValue cannot be null!");
        Map<String, Object> idMap = new HashMap<>();
        idMap.put(idName, idValue);
        return idMap;
    }

    public static String intToChineseNumber(int input) {
        StringBuilder resultBuilder = new StringBuilder();
        if(input<10)
            return CHINESE_NUM[input];
        int count = 0;
        while(input>0){
            resultBuilder.insert(0, CHINESE_NUM_UNITS[count]).insert(0, CHINESE_NUM[input % 10]);
            input = input / 10;
            count++;
        }
        return resultBuilder.toString()
                .replaceAll("零[千百十]", "零")
                .replaceAll("零+万", "万")
                .replaceAll("零+亿", "亿")
                .replaceAll("零+", "零")
                .replaceAll("零$", "");
    }

    public static String getFileNameWithExt(String file){
        Pattern p = Pattern.compile(FILE_NAME_REG);
        Matcher m = p.matcher(file);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    public static String[] parseFileNameAndExt(String path){
        String fileNameAndExt = getFileNameWithExt(path);
        if(StringUtils.isNotBlank(fileNameAndExt))
            return fileNameAndExt.split("\\.");
        return null;
    }

    public static String getFileType(String filePath) {
        return filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
    }

    public static String to62RadixString(long seq) {
        StringBuilder sBuilder = new StringBuilder();
        while (true) {
            int remainder = (int) (seq % 62);
            sBuilder.append(DIGITS[remainder]);
            seq = seq / 62;
            if (seq == 0) {
                break;
            }
        }
        return sBuilder.toString();
    }

    public static String mastMobile(String mobile){
        if(StringUtils.isBlank(mobile))
            return mobile;
        return MOBILE_MASK_PATTERN.matcher(mobile).replaceAll(MASK_CHARACTER);
    }

    public static void main(String[] args) {
        System.out.println(mastMobile("17717568668"));
    }
}
