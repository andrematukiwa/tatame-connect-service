package com.tatame.connect.feature.professor.repository;

import com.tatame.connect.feature.professor.model.dto.ProfessorSearchFilter;
import com.tatame.connect.feature.professor.repository.entity.ProfessorProfileEntity;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class ProfessorSpecifications {

    private ProfessorSpecifications() {}

    public static Specification<ProfessorProfileEntity> withFilter(ProfessorSearchFilter filter) {
        return (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            if (filter.cidade() != null && !filter.cidade().isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("cidade")), filter.cidade().trim().toLowerCase()));
            }
            if (filter.faixa() != null) {
                predicates.add(cb.equal(root.get("faixa"), filter.faixa()));
            }
            if (filter.especialidade() != null) {
                predicates.add(cb.equal(root.join("especialidades"), filter.especialidade()));
            }
            if (filter.diaSemana() != null) {
                predicates.add(cb.equal(root.join("diasDisponiveis"), filter.diaSemana()));
            }
            if (filter.periodo() != null) {
                predicates.add(cb.equal(root.join("periodosDisponiveis"), filter.periodo()));
            }
            if (filter.valorMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("valorPretendido"), filter.valorMin()));
            }
            if (filter.valorMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("valorPretendido"), filter.valorMax()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
