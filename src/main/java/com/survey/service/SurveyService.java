package com.survey.service;

import com.survey.dto.SurveyDto;
import com.survey.model.Survey;
import com.survey.model.User;
import com.survey.model.enums.SurveyStatus;
import com.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SurveyService {

    private final SurveyRepository surveyRepository;

    public Survey createSurvey(SurveyDto dto, User owner) {
        Survey survey = Survey.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .owner(owner)
                .status(SurveyStatus.DRAFT)
                .build();
        return surveyRepository.save(survey);
    }

    public Survey updateSurvey(Long id, SurveyDto dto) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found"));
        survey.setTitle(dto.getTitle());
        survey.setDescription(dto.getDescription());
        return surveyRepository.save(survey);
    }

    public void deleteSurvey(Long id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found"));
        surveyRepository.delete(survey);
    }

    public Survey publishSurvey(Long id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found"));
        survey.setStatus(SurveyStatus.PUBLISHED);
        return surveyRepository.save(survey);
    }

    public List<Survey> getAllPublished() {
        return surveyRepository.findByStatus(SurveyStatus.PUBLISHED);
    }

    public List<Survey> getAllSurveys() {
        return surveyRepository.findAll();
    }

    public Survey getSurveyById(Long id) {
        return surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found"));
    }
}
