package org.example.orderservice.Entity;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorObject {
    private String message;
    private String statusCode;
    private Date timestamp;
}
