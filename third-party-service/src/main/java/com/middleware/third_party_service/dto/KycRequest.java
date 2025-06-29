package com.middleware.third_party_service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Getter
@Setter
@ToString
public class KycRequest {
    private String bvnNumber;
    private String ninNumber;
}
