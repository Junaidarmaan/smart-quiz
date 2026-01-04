package com.haisy.app.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haisy.app.Model.Results;

public interface ResultRepository extends JpaRepository<Results,Integer>{
    Optional<Results> findByJoinCode(String joinCode);
}
