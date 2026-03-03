package com.azucher.paginationgrille.back.application;

import java.util.List;

public record ClientProjectsQuery(int page,
                                  int pageSize,
                                  String clientName,
                                  String projectStatus,
                                  String clientNameStartsWith,
                                  List<SortCriteria> sortCriteriaList) {
}
