package com.example.system.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.system.Entity.Team;

@Repository
public interface TeamRepo extends JpaRepository<Team, Long>{

    Optional<Team> findById(Long id);
}
