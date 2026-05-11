package com.survey.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSubmitDto {
    @NotNull
    private Long surveyId;

    @NotNull
    private Map<Long, String> answers;
}
