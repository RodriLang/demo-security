package com.example.demosecurity.repositories;

import com.example.demosecurity.enums.RoleType;
import com.example.demosecurity.models.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByRoleName(RoleType roleName);

    boolean existsByRoleName(RoleType roleName);
}
