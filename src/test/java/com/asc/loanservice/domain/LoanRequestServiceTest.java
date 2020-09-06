package com.asc.loanservice.domain;

import com.asc.loanservice.contracts.CustomerCheckResultDto;
import com.asc.loanservice.contracts.LoanRequestDto;
import com.asc.loanservice.contracts.LoanRequestEvaluationResult;
import com.asc.loanservice.models.LoanRequestModel;
import com.asc.loanservice.utils.validators.ObjectValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoanRequestServiceTest {

    @InjectMocks
    private LoanRequestService loanRequestService;

    @Mock
    RestTemplate restTemplate;

    @Mock
    ObjectValidator objectValidator;


    @Test
    void buildLoanRequestModelShouldThrowIllegalArgumentExceptionOnNullCustomerName() throws Exception {

        //given
        LoanRequestDto loanRequestDto = LoanRequestDto.builder()
                .customerName(null)
                .customerTaxId("40080207695")
                .loanAmount(BigDecimal.valueOf(8000))
                .numberOfInstallments(17)
                .customerMonthlyIncome(BigDecimal.valueOf(20000))
                .customerBirthday(LocalDate.of(1980, 9, 17))
                .firstInstallmentDate(LocalDate.of(2020, 9, 26))
                .build();
        Mockito.doThrow(new IllegalArgumentException()).when(objectValidator).validateObjectFields(loanRequestDto);
        //then
        assertThrows(IllegalArgumentException.class, () -> loanRequestService.buildLoanRequestModel(loanRequestDto));

    }

    @Test
    void buildLoanRequestModelShouldThrowIllegalArgumentExceptionOnEmptyCustomerName() throws Exception {

        //given
        LoanRequestDto loanRequestDto = LoanRequestDto.builder()
                .customerName("")
                .customerTaxId("40080207695")
                .loanAmount(BigDecimal.valueOf(8000))
                .numberOfInstallments(17)
                .customerMonthlyIncome(BigDecimal.valueOf(20000))
                .customerBirthday(LocalDate.of(1980, 9, 17))
                .firstInstallmentDate(LocalDate.of(2020, 9, 26))
                .build();
        Mockito.doThrow(new IllegalArgumentException()).when(objectValidator).validateObjectFields(loanRequestDto);

        //then
        assertThrows(IllegalArgumentException.class, () -> loanRequestService.buildLoanRequestModel(loanRequestDto));

    }

    @Test
    void buildLoanRequestModelShouldSetEvaluationResultOnRejectedWhenRegisterDebtorIsTrue() throws Exception {

        final String API_URL = "http://localhost:8090/api/customercheck/";
        //given
        LoanRequestDto loanRequestDto = LoanRequestDto.builder()
                .customerName("Tom")
                .customerTaxId("40080200695")
                .loanAmount(BigDecimal.valueOf(8000))
                .numberOfInstallments(17)
                .customerMonthlyIncome(BigDecimal.valueOf(20000))
                .customerBirthday(LocalDate.of(1980, 9, 17))
                .firstInstallmentDate(LocalDate.of(2020, 9, 26))
                .build();
        CustomerCheckResultDto customerCheckResultDto = new CustomerCheckResultDto("40080200695", true);
        Mockito.when(restTemplate.getForObject(API_URL + customerCheckResultDto.getCustomerTaxId(),
                CustomerCheckResultDto.class)).thenReturn(customerCheckResultDto);
        Mockito.when(objectValidator.validateObjectFields(loanRequestDto)).thenReturn(true);
        //when
        LoanRequestModel loanRequestModel = loanRequestService.buildLoanRequestModel(loanRequestDto);

        //then
        assertEquals(LoanRequestEvaluationResult.REJECTED, loanRequestModel.getEvaluationResult());
    }

    @Test
    void buildLoanRequestModelShouldSetEvaluationResultOnRejectedWhenCustomerIsOlderThan65OnLoanEnds() throws Exception {

        //given
        LoanRequestDto loanRequestDto = LoanRequestDto.builder()
                .customerName("Tom")
                .customerTaxId("40080200695")
                .loanAmount(BigDecimal.valueOf(8000))
                .numberOfInstallments(17)
                .customerMonthlyIncome(BigDecimal.valueOf(20000))
                .customerBirthday(LocalDate.of(1956, 9, 17))
                .firstInstallmentDate(LocalDate.of(2020, 9, 26))
                .build();
        Mockito.when(objectValidator.validateObjectFields(loanRequestDto)).thenReturn(true);

        //when
        LoanRequestModel loanRequestModel = loanRequestService.buildLoanRequestModel(loanRequestDto);

        //then
        assertEquals(LoanRequestEvaluationResult.REJECTED, loanRequestModel.getEvaluationResult());
    }

    @Test
    void buildLoanRequestModelShouldSetEvaluationResultOnRejectedWhenCustomerCreditworthinessIsNotEnough() throws Exception {

        //given
        LoanRequestDto loanRequestDto = LoanRequestDto.builder()
                .customerName("Tom")
                .customerTaxId("40080200695")
                .loanAmount(BigDecimal.valueOf(10000))
                .numberOfInstallments(10)
                .customerMonthlyIncome(BigDecimal.valueOf(6933))
                .customerBirthday(LocalDate.of(1966, 9, 17))
                .firstInstallmentDate(LocalDate.of(2020, 9, 26))
                .build();
        Mockito.when(objectValidator.validateObjectFields(loanRequestDto)).thenReturn(true);
        //when
        LoanRequestModel loanRequestModel = loanRequestService.buildLoanRequestModel(loanRequestDto);

        //then
        assertEquals(LoanRequestEvaluationResult.REJECTED, loanRequestModel.getEvaluationResult());
    }

}
