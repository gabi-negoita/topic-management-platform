package com.mtapo.app.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class FormResponse {

    public static String STATUS_SUCCESS = "success";
    public static String STATUS_ERROR = "negative";

    private String message;
    private String status;
    private String description;
}
