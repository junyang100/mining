package com.mine.hdService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressReq {
    private String path;
    //0 正式网络 1 测试网络
    private int network;
    private String curType;
}
