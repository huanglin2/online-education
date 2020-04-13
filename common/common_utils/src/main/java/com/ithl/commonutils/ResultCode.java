package com.ithl.commonutils;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author hl
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(0, "成功"),
    ERROR(1, "失败");

    private Integer stateCode;
    private String stateText;
}
