package com.duri.duricore.onboarding.controller;

import com.duri.duricore.onboarding.dto.*;
import com.duri.duricore.onboarding.model.Choice;
import com.duri.duricore.onboarding.model.Question;
import com.duri.duricore.onboarding.service.FacePreferenceService;
import com.duri.duricore.onboarding.service.QuestionLoaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 회원가입 취향 이지선다 API Controller
 */
@RestController
@RequestMapping("/api/onboarding/face-preference")
@RequiredArgsConstructor
public class OnboardingController {

    private final QuestionLoaderService questionLoaderService;
    private final FacePreferenceService facePreferenceService;

    /**
     * GET /api/onboarding/face-preference/questions?gender=MALE
     * 성별에 따른 얼굴 취향 질문 목록 조회
     * 
     * @param gender "MALE" or "FEMALE"
     */
    @GetMapping("/questions")
    public ResponseEntity<List<QuestionResponse>> getQuestions(@RequestParam String gender) {
        List<Question> questions = questionLoaderService.getQuestionsByGender(gender);
        
        List<QuestionResponse> responses = questions.stream()
                .map(this::convertToQuestionResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * POST /api/onboarding/face-preference/answers
     * 얼굴 취향 답변 제출
     */
    @PostMapping("/answers")
    public ResponseEntity<Void> submitAnswers(@RequestBody AnswerRequest request) {
        facePreferenceService.submitAnswers(request.getUserId(), request.getAnswers());
        return ResponseEntity.ok().build();
    }

    /**
     * GET /api/onboarding/face-preference/result?userId=1
     * 얼굴 취향 결과 조회
     */
    @GetMapping("/result")
    public ResponseEntity<FacePreferenceResultResponse> getResult(@RequestParam Long userId) {
        var facePreference = facePreferenceService.getFacePreferenceResult(userId);
        
        FacePreferenceResultResponse response = FacePreferenceResultResponse.builder()
                .facePreference(facePreference)
                .build();
        
        return ResponseEntity.ok(response);
    }

    private QuestionResponse convertToQuestionResponse(Question question) {
        List<ChoiceResponse> choiceResponses = question.getChoices().stream()
                .map(this::convertToChoiceResponse)
                .collect(Collectors.toList());

        return QuestionResponse.builder()
                .questionId(question.getQuestionId())
                .questionText(question.getQuestionText())
                .timeLimit(question.getTimeLimit())
                .choices(choiceResponses)
                .build();
    }

    private ChoiceResponse convertToChoiceResponse(Choice choice) {
        return ChoiceResponse.builder()
                .choiceId(choice.getChoiceId())
                .imageUrl(choice.getImageUrl())
                .build();
    }
}
