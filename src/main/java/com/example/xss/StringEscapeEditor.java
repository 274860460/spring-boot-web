package com.example.xss;


import org.springframework.web.util.HtmlUtils;

import java.beans.PropertyEditorSupport;

public class StringEscapeEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) {

        if (text == null) {
            setValue(null);
            return;
        }

        text = HtmlUtils.htmlEscape(text);

        setValue(text);

    }

    @Override
    public String getAsText() {
        Object value = getValue();
        return value != null ? value.toString() : "";
    }

}


