package com.survey.repository;

import com.survey.model.Response;
import com.survey.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findBySurvey(Survey survey);
}
