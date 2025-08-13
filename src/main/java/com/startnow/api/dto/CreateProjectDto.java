package com.startnow.api.dto;

import lombok.Data;

@Data
public class CreateProjectDto {
    private String htmlContent;
    private String cssContent;
    private String jsContent;
}
