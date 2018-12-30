package com.shgx.cache.enums;

import com.fasterxml.jackson.annotation.JsonValue;
/**
 * 审核枚举类型
 *
 * @author guangxush
 */
public enum ReviewTypeEnum implements Convertible<Integer>{
    /**
     * 书籍审核通过
     */
    PASS(0, "pass"), /**
     * 书籍审核未过
     */
    REJECTED(1, "rejected"), /**
     * 书籍待审核
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
