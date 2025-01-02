package com.apec_finance.cash.service;

import com.apec_finance.cash.config.FormFeignEncoderConfig;
import com.apec_finance.cash.model.InvestorBankAcc;
import com.apec_finance.cash.model.RsInvestorBankAcc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam; 
import java.util.List;


@FeignClient(name = "app-client", url = "${spring.feign.client.config.app-client.url}", configuration = FormFeignEncoderConfig.class)
public interface AppClient {

    @GetMapping("/items/investors_bank_acc")
    RsInvestorBankAcc  getInvestorBankAcc(@RequestParam("filter[investor_id][_eq]") Long filter, @RequestParam String fields);
}
