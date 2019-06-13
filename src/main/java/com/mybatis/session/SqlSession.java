package com.mybatis.session;

import com.mybatis.config.Configuration;
import com.mybatis.config.MapperRegistory;
import com.mybatis.exectuor.Executor;
import com.mybatis.mapper.MapperProxy;

import java.lang.reflect.Proxy;

public class SqlSession {

    private Configuration configuration;
    private Executor executor;

    public Configuration getConfiguration() {
        return configuration;
    }

    //关联起来
    public SqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    public <T> T getMapper(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},new MapperProxy(this,clazz));
    }

    public <T> T selectOne(MapperRegistory.MapperData mapperData, Object parameter) throws Exception {
        return executor.query(mapperData, parameter);
    }
}
