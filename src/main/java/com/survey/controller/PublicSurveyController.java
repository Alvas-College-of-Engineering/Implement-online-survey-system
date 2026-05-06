package com.survey.controller;

import com.survey.dto.ResponseSubmitDto;
import com.survey.service.QuestionService;
import com.survey.service.ResponseService;
import com.survey.service.SurveyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PublicSurveyController {

    private final SurveyService surveyService;
    private final QuestionService questionService;
    private final ResponseService responseService;

    @GetMapping("/surveys")
    public String listSurveys(Model model) {
        model.addAttribute("surveys", surveyService.getAllPublished());
        return "public/survey-list";
    }

    @GetMapping("/surveys/{id}")
    public String viewSurvey(@PathVariable Long id, Model model) {
        model.addAttribute("survey", surveyService.getSurveyById(id));
        model.addAttribute("questions", questionService.getQuestionsBySurvey(id));
        model.addAttribute("responseDto", new ResponseSubmitDto());
        return "public/survey-fill";
    }

    @PostMapping("/surveys/{id}/respond")
    public String submitResponse(@PathVariable Long id,
                                 @ModelAttribute("responseDto") ResponseSubmitDto dto,
                                 HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        dto.setSurveyId(id);
        responseService.submitResponse(dto, ip);
        return "redirect:/surveys/" + id + "?submitted=true";
    }
}
