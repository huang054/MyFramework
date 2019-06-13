package com.mybatis.config;

public class Configuration {

    private String scanPath;

    private MapperRegistory mapperRegistory = new MapperRegistory();

    public Configuration scanPath(String scanPath){
        this.scanPath=scanPath;
        return this;
    }

    public void build(){
        if (null == scanPath || scanPath.length() < 1) {
            throw new RuntimeException("scan path is required .");
        }

    }

    public String getScanPath() {
        return scanPath;
    }

    public void setScanPath(String scanPath) {
        this.scanPath = scanPath;
    }

    public MapperRegistory getMapperRegistory() {
        return mapperRegistory;
    }

    public void setMapperRegistory(MapperRegistory mapperRegistory) {
        this.mapperRegistory = mapperRegistory;
    }
}
