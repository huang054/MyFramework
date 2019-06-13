package com.mybatis.exectuor;

import com.mybatis.config.MapperRegistory;

public interface Executor {

    <T> T query(MapperRegistory.MapperData mapperData, Object parameter) throws Exception;
}
