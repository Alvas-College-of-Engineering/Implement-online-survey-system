package com.survey.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.survey.dto.QuestionDto;
import com.survey.model.Question;
import com.survey.model.Survey;
import com.survey.repository.QuestionRepository;
import com.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;
    private final ObjectMapper objectMapper;

    public Question addQuestion(QuestionDto dto) {
        Survey survey = surveyRepository.findById(dto.getSurveyId())
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        List<String> finalOptions = dto.getOptions();
        // If the form sent a single string with commas, split it
        if (finalOptions != null && finalOptions.size() == 1 && finalOptions.get(0).contains(",")) {
            finalOptions = List.of(finalOptions.get(0).split("\\s*,\\s*"));
        }

        String optionsJson = null;
        if (finalOptions != null && !finalOptions.isEmpty()) {
            try {
                optionsJson = objectMapper.writeValueAsString(finalOptions);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error serializing options", e);
            }
        }

        Question question = Question.builder()
                .survey(survey)
                .type(dto.getType())
                .text(dto.getText())
                .options(optionsJson)
                .build();

        return questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        questionRepository.delete(question);
    }

    public List<Question> getQuestionsBySurvey(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));
        return questionRepository.findBySurvey(survey);
    }
}
