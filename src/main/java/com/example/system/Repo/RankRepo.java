package com.example.system.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.system.Entity.Rank;

@Repository
public interface RankRepo extends JpaRepository<Rank, Long>{

    Optional<Rank> findById(Long id);
}
