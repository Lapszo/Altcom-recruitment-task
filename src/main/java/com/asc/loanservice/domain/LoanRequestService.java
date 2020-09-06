package com.asc.loanservice.domain;

import com.asc.loanservice.constants.Constants;
import com.asc.loanservice.contracts.CustomerCheckResultDto;
import com.asc.loanservice.contracts.LoanRequestDto;
import com.asc.loanservice.contracts.LoanRequestEvaluationResult;
import com.asc.loanservice.models.LoanRequestModel;
import com.asc.loanservice.utils.validators.ObjectValidator;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class LoanRequestService {

    private static final String CUSTOMER_CHECK_GET_API_URL = Constants.ApiUrls.CUSTOMER_CHECK_GET_API_URL;
    private static final BigDecimal INTEREST_RATE = Constants.BusinessRules.INTEREST_RATE;
    private static final BigDecimal MINIMUM_MONTHLY_CUSTOMER_INCOME_PERCENTAGE = Constants.BusinessRules.MINIMUM_MONTHLY_CUSTOMER_INCOME_PERCENTAGE;
    private static final Integer MAXIMUM_AGE_OF_CUSTOMER_ON_LAST_RENT = Constants.BusinessRules.MAXIMUM_AGE_OF_CUSTOMER_ON_LAST_RENT;

    private RestTemplate restTemplate = new RestTemplate();

    private ObjectValidator objectValidator = new ObjectValidator();

    public LoanRequestModel buildLoanRequestModel(LoanRequestDto loanRequest) {
        checkLoanRequest(loanRequest);
        return LoanRequestModel.builder()
                .customerBirthday(checkCustomerBirthdayDate(loanRequest.getCustomerBirthday()))
                .customerName(loanRequest.getCustomerName())
                .customerTaxId(loanRequest.getCustomerTaxId())
                .customerMonthlyIncome(checkCustomerMonthlyIncome(loanRequest.getCustomerMonthlyIncome()))
                .loanAmount(checkLoanAmount(loanRequest.getLoanAmount()))
                .numberOfInstallments(checkNumberOfInstallments(loanRequest.getNumberOfInstallments()))
                .firstInstallmentDate(checkFirstInstallmentDate(loanRequest.getFirstInstallmentDate()))
                .evaluationResult(determinateEvaluationStatus(loanRequest))
                .registrationDate(getRegistrationDate())
                .build();
    }

    private LocalDate checkCustomerBirthdayDate(LocalDate date) {
        return date.isBefore(LocalDate.now()) ? date : null;
    }

    private LocalDate checkFirstInstallmentDate(LocalDate date) {
        return date.isAfter(LocalDate.now()) ? date : null;
    }

    private LocalDateTime getRegistrationDate() {
        return LocalDateTime.now();
    }

    private BigDecimal checkCustomerMonthlyIncome(BigDecimal customerMonthlyIncome) {
        return customerMonthlyIncome.compareTo(BigDecimal.ZERO) > 0 ? customerMonthlyIncome : null;
    }

    private BigDecimal checkLoanAmount(BigDecimal loanAmount) {
        return loanAmount.compareTo(BigDecimal.ZERO) > 0 ? loanAmount : null;
    }

    private Integer checkNumberOfInstallments(Integer numberOfInstallments) {
        return numberOfInstallments.compareTo(0) > 0 ? numberOfInstallments : null;
    }

    private LoanRequestEvaluationResult determinateEvaluationStatus(LoanRequestDto loanRequest) {
        return customerAgeOnLoanEnded(loanRequest.getCustomerBirthday(), loanRequest.getNumberOfInstallments(),
                loanRequest.getFirstInstallmentDate()) &&
                customerCreditworthiness(loanRequest.getCustomerMonthlyIncome(), loanRequest.getLoanAmount(),
                        loanRequest.getNumberOfInstallments()) &&
                customerRegisterAsDebtor(loanRequest.getCustomerTaxId()) ?
                LoanRequestEvaluationResult.APPROVED : LoanRequestEvaluationResult.REJECTED;
    }

    private Boolean customerAgeOnLoanEnded(LocalDate customerBirthday, Integer numberOfInstallments,
                                           LocalDate firstInstallmentDate) {
        return firstInstallmentDate.plusMonths(numberOfInstallments).getYear() - customerBirthday.getYear()
                <= MAXIMUM_AGE_OF_CUSTOMER_ON_LAST_RENT;
    }

    private boolean customerCreditworthiness(BigDecimal customerMonthlyIncome,
                                             BigDecimal loanAmount, Integer numberOfInstallments) {
        return (loanAmount.divide(new BigDecimal(numberOfInstallments), 2, RoundingMode.CEILING))
                .multiply(INTEREST_RATE)
                .compareTo(customerMonthlyIncome.multiply(MINIMUM_MONTHLY_CUSTOMER_INCOME_PERCENTAGE)) < 0;
    }

    private boolean customerRegisterAsDebtor(String customerTaxId) {
        return !Objects.requireNonNull(restTemplate.getForObject(CUSTOMER_CHECK_GET_API_URL + customerTaxId,
                CustomerCheckResultDto.class)).getIsRegisteredDebtor();
    }

    private void checkLoanRequest(LoanRequestDto loanRequest){
        try {
            if(!objectValidator.validateObjectFields(loanRequest))
                throw new IllegalArgumentException("Illegal argument in LoanRequest. ");
        } catch (IllegalAccessException e) {
        }
    }
}
