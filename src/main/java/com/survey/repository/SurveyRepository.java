package com.survey.repository;

import com.survey.model.Survey;
import com.survey.model.User;
import com.survey.model.enums.SurveyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findByOwner(User owner);
    List<Survey> findByStatus(SurveyStatus status);
}
