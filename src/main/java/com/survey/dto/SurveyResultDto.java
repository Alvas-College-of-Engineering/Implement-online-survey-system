package com.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResultDto {
    private Long surveyId;
    private int totalResponses;
    private Map<Long, Map<String, Integer>> answerBreakdown;
}
