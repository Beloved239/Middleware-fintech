package com.middleware.transfer_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> {
    private String responseMessage;
    private String responseCode;
    private T payload;
}
