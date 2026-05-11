package com.survey.controller;

import com.survey.dto.QuestionDto;
import com.survey.dto.SurveyDto;
import com.survey.model.Survey;
import com.survey.model.User;
import com.survey.service.QuestionService;
import com.survey.service.SurveyService;
import com.survey.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminSurveyController {

    private final SurveyService surveyService;
    private final QuestionService questionService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("surveys", surveyService.getAllSurveys());
        return "admin/dashboard";
    }

    @GetMapping("/surveys/new")
    public String newSurveyForm(Model model) {
        model.addAttribute("surveyDto", new SurveyDto());
        return "admin/survey-form";
    }

    @PostMapping("/surveys")
    public String createSurvey(@Valid @ModelAttribute("surveyDto") SurveyDto dto,
                               BindingResult result,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (result.hasErrors()) {
            return "admin/survey-form";
        }
        User user = userDetails.getUser();
        surveyService.createSurvey(dto, user);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/surveys/{id}/edit")
    public String editSurveyForm(@PathVariable Long id, Model model) {
        Survey survey = surveyService.getSurveyById(id);
        SurveyDto dto = new SurveyDto(survey.getTitle(), survey.getDescription());
        model.addAttribute("surveyDto", dto);
        model.addAttribute("surveyId", id);
        return "admin/survey-form";
    }

    @PostMapping("/surveys/{id}")
    public String updateSurvey(@PathVariable Long id,
                               @Valid @ModelAttribute("surveyDto") SurveyDto dto,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "admin/survey-form";
        }
        surveyService.updateSurvey(id, dto);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/surveys/{id}/delete")
    public String deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurvey(id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/surveys/{id}/publish")
    public String publishSurvey(@PathVariable Long id) {
        surveyService.publishSurvey(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/surveys/{id}/questions")
    public String manageQuestions(@PathVariable Long id, Model model) {
        model.addAttribute("survey", surveyService.getSurveyById(id));
        model.addAttribute("questions", questionService.getQuestionsBySurvey(id));
        QuestionDto questionDto = new QuestionDto();
        questionDto.setSurveyId(id);
        model.addAttribute("questionDto", questionDto);
        return "admin/questions";
    }

    @PostMapping("/surveys/{id}/questions")
    public String addQuestion(@PathVariable Long id,
                              @ModelAttribute("questionDto") QuestionDto dto) {
        dto.setSurveyId(id);
        questionService.addQuestion(dto);
        return "redirect:/admin/surveys/" + id + "/questions";
    }

    @PostMapping("/surveys/{surveyId}/questions/{questionId}/delete")
    public String deleteQuestion(@PathVariable Long surveyId, @PathVariable Long questionId) {
        questionService.deleteQuestion(questionId);
        return "redirect:/admin/surveys/" + surveyId + "/questions";
    }
}
