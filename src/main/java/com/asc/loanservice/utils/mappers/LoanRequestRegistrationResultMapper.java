package com.asc.loanservice.utils.mappers;

import com.asc.loanservice.contracts.LoanRequestRegistrationResultDto;
import com.asc.loanservice.models.LoanRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LoanRequestRegistrationResultMapper {

    LoanRequestRegistrationResultMapper INSTANCE = Mappers.getMapper(LoanRequestRegistrationResultMapper.class);

    LoanRequestRegistrationResultDto mapToLoanRequestRegistrationResultDto(LoanRequestModel loanRequestModel);
}
