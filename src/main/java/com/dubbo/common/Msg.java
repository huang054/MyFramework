package com.dubbo.common;

import java.io.Serializable;

public class Msg implements Serializable {

    private String className;

    private String methodName;

    private Class<?>[] parames;

    private Object[] paramesValue;

    public Msg(String className, String methodName, Class<?>[] parames, Object[] paramesValue) {
        this.className = className;
        this.methodName = methodName;
        this.parames = parames;
        this.paramesValue = paramesValue;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParames() {
        return parames;
    }

    public void setParames(Class<?>[] parames) {
        this.parames = parames;
    }

    public Object[] getParamesValue() {
        return paramesValue;
    }

    public void setParamesValue(Object[] paramesValue) {
        this.paramesValue = paramesValue;
    }
}
