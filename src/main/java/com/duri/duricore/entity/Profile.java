package com.duri.duricore.entity;

import com.duri.duricore.converter.FacePreferenceConverter;
import com.duri.duricore.converter.JsonMapConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "gender", nullable = false)
    private Boolean gender;

    @Column(name = "region", nullable = false, length = 50)
    private String region;

    @Column(name = "hobbies", columnDefinition = "JSON")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, Object> hobbies;

    @Column(name = "face_features", nullable = false, length = 10, columnDefinition = "CHAR(10)")
    @Convert(converter = FacePreferenceConverter.class)
    private List<Integer> faceFeatures;

    @Column(name = "face_preference", nullable = false, length = 10, columnDefinition = "CHAR(10)")
    @Convert(converter = FacePreferenceConverter.class)
    private List<Integer> facePreference;

    @Column(name = "additional_information", columnDefinition = "JSON")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, Object> additionalInformation;

    @Column(name = "absolute_score", nullable = false)
    private Byte absoluteScore;
}

