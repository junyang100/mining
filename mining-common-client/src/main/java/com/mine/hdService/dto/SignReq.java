package com.mine.hdService.dto;


import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignReq {
    private String[] path;
    private String txStr;
    private int network;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
