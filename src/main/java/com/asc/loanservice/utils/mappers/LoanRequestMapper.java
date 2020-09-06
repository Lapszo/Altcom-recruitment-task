package com.asc.loanservice.utils.mappers;

import com.asc.loanservice.contracts.LoanRequestDataDto;
import com.asc.loanservice.models.LoanRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LoanRequestMapper {

    LoanRequestMapper INSTANCE = Mappers.getMapper(LoanRequestMapper.class);

    LoanRequestDataDto mapToLoanRequestDataDto(LoanRequestModel loanRequestModel);

}
