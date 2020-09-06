package com.asc.loanservice.api;

import com.asc.loanservice.contracts.LoanRequestDto;
import com.asc.loanservice.contracts.LoanRequestEvaluationResult;
import com.asc.loanservice.contracts.LoanRequestRegistrationResultDto;
import com.asc.loanservice.domain.LoanRequestService;
import com.asc.loanservice.models.LoanRequestModel;
import com.asc.loanservice.repository.LoanRequestRepository;
import com.asc.loanservice.utils.converters.LocalDateAdapter;
import com.google.gson.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(LoanRequestController.class)
class LoanRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    LoanRequestRepository loanRequestRepository;

    @MockBean
    LoanRequestService loanRequestService;


    @Test
    void registerShouldReturnApprovedLoanRequestRegistrationResult() throws Exception{

        //given
        LoanRequestModel loanRequestModel = LoanRequestModel.builder()
                .loanRequestNumber(100000000L)
                .customerName("Tom")
                .customerTaxId("40080207695")
                .loanAmount(BigDecimal.valueOf(8000))
                .numberOfInstallments(17)
                .customerMonthlyIncome(BigDecimal.valueOf(20000))
                .customerBirthday(LocalDate.of(1980, 9, 17))
                .firstInstallmentDate(LocalDate.of(2021, 9, 26))
                .registrationDate(LocalDateTime.now())
                .evaluationResult(LoanRequestEvaluationResult.APPROVED)
                .build();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        when(loanRequestService.buildLoanRequestModel(any(LoanRequestDto.class))).thenReturn(loanRequestModel);

        //when
        MvcResult mvcResult = mockMvc
                .perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new LoanRequestDto())))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertEquals(LoanRequestEvaluationResult.APPROVED, new Gson().fromJson(mvcResult.getResponse().
                getContentAsString(),LoanRequestRegistrationResultDto.class).getEvaluationResult());
    }

    @Test
    void registerShouldReturnRejectedLoanRequestRegistrationResult() throws Exception{

        //given
        LoanRequestModel loanRequestModel = LoanRequestModel.builder()
                .loanRequestNumber(100000000L)
                .customerName("Tom")
                .customerTaxId("40080207695")
                .loanAmount(BigDecimal.valueOf(8000))
                .numberOfInstallments(17)
                .customerMonthlyIncome(BigDecimal.valueOf(20000))
                .customerBirthday(LocalDate.of(1965, 9, 17))
                .firstInstallmentDate(LocalDate.of(2021, 9, 26))
                .registrationDate(LocalDateTime.now())
                .evaluationResult(LoanRequestEvaluationResult.REJECTED)
                .build();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        when(loanRequestService.buildLoanRequestModel(any(LoanRequestDto.class))).thenReturn(loanRequestModel);

        //when
        MvcResult mvcResult = mockMvc
                .perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new LoanRequestDto())))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertEquals(LoanRequestEvaluationResult.REJECTED, new Gson().fromJson(mvcResult.getResponse().
                getContentAsString(),LoanRequestRegistrationResultDto.class).getEvaluationResult());
    }

    @Test
    void getByNumberShouldReturnEmptyLoanRequestDto() throws Exception {

        //given

        //when
        MvcResult mvcResult = mockMvc
                .perform(get("/api/loans/100000000"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertTrue(mvcResult.getResponse().getContentAsString().contains("null"));
    }

    @Test
    void getByNumberShouldReturnSavedLoanRequestById() throws Exception {
        //given
        LoanRequestModel loanRequestModel = LoanRequestModel.builder()
                .loanRequestNumber(100000000L)
                .customerName("Tom")
                .customerTaxId("40080207695")
                .loanAmount(BigDecimal.valueOf(8000))
                .numberOfInstallments(17)
                .customerMonthlyIncome(BigDecimal.valueOf(20000))
                .customerBirthday(LocalDate.of(1980, 9, 17))
                .firstInstallmentDate(LocalDate.of(2021, 9, 26))
                .registrationDate(LocalDateTime.now())
                .evaluationResult(LoanRequestEvaluationResult.APPROVED)
                .build();
        when(loanRequestRepository.findById(100000000L)).thenReturn(Optional.of(loanRequestModel));

        //when
        MvcResult mvcResult = mockMvc
                .perform(get("/api/loans/100000000"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertFalse(mvcResult.getResponse().getContentAsString().contains("null"));
    }

}
