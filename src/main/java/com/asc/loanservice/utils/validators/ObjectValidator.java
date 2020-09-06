package com.asc.loanservice.utils.validators;

import com.asc.loanservice.contracts.LoanRequestDto;

import java.lang.reflect.Field;

public class ObjectValidator {

    public Boolean validateObjectFields(Object object) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(object) == null)
                return false;
        }
        return true;
    }
}
