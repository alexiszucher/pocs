package com.azucher.paginationgrille.back.application;

import com.azucher.paginationgrille.back.readmodel.ClientProject;

import java.util.List;

public interface FindClientProjects {
    List<ClientProject> find(ClientProjectsQuery query);

    default List<ClientProject> find(int page, int pageSize) {
        return find(new ClientProjectsQuery(page, pageSize, null, null, null, null));
    }
}
