package com.survey.repository;

import com.survey.model.Question;
import com.survey.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySurvey(Survey survey);
}
