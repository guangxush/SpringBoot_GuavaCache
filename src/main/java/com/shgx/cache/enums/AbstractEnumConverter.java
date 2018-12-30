package com.shgx.cache.enums;

import javax.persistence.AttributeConverter;

public abstract class AbstractEnumConverter<E extends Enum<E> & Convertible<T>, T> implements AttributeConverter<E, T> {

    private final Class<E> clazz;

    /**
     * constructor
     *
     * @param clazz
     */
    public AbstractEnumConverter(Class<E> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T convertToDatabaseColumn(E attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public E convertToEntityAttribute(T dbData) {
        E[] enums = clazz.getEnumConstants();

        for(E e : enums) {
            if(e.getValue().equals(dbData)) {
                return e;
            }
        }

        throw new IllegalArgumentException();
    }
}
