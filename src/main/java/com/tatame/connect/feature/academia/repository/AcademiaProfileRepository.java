package com.tatame.connect.feature.academia.repository;

import com.tatame.connect.feature.academia.repository.entity.AcademiaProfileEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademiaProfileRepository extends JpaRepository<AcademiaProfileEntity, UUID> {

    Optional<AcademiaProfileEntity> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);
}
