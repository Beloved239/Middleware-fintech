/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.middleware.common.converter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */


@Slf4j
public class JsonConverter {

    public static <T> String toJson(T obj, boolean formatOutput) {
        String jsonString = "";
        JsonMapper jsonMapper = new JsonMapper();
        try {
            if (formatOutput) {
                jsonMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
                jsonString = jsonMapper.findAndRegisterModules().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(obj);
            } else {
                jsonString = jsonMapper.findAndRegisterModules().writeValueAsString(obj);
            }
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            log.error("error converting object to json", e);
        }
        return jsonString;

    }

    public static <T> T toObj(String jsonString, Class<T> objClass) {
        T obj = null;
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        try {
            obj = jsonMapper.findAndRegisterModules()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(jsonString, objClass);
        } catch (JsonProcessingException e) {
            log.error("error converting json {} to object: {}", jsonString, e.getMessage());
        }
        return obj;
    }


    public static <T> T toObj(JsonNode jsonNode, Class<T> objClass) {
        T obj = null;
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        jsonMapper.findAndRegisterModules().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            obj = jsonMapper.treeToValue(jsonNode, objClass);
        } catch (JsonProcessingException e) {
            log.error("error converting jsonNode to object", e);
        }
        return obj;
    }

    public static JsonNode toJsonNode(String json) {
        JsonNode jsonNode = null;
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        jsonMapper.findAndRegisterModules().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            jsonNode = jsonMapper.readTree(json);
        } catch (JsonProcessingException e) {
            log.error("error converting jsonNode to object", e);
        }
        return jsonNode;
    }

    public static JsonNode toJsonNode(byte[] binaryArray) {
        JsonNode jsonNode = null;
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        jsonMapper.findAndRegisterModules().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            jsonNode = jsonMapper.readTree(binaryArray);
        } catch (IOException e) {
            log.error("error converting BinaryArray to object", e);
        }
        return jsonNode;
    }
}
