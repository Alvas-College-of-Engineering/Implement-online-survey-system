package com.survey.dto;

import com.survey.model.enums.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    @NotNull
    private Long surveyId;

    @NotNull
    private QuestionType type;

    @NotBlank
    private String text;

    private List<String> options;
}
