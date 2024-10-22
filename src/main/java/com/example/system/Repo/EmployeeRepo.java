package com.example.system.Repo;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.system.Entity.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long>{

    Optional<Employee> findByEmail(String email);

    Employee findUserByEmail(String email);

    Employee findByName(String name);

    List<Employee> findByTeamId(Long teamId);

    List<Employee> findAll();

    @Query("SELECT e FROM Employee e WHERE e.name LIKE %:username% AND (e.team.name = :team OR :team = 'All')")
    List<Employee> searchEmployees(@Param("username") String username, @Param("team") String team);

    Employee findByEmailAndPassword(String email, String password);
}

