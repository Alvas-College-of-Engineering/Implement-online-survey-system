package com.survey.controller;

import com.survey.dto.SurveyResultDto;
import com.survey.model.Survey;
import com.survey.service.QuestionService;
import com.survey.service.ResponseService;
import com.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ResultsController {

    private final ResponseService responseService;
    private final SurveyService surveyService;
    private final QuestionService questionService;

    @GetMapping("/admin/surveys/{id}/results")
    public String viewResults(@PathVariable Long id, Model model) {
        Survey survey = surveyService.getSurveyById(id);
        SurveyResultDto result = responseService.getResultsBySurvey(id);
        
        model.addAttribute("survey", survey);
        model.addAttribute("result", result);
        model.addAttribute("questions", questionService.getQuestionsBySurvey(id));
        
        return "admin/results";
    }
}
