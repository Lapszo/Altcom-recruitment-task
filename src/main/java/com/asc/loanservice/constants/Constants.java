package com.asc.loanservice.constants;

import java.math.BigDecimal;

public final class Constants {
    private Constants() {}
    public static final class ApiUrls {
        private ApiUrls() {}
        public static final String CUSTOMER_CHECK_GET_API_URL = "http://localhost:8090/api/customercheck/";
    }
    public static final class BusinessRules {
        private BusinessRules() {}
        public static final BigDecimal INTEREST_RATE = BigDecimal.valueOf(1.04);
        public static final  BigDecimal MINIMUM_MONTHLY_CUSTOMER_INCOME_PERCENTAGE = BigDecimal.valueOf(0.15);
        public static final  Integer MAXIMUM_AGE_OF_CUSTOMER_ON_LAST_RENT = 65;
    }
}
