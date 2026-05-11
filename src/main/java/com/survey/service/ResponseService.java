package com.survey.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.survey.dto.ResponseSubmitDto;
import com.survey.dto.SurveyResultDto;
import com.survey.exception.ResourceNotFoundException;
import com.survey.model.Question;
import com.survey.model.Response;
import com.survey.model.Survey;
import com.survey.repository.QuestionRepository;
import com.survey.repository.ResponseRepository;
import com.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;

    public Response submitResponse(ResponseSubmitDto dto, String ip) {
        Survey survey = surveyRepository.findById(dto.getSurveyId())
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));

        String answersJson;
        try {
            answersJson = objectMapper.writeValueAsString(dto.getAnswers());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing answers", e);
        }

        Response response = Response.builder()
                .survey(survey)
                .answers(answersJson)
                .respondentIp(ip)
                .build();

        return responseRepository.save(response);
    }

    public SurveyResultDto getResultsBySurvey(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));

        List<Response> responses = responseRepository.findBySurvey(survey);
        List<Question> questions = questionRepository.findBySurvey(survey);

        Map<Long, Map<String, Integer>> answerBreakdown = new HashMap<>();

        // Initialize breakdown map for each question
        for (Question question : questions) {
            answerBreakdown.put(question.getId(), new HashMap<>());
        }

        for (Response response : responses) {
            try {
                Map<Long, String> responseAnswers = objectMapper.readValue(
                        response.getAnswers(),
                        new TypeReference<Map<Long, String>>() {}
                );

                for (Map.Entry<Long, String> entry : responseAnswers.entrySet()) {
                    Long questionId = entry.getKey();
                    String answer = entry.getValue();

                    if (answerBreakdown.containsKey(questionId)) {
                        Map<String, Integer> counts = answerBreakdown.get(questionId);
                        counts.put(answer, counts.getOrDefault(answer, 0) + 1);
                    }
                }
            } catch (JsonProcessingException e) {
                // Log error or handle corrupted JSON
            }
        }

        return new SurveyResultDto(surveyId, responses.size(), answerBreakdown);
    }
}
