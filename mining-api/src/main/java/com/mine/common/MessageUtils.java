package com.mine.common;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;

@Component
public class MessageUtils {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String code, String... params) {
        if (StringUtils.isBlank(code)) {
            return StringUtils.EMPTY;
        }
        Locale locale = new Locale("zh_CN");
        String msg = messageSource.getMessage(code, null, locale);
        return msg == null ? msg
                : params == null ? msg : MessageFormat.format(msg, params);
    }

}
