package com.azucher.paginationgrille.back.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

@Component
public interface JPAClientProjectViewRepository
        extends JpaRepository<ClientProjectView, String>,
        JpaSpecificationExecutor<ClientProjectView> {}
