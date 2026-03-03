package com.azucher.paginationgrille.back.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;
import org.springframework.data.annotation.Immutable;

@Entity
@Immutable
@Subselect("""
    SELECT p.id          AS id,
           p.name        AS project_name,
           p.status      AS project_status,
           c.name        AS client_name
    FROM project_entity p
    JOIN client_entity c ON p.client_id = c.id
""")
@Synchronize({"project_entity", "client_entity"})
public class ClientProjectView {
    @Id
    private String id;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_status")
    private String projectStatus;

    @Column(name = "client_name")
    private String clientName;

    // getters
    public String id() {
        return id;
    }
    public String projectName() {
        return projectName;
    }
    public String projectStatus() {
        return projectStatus;
    }
    public String clientName() {
        return clientName;
    }
}
