package com.azucher.paginationgrille.back.infrastructure;

import com.azucher.paginationgrille.back.application.*;
import com.azucher.paginationgrille.back.readmodel.ClientProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.azucher.paginationgrille.back.infrastructure.ClientProjectViewSpecifications.*;

@Component
public class JPAFindClientProjects implements FindClientProjects {
    private final JPAClientProjectViewRepository clientProjectViewRepository;

    public JPAFindClientProjects(JPAClientProjectViewRepository clientProjectViewRepository) {
        this.clientProjectViewRepository = clientProjectViewRepository;
    }

    @Override
    public List<ClientProject> find(ClientProjectsQuery query) {
        Sort sort = buildSort(query.sortCriteriaList());
        PageRequest pageRequest = PageRequest.of(query.page() - 1, query.pageSize(), sort);
        Specification<ClientProjectView> spec = Specification
                .where(withClientName(query.clientName()))
                .and(withProjectStatus(query.projectStatus()))
                .and(withClientNameStartsWith(query.clientNameStartsWith()));

        Page<ClientProjectView> page = clientProjectViewRepository.findAll(spec, pageRequest);

        return page.getContent().stream()
                .map(clientProjectView -> new ClientProject(clientProjectView.projectName(), clientProjectView.clientName()))
                .toList();
    }

    private Sort buildSort(List<SortCriteria> sortCriteriaList) {
        if (sortCriteriaList == null || sortCriteriaList.isEmpty()) {
            return Sort.by("projectName").ascending();
        }
        return sortCriteriaList.stream()
                .map(sc -> sc.direction() == SortDirection.ASC
                        ? Sort.by(toPropertyPath(sc.field())).ascending()
                        : Sort.by(toPropertyPath(sc.field())).descending())
                .reduce(Sort.unsorted(), Sort::and);
    }

    private String toPropertyPath(SortField field) {
        return switch (field) {
            case PROJECT_NAME -> "projectName";
            case CLIENT_NAME  -> "clientName";
        };
    }
}
