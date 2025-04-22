package com.easylive.entity.constants;

public class constants {

    public static final Integer ONE = 1;

    public static final Integer ZERO = 0;

    public static final Integer LENTH_10 = 10;

    public static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-zA-Z])[\\da-zA-Z~!@#$%^&*_]{8,18}$";

    public static final Integer REDIS_KEY_EXPIRES_ONE_MIN = 60000;

    public static final Integer REDIS_KEY_EXPIRES_ONE_DAY = 86400000;

    public static final Integer TIME_SECONDS_ONE_DAY =  REDIS_KEY_EXPIRES_ONE_DAY / 1000;

    public static final String REDIS_KEY_PREFIX = "easylive:";

    public static String REDIS_KEY_CHECK_CODE = REDIS_KEY_PREFIX + "checkcode:";

    public static String REDIS_KEY_TOKEN_WEB = REDIS_KEY_PREFIX + "token:web:";

    public static String TOKEN_WEB = "token";
}
