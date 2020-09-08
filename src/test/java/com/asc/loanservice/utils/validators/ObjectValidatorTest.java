package com.asc.loanservice.utils.validators;

import com.asc.loanservice.contracts.LoanRequestDto;

import com.asc.loanservice.contracts.LoanRequestEvaluationResult;
import com.asc.loanservice.contracts.LoanRequestRegistrationResultDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectValidatorTest {

    private ObjectValidator objectValidator = new ObjectValidator();

    @Test
    void validateObjectFieldsShouldReturnFalseOnNullValuesOfObject() throws Exception{
        final LoanRequestDto loanRequestDto = new LoanRequestDto();

        assertFalse(objectValidator.validateObjectFields(loanRequestDto));
    }

    @Test
    void validateObjectFieldsShouldReturnTrueNonNullValuesOfObject() throws Exception{
        final LoanRequestRegistrationResultDto loanRequestRegistrationResultDto =
                new LoanRequestRegistrationResultDto("123456789", LoanRequestEvaluationResult.APPROVED);

        assertTrue(objectValidator.validateObjectFields(loanRequestRegistrationResultDto));
    }
}
