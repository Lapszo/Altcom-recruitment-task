package com.asc.loanservice.api;

import com.asc.loanservice.contracts.LoanRequestDataDto;
import com.asc.loanservice.contracts.LoanRequestDto;
import com.asc.loanservice.contracts.LoanRequestRegistrationResultDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanRequestController {

    @PostMapping
    public LoanRequestRegistrationResultDto register(@RequestBody LoanRequestDto loanRequest){
        //TODO: implement
        return null;
    }

    @GetMapping("/{loanNumber}")
    public LoanRequestDataDto getByNumber(@PathVariable("loanNumber") String loanNumber){
        //TODO: implement
        return null;
    }
}
