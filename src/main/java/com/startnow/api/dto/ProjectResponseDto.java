package com.startnow.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDto {
    private Long id;
    private String htmlContent;
    private String cssContent;
    private String jsContent;
    private Long userId;
}
