package com.tatame.connect.feature.professor.repository;

import com.tatame.connect.feature.professor.repository.entity.ProfessorProfileEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorProfileRepository
    extends JpaRepository<ProfessorProfileEntity, UUID>, JpaSpecificationExecutor<ProfessorProfileEntity> {

    Optional<ProfessorProfileEntity> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);
}
