package com.wojto.util;

import org.mockito.Mockito;
import org.springframework.beans.factory.FactoryBean;

public class MockitoFactoryBean<T> implements FactoryBean<T> {

    private Class<T> classToBeMocked;

    public MockitoFactoryBean(Class<T> classToBeMocked) {
        this.classToBeMocked = classToBeMocked;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public T getObject() throws Exception {
        return Mockito.mock(classToBeMocked);
    }

    @Override
    public Class<?> getObjectType() {
        return classToBeMocked;
    }
}
