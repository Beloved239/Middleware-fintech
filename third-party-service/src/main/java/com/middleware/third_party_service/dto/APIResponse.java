package com.middleware.third_party_service.dto;

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
    private String message;
    private String code;
    private T payload;
}
