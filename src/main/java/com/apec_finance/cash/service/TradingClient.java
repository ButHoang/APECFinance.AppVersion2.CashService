package com.apec_finance.cash.service;

import com.apec_finance.cash.config.FormFeignEncoderConfig;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam; 
import java.util.List;
import java.util.Map;
import com.apec_finance.cash.comon.ResponseBuilder;
import org.springframework.web.bind.annotation.RequestHeader;



@FeignClient(name = "trading-client", url = "${spring.feign.client.config.trading-client.url}", configuration = FormFeignEncoderConfig.class)
public interface TradingClient {

    @GetMapping("/order/product-id?orderIds={orderIdss}")
    ResponseBuilder<Map<String, Integer>> getProductIdsWithOrderIds(@RequestHeader("Authorization") String token,  @RequestParam("orderIdss") String orderIds);

}
