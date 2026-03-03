package com.azucher.paginationgrille.back;
import com.azucher.paginationgrille.back.application.*;
import com.azucher.paginationgrille.back.readmodel.ClientProject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@Sql(scripts = "classpath:ClientProjectData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class PaginationGrilleTest {
    @Autowired
    private FindClientProjects findClientProjects;

    @Test
    void shouldRetrieveFirstPageOfClientProjects_SortByProjectNameAscByDefault() {
        List<ClientProject> clientProjects = findClientProjects.find(1, 5);
        Assertions.assertEquals(5, clientProjects.size());
        assertClientProjectExist(clientProjects, "ACR", "Joe");
        assertClientProjectExist(clientProjects, "Alpine", "Bastian");
        assertClientProjectExist(clientProjects, "Boreal", "Bastian");
        assertClientProjectExist(clientProjects, "Cobalt", "Robin");
        assertClientProjectExist(clientProjects, "Datarep", "Gérald");
    }

    @Test
    void shouldRetrieveSecondPageOfClientProjects() {
        List<ClientProject> page1 = findClientProjects.find(1, 5);
        List<ClientProject> page2 = findClientProjects.find(2, 5);
        List<ClientProject> first10 = findClientProjects.find(1, 10);

        Assertions.assertEquals(5, page1.size(), "La page 1 doit contenir 5 éléments");
        Assertions.assertEquals(5, page2.size(), "La page 2 doit contenir 5 éléments");
        Assertions.assertEquals(10, first10.size(), "Avec ce dataset de test, on attend 10 éléments");

        assertClientProjectExist(page2, "DeltaWorks", "Robin");
        assertClientProjectExist(page2, "Epsilon", "Bastian");
        assertClientProjectExist(page2, "Moma", "Bastian");
        assertClientProjectExist(page2, "Moma", "Robin");
        assertClientProjectExist(page2, "PF", "Nixon");
    }

    @Test
    void shouldFilterByClientName() {
        List<ClientProject> clientProjects =
                findClientProjects.find(new ClientProjectsQuery(1, 10, "Bastian", null, null, null));

        Assertions.assertEquals(4, clientProjects.size(), "Bastian doit avoir 4 projets dans le dataset");

        Assertions.assertEquals("Alpine", clientProjects.get(0).projectName());
        Assertions.assertEquals("Boreal", clientProjects.get(1).projectName());
        Assertions.assertEquals("Epsilon", clientProjects.get(2).projectName());
        Assertions.assertEquals("Moma", clientProjects.get(3).projectName());

        clientProjects.forEach(cp -> Assertions.assertEquals("Bastian", cp.clientName()));
    }

    @Test
    void shouldFilterByClientNameAndProjectStatus() {
        List<ClientProject> clientProjects =
                findClientProjects.find(new ClientProjectsQuery(1, 10, "Bastian", "ACTIVE", null, null));

        Assertions.assertEquals(3, clientProjects.size(), "Bastian doit avoir 3 projets actifs dans le dataset");

        Assertions.assertEquals("Alpine", clientProjects.get(0).projectName());
        Assertions.assertEquals("Boreal", clientProjects.get(1).projectName());
        Assertions.assertEquals("Epsilon", clientProjects.get(2).projectName());
    }

    @Test
    void shouldFilterByFirstLettersOfClientName() {
        List<ClientProject> clientProjects =
                findClientProjects.find(new ClientProjectsQuery(1, 10, null, null, "Bas", null));

        Assertions.assertEquals(4, clientProjects.size(), "La lettre 'B' doit matcher uniquement Bastian (4 projets)");

        clientProjects.forEach(cp -> Assertions.assertEquals("Bastian", cp.clientName()));

        Assertions.assertEquals("Alpine", clientProjects.get(0).projectName());
        Assertions.assertEquals("Boreal", clientProjects.get(1).projectName());
        Assertions.assertEquals("Epsilon", clientProjects.get(2).projectName());
        Assertions.assertEquals("Moma", clientProjects.get(3).projectName());
    }

    @Test
    void shouldSortByClientName() {
        List<ClientProject> clientProjects =
                findClientProjects.find(new ClientProjectsQuery(1, 10, null, null, null, List.of(new SortCriteria(SortField.CLIENT_NAME, SortDirection.ASC))));

        Assertions.assertEquals(10, clientProjects.size());

        // Bastian en premier (alphabétique)
        Assertions.assertEquals("Bastian",  clientProjects.get(0).clientName());
        Assertions.assertEquals("Bastian",  clientProjects.get(1).clientName());
        Assertions.assertEquals("Bastian", clientProjects.get(2).clientName());
        Assertions.assertEquals("Bastian",    clientProjects.get(3).clientName());

        // Robin en dernier
        Assertions.assertEquals("Robin",      clientProjects.get(7).clientName());
        Assertions.assertEquals("Robin",  clientProjects.get(8).clientName());
        Assertions.assertEquals("Robin", clientProjects.get(9).clientName());
    }

    @Test
    void shouldSortByProjectNameAndClientName() {
        List<ClientProject> clientProjects =
                findClientProjects.find(new ClientProjectsQuery(1, 11, null, null, null,
                        List.of(new SortCriteria(SortField.CLIENT_NAME, SortDirection.ASC), new SortCriteria(SortField.PROJECT_NAME, SortDirection.ASC))));

        Assertions.assertEquals(11, clientProjects.size());

        // Bastian en premier et projet par ordre alphabétique
        Assertions.assertEquals("Alpine",  clientProjects.get(0).projectName());
        Assertions.assertEquals("Boreal",  clientProjects.get(1).projectName());
        Assertions.assertEquals("Epsilon", clientProjects.get(2).projectName());
        Assertions.assertEquals("Moma",    clientProjects.get(3).projectName());
        clientProjects.subList(0, 4).forEach(cp ->
                Assertions.assertEquals("Bastian", cp.clientName()));

        // Robin en dernier et projet par ordre alphabétique
        Assertions.assertEquals("Cobalt",      clientProjects.get(7).projectName());
        Assertions.assertEquals("DeltaWorks",  clientProjects.get(8).projectName());
        Assertions.assertEquals("Moma", clientProjects.get(9).projectName());
        clientProjects.subList(7, 10).forEach(cp ->
                Assertions.assertEquals("Robin", cp.clientName()));
    }

    private void assertClientProjectExist(List<ClientProject> clientProjects, String projectName, String clientName) {
        clientProjects.stream().filter(cp -> cp.projectName().equals(projectName) && cp.clientName().equals(clientName)).findFirst().orElseThrow();
    }
}
