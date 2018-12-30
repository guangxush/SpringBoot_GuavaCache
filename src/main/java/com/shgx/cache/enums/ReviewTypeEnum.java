package com.shgx.cache.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReviewTypeEnum implements Convertible<Integer>{
    /**
     * 过审
     */
    PASS(0, "pass"), /**
     * 未过审
     */
    REJECTED(1, "rejected"), /**
     * 待审核
     */
    UNKNOWN(2, "unknown");
    private int value;
    private String name;

    ReviewTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    @JsonValue
    public Integer getValue() {
        return this.value;
    }

    public static class Converter extends AbstractEnumConverter<ReviewTypeEnum, Integer> {
        public Converter() {
            super(ReviewTypeEnum.class);
        }
    }
}
