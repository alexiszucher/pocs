package com.azucher.paginationgrille.back.infrastructure;

import com.azucher.paginationgrille.back.application.ClientProjectsQuery;
import com.azucher.paginationgrille.back.application.FindClientProjects;
import com.azucher.paginationgrille.back.readmodel.ClientProject;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ClientProjectController {
    private final FindClientProjects findClientProjects;

    public ClientProjectController(FindClientProjects findClientProjects) {
        this.findClientProjects = findClientProjects;
    }

    @GetMapping("/client-projects")
    public List<ClientProject> getAllClientProjects(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
                                                    @RequestParam(required = false) String nom) {
        return findClientProjects.find(new ClientProjectsQuery(page, pageSize, null, null, nom, null));
    }
}
