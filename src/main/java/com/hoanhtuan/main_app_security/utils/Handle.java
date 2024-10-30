package com.hoanhtuan.main_app_security.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor (access = AccessLevel.PRIVATE)
public class Handle {
    private static final String DASH = "-";
    private static final String DEFAULT_NUMBER_ID_12 = "000000000000";
    private static final String REQUEST_KEY = "requestId";
    private static final AtomicReference<SimpleDateFormat> DATE_FORMAT = new AtomicReference<>(new SimpleDateFormat("ddMMyyyy"));
    private static final AtomicReference<SimpleDateFormat> TIME_FORMAT = new AtomicReference<>(new SimpleDateFormat("HHmm"));
    private static final AtomicReference<SimpleDateFormat> DAY_MONTH_FORMAT = new AtomicReference<>(new SimpleDateFormat("ddMM"));
    private static final AtomicReference<SimpleDateFormat> YEAR_FORMAT = new AtomicReference<>(new SimpleDateFormat("yyyy"));

    public static void addRequestId(String requestId){
        String logId = StringUtils.defaultIfBlank(requestId, UUID.randomUUID().toString());
        MDC.put(REQUEST_KEY, logId);
    }

    public static String generateRequestIdStart() {
        Date now = new Date();

        String datePart = DATE_FORMAT.get().format(now);
        String timePart = TIME_FORMAT.get().format(now);
        String dayMonthPart = DAY_MONTH_FORMAT.get().format(now);
        String yearPart = YEAR_FORMAT.get().format(now);

        return StringUtils.join(datePart , DASH, timePart, DASH, dayMonthPart, DASH, yearPart, DASH, DEFAULT_NUMBER_ID_12);
    }

}
