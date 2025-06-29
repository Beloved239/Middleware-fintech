package com.middleware.customer_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.middleware.customer_service.Utils.*;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MiddleWareResponse {
    private String responseCode;
    private String responseMessage;
    private boolean status;
    private Object data;

    public MiddleWareResponse() {}

    public MiddleWareResponse(String responseCode, String responseMessage, boolean status) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.status = status;
    }

    public MiddleWareResponse(String responseCode, String responseMessage,boolean status, Object data) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.data = data;
        this.status = status;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

    public MiddleWareResponse success() {
        return new MiddleWareResponse(SUCCESS_CODE,
                CREATED_MESSAGE, true);
    }

    public MiddleWareResponse success(Object data) {
        return new MiddleWareResponse(SUCCESS_CODE,
                SUCCESS_MESSAGE,true, data);
    }

    public MiddleWareResponse failure() {
        return new MiddleWareResponse(FAILURE_CODE,
                CREATED_MESSAGE,false);
    }

    public MiddleWareResponse failure(String message) {
        return new MiddleWareResponse(FAILURE_CODE, message,false);
    }

    public MiddleWareResponse created() {
        return new MiddleWareResponse(CREATED_CODE,
                CREATED_MESSAGE,true);
    }

    public MiddleWareResponse created(Object data) {
        return new MiddleWareResponse(CREATED_CODE,
                CREATED_MESSAGE,true, data);
    }
}
