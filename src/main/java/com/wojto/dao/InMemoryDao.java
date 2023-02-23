package com.wojto.dao;

import org.springframework.batch.item.file.mapping.FieldSetMapper;

import java.util.List;

public interface InMemoryDao {

    public String getFileName();
    public String[] getParameterNames();
    public <T extends Object> Class<T> getClassType();
    public FieldSetMapper getMapperForObjects();

}
