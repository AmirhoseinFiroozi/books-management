package com.books.utility.config;

import com.books.utility.commons.repository.interfaces.FilterBase;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class FilterBaseBinder {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        if (binder.getTarget() instanceof FilterBase) {
            StringTrimmerEditor stringTrimmer = new StringTrimmerEditor(true);
            binder.registerCustomEditor(String.class, stringTrimmer);
        }
    }
}
