package com.asc.loanservice.contracts;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCheckResultDto {
    private String customerTaxId;
    private Boolean isRegisteredDebtor;
}
