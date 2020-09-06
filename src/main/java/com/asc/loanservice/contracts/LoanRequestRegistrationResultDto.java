package com.asc.loanservice.contracts;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestRegistrationResultDto {
    private String loanRequestNumber;
    private LoanRequestEvaluationResult evaluationResult;
}
