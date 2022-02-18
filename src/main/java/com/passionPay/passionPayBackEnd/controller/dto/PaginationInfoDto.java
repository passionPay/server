package com.passionPay.passionPayBackEnd.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationInfoDto {
    private int pageSize;
    private int pageNumber;
}
