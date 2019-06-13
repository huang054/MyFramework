package com.mybatis.config;

import com.mybatis.Test;

import java.util.HashMap;
import java.util.Map;

public class MapperRegistory {
    public static final Map<String, MapperData> methodSqlMapping = new HashMap<>();

    public MapperRegistory() {
        methodSqlMapping.put("com.mybatis.TestMapper.selectByPrimaryKey",
                new MapperData("select * from test where id = %d", Test.class));
    }

    public class MapperData<T>{
        private String sql;
        private Class<T> type;

        public MapperData(String sql, Class<T> type) {
            this.sql = sql;
            this.type = type;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public Class<T> getType() {
            return type;
        }

        public void setType(Class<T> type) {
            this.type = type;
        }
    }

    public MapperData get(String nameSpace) {
        return methodSqlMapping.get(nameSpace);
    }
}
