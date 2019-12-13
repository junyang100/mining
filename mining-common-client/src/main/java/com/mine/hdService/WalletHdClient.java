package com.mine.hdService;


import com.mine.hdService.dto.AddressReq;
import com.mine.hdService.dto.AddressResp;
import com.mine.hdService.dto.HdResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "walletHD", url = "${wallet.hdService.url}")
public interface WalletHdClient {

    @RequestMapping(value = "/front/wallet/address", method = RequestMethod.POST)
    HdResult<AddressResp> getAddress(@RequestBody AddressReq req);


}
