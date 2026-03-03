package com.azucher.paginationgrille.back.infrastructure;

import org.springframework.data.jpa.domain.Specification;

public class ClientProjectViewSpecifications {
    static Specification<ClientProjectView> withClientName(String clientName) {
        return (root, query, cb) ->
                clientName == null ? null : cb.equal(root.get("clientName"), clientName);
    }
    static Specification<ClientProjectView> withProjectStatus(String status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("projectStatus"), status);
    }
    static Specification<ClientProjectView> withClientNameStartsWith(String prefix) {
        return (root, query, cb) ->
                prefix == null ? null : cb.like(root.get("clientName"), prefix + "%");
    }
}
