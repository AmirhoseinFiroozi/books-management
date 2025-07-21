package com.books.utility.commons.repository.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : ahmad on 14.01.20 - 10:25
 **/
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
public class ConditionParameters {
    private String key;
    private Object value;

    public ConditionParameters(String key, Object value) {
        this.key = key;
        this.value = value;
    }
}
