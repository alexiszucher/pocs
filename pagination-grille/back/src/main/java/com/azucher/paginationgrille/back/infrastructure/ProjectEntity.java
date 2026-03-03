package com.azucher.paginationgrille.back.infrastructure;

import jakarta.persistence.*;

@Entity
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private ClientEntity client;
    private String status;

    public String getName() {
        return name;
    }
    public String getStatus() {
        return status;
    }
    public ClientEntity getClient() {
        return client;
    }
}
