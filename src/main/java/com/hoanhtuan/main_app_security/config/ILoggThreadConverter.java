package com.hoanhtuan.main_app_security.config;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class ILoggThreadConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        String threadName = event.getThreadName();
        int targetLength = 22;

        if (threadName.length() < targetLength) {
            // Thêm dấu '-' để đủ 22 ký tự
            StringBuilder sb = new StringBuilder(threadName);
            while (sb.length() < targetLength) {
                sb.append('_');
            }
            return sb.toString();
        }

        return threadName;
    }

}
