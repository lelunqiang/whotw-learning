package com.whotw.mysql.custom;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;

/**
 * @author EdisonXu
 * @date 2019-09-05
 */
public class CustomMybatisXMLLanguageDriver extends XMLLanguageDriver {

    @Override
    public CustomMybatisDefaultParameterHandler createParameterHandler(MappedStatement mappedStatement,
                                                                 Object parameterObject, BoundSql boundSql) {
        // TODO 使用 MybatisDefaultParameterHandler 而不是 ParameterHandler
        return new CustomMybatisDefaultParameterHandler(mappedStatement, parameterObject, boundSql);
    }
    
}
