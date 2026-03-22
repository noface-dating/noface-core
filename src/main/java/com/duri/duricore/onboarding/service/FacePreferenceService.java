package com.duri.duricore.onboarding.service;

import com.duri.duricore.entity.Profile;
import com.duri.duricore.onboarding.dto.AnswerDto;
import com.duri.duricore.onboarding.exception.OnboardingException;
import com.duri.duricore.onboarding.exception.OnboardingErrorCode;
import com.duri.duricore.onboarding.model.Choice;
import com.duri.duricore.onboarding.model.Question;
import com.duri.duricore.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 얼굴 취향 이지선다 답변 처리 서비스
 * 
 * 정책:
 * - 중복 제출: 거부 (CONFLICT 에러 반환)
 * - profile이 없으면 예외 발생 (회원가입 단계 오류)
 * - face_preference가 이미 존재하면 중복 제출로 간주
 * - 타임아웃된 답변은 무시
 * - 답변을 배열로 저장: [0,0,0,0,0,1,1,1,1,1]
 *   - 각 인덱스는 질문 순서 (q1~q10)
 *   - 값은 선택지 (0: 첫 번째 선택지, 1: 두 번째 선택지)
 */
@Service
@RequiredArgsConstructor
public class FacePreferenceService {

    private final ProfileRepository profileRepository;
    private final QuestionLoaderService questionLoaderService;

    /**
     * 답변 제출 및 face_preference 배열로 저장
     * 
     * @param userId 사용자 ID
     * @param answers 답변 목록 (questionId, choiceId, isTimeout 포함)
     */
    @Transactional
    public void submitAnswers(Long userId, List<AnswerDto> answers) {
        Profile profile = profileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new OnboardingException(OnboardingErrorCode.PROFILE_NOT_FOUND));

        if (profile.getFacePreference() != null && !profile.getFacePreference().isEmpty()) {
            throw new OnboardingException(OnboardingErrorCode.DUPLICATE_SUBMISSION);
        }

        Boolean userGender = profile.getGender();
        String gender = Boolean.TRUE.equals(userGender) ? "MALE" : "FEMALE";

        List<Question> questions = questionLoaderService.getQuestionsByGender(gender);

        List<AnswerDto> sortedAnswers = answers.stream()
                .filter(a -> !Boolean.TRUE.equals(a.getIsTimeout()))
                .sorted(Comparator.comparing(AnswerDto::getQuestionId))
                .collect(Collectors.toList());

        List<Integer> facePreferenceArray = new ArrayList<>();
        
        for (Question question : questions) {
            String questionId = question.getQuestionId();
            
            AnswerDto answer = sortedAnswers.stream()
                    .filter(a -> a.getQuestionId().equals(questionId))
                    .findFirst()
                    .orElse(null);

            if (answer == null) {
                throw new OnboardingException(OnboardingErrorCode.INVALID_QUESTION_ID);
            }

            Choice choice;
            try {
                choice = questionLoaderService.getChoiceById(gender, questionId, answer.getChoiceId());
            } catch (RuntimeException e) {
                throw new OnboardingException(OnboardingErrorCode.INVALID_CHOICE_ID);
            }

            int choiceIndex = question.getChoices().indexOf(choice);
            if (choiceIndex == -1) {
                throw new OnboardingException(OnboardingErrorCode.INVALID_CHOICE_ID);
            }

            facePreferenceArray.add(choiceIndex);
        }

        profile.setFacePreference(facePreferenceArray);
        profileRepository.save(profile);
    }

    /**
     * 현재 사용자의 face_preference 조회
     * 
     * @param userId 사용자 ID
     * @return face_preference 배열
     */
    @Transactional(readOnly = true)
    public List<Integer> getFacePreferenceResult(Long userId) {
        Profile profile = profileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new OnboardingException(OnboardingErrorCode.PROFILE_NOT_FOUND));

        return profile.getFacePreference() != null ? profile.getFacePreference() : new ArrayList<>();
    }
}
