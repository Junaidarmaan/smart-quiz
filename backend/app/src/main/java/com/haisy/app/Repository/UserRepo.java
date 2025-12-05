package com.haisy.app.Repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.haisy.app.Model.User;

@Repository
public interface UserRepo extends JpaRepository<User,Integer>{
    boolean existsByUserName(String username);
    Optional<User> findByUserName(String userName);
}
