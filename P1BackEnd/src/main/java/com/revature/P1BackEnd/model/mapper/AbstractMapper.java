package com.revature.P1BackEnd.model.mapper;

public abstract class AbstractMapper<U, V> {

    public abstract U dtoToEntity(V v);

    public abstract V entityToDto(U u);
}
