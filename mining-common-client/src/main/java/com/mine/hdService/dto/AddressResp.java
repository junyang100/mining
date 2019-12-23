package com.mine.hdService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResp {
    @JsonProperty("BTC")
    private String btc;
    @JsonProperty("ETH")
    private String eth;
}
