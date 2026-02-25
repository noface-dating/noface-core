package com.duri.duricore.chat.repository.mysql;

import com.duri.duricore.chat.entity.mysql.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findByuserId(Long userId);

    default List<Users> findAllByUsersId(Long... userIds) {
        return findAllById(List.of(userIds));
    }



}
