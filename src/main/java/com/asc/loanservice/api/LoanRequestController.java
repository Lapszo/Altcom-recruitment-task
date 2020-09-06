package com.asc.loanservice.api;

import com.asc.loanservice.contracts.LoanRequestDataDto;
import com.asc.loanservice.contracts.LoanRequestDto;
import com.asc.loanservice.contracts.LoanRequestRegistrationResultDto;
import com.asc.loanservice.domain.LoanRequestService;
import com.asc.loanservice.models.LoanRequestModel;
import com.asc.loanservice.repository.LoanRequestRepository;
import com.asc.loanservice.utils.mappers.LoanRequestMapper;
import com.asc.loanservice.utils.mappers.LoanRequestRegistrationResultMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanRequestController {

    private final LoanRequestRepository loanRequestRepository;
    private final LoanRequestService loanRequestService;


    public LoanRequestController(LoanRequestRepository loanRequestRepository, LoanRequestService loanRequestService) {
        this.loanRequestRepository = loanRequestRepository;
        this.loanRequestService = loanRequestService;
}

    @PostMapping
    public LoanRequestRegistrationResultDto register(@RequestBody LoanRequestDto loanRequest) {
        LoanRequestModel loanRequestModel = loanRequestService.buildLoanRequestModel(loanRequest);
        loanRequestRepository.save(loanRequestModel);
        return LoanRequestRegistrationResultMapper.INSTANCE
                .mapToLoanRequestRegistrationResultDto(loanRequestModel);
    }

    @GetMapping("/{loanNumber}")
    public LoanRequestDataDto getByNumber(@PathVariable("loanNumber") String loanNumber){
        LoanRequestModel loanRequestModel = loanRequestRepository.findById(Long.parseLong(loanNumber))
                .orElse(new LoanRequestModel());
        return LoanRequestMapper.INSTANCE
                .mapToLoanRequestDataDto(loanRequestModel);
    }

}
