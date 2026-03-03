package com.azucher.paginationgrille.back.infrastructure;

import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ClientProjectSpecifications {
    static Specification<ProjectEntity> withClientName(String clientName) {
        return (root, query, criteriaBuilder) ->
                clientName == null ? null : criteriaBuilder.equal(root.get("client").get("name"), clientName);
    }

    static Specification<ProjectEntity> withProjectStatus(String status) {
        return (root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    static Specification<ProjectEntity> withClientNameStartsWith(String prefix) {
        return (root, query, criteriaBuilder) ->
                prefix == null ? null : criteriaBuilder.like(root.get("client").get("name"), prefix + "%");
    }

    static Specification<ProjectEntity> withFetchClient() {
        return (root, query, cb) -> {
            if (!query.getResultType().equals(Long.class)) {
                root.fetch("client", JoinType.INNER);
            }
            return null;
        };
    }
}
