package com.asc.loanservice.repository;

import com.asc.loanservice.models.LoanRequestModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRequestRepository extends CrudRepository<LoanRequestModel, Long> {
}
