-- MariaDB 테스트 데이터 (이미 존재하면 INSERT IGNORE로 스킵)
INSERT IGNORE INTO users (user_id, username, password, email, platform_type, last_login_at, created_at)
VALUES (1, 'testuser', 'pw', 'test@example.com', 'NATIVE', NULL, NOW());

INSERT IGNORE INTO profile (profile_id, user_id, nickname, birth_date, gender, region, hobbies, face_features, face_preference, additional_information, absolute_score)
VALUES (1, 1, '테스트유저', '1995-01-01', TRUE, '서울', '{}', '0000000000', '0000000000', '{}', 50);
