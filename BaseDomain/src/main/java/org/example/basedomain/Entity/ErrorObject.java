package org.example.basedomain.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorObject {
    private String message;
    private String statusCode;
    private Date timestamp;
}
