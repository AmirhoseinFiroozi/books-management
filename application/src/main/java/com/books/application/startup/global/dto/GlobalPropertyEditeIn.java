package com.books.application.startup.global.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

/**
 * @auther : Armin.Nik
 * @project : shared
 * @date : 04.10.22
 */
@Getter
@Setter
public class GlobalPropertyEditeIn {
    private JsonNode property;
}
