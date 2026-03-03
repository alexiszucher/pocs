package com.azucher.paginationgrille.back.infrastructure;

import com.azucher.paginationgrille.back.application.*;
import com.azucher.paginationgrille.back.readmodel.ClientProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.azucher.paginationgrille.back.application.SortField.PROJECT_NAME;
import static com.azucher.paginationgrille.back.infrastructure.ClientProjectSpecifications.*;

@Component
public class JPAFindClientProjects implements FindClientProjects {
    private final JPAClientProjectRepository clientProjectRepository;

    public JPAFindClientProjects(JPAClientProjectRepository clientProjectRepository) {
        this.clientProjectRepository = clientProjectRepository;
    }

    @Override
    public List<ClientProject> find(ClientProjectsQuery query) {
        Sort sort = buildSort(query.sortCriteriaList());
        PageRequest pageRequest = PageRequest.of(query.page() - 1, query.pageSize(), sort);
        Specification<ProjectEntity> spec = Specification
                .where(withFetchClient())
                .and(withClientName(query.clientName()))
                .and(withProjectStatus(query.projectStatus()))
                .and(withClientNameStartsWith(query.clientNameStartsWith()));

        Page<ProjectEntity> page = clientProjectRepository.findAll(spec, pageRequest);

        return page.getContent().stream()
                .map(p -> new ClientProject(p.getName(), p.getClient().getName()))
                .toList();
    }

    private Sort buildSort(List<SortCriteria> sortCriteriaList) {
        if (sortCriteriaList == null || sortCriteriaList.isEmpty()) {
            return Sort.by("name").ascending();
        }
        return sortCriteriaList.stream()
                .map(sc -> sc.direction() == SortDirection.ASC
                        ? Sort.by(toPropertyPath(sc.field())).ascending()
                        : Sort.by(toPropertyPath(sc.field())).descending())
                .reduce(Sort.unsorted(), Sort::and);
    }

    private String toPropertyPath(SortField field) {
        return switch (field) {
            case PROJECT_NAME -> "name";
            case CLIENT_NAME  -> "client.name";
        };
    }
}
