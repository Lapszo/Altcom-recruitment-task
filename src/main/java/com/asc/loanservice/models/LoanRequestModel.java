package com.asc.loanservice.models;

import com.asc.loanservice.contracts.LoanRequestEvaluationResult;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loan_requests")
public class LoanRequestModel {

    @Id
    @SequenceGenerator(name="loanRequestSequenceGenerator", initialValue=100000000, allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loanRequestSequenceGenerator")
    @Column(name = "loanRequestNumber")
    private Long loanRequestNumber;
    @NotNull
    @NotBlank
    private String customerName;
    @NotNull
    private LocalDate customerBirthday;
    @NotNull
    @NotBlank
    private String customerTaxId;
    @NotNull
    private BigDecimal customerMonthlyIncome;
    @NotNull
    private BigDecimal loanAmount;
    @NotNull
    private Integer numberOfInstallments;
    @NotNull
    private LocalDate firstInstallmentDate;
    @NotNull
    @Enumerated(EnumType.STRING)
    private LoanRequestEvaluationResult evaluationResult;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime registrationDate;

}
