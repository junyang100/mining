package com.mine.hdService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HdResult<T> {
    private int status;
    private boolean flag;
    private String message;
    private T data;

    public HdResult success(T t){
        status = 0;
        flag = true;
        message = "success";
        data = t;
        return this;
    }

    public HdResult fail(){
        status = 1;
        flag = false;
        message = "fail";
        data = null;
        return this;
    }

}
