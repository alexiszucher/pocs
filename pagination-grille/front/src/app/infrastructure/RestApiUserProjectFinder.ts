import UserProjectFinder, {UserProject, UserProjectFilters} from '../application/UserProjectFinder';
import {from, Observable} from 'rxjs';

export class RestApiUserProjectFinder implements UserProjectFinder {
  findUserProjects(page: number, pageSize: number, filters: UserProjectFilters): Observable<UserProject[]> {
    console.log('findUserProjects', page, pageSize, filters);
    const params = new URLSearchParams({
      page: String(page),
      pageSize: String(pageSize),
      ...Object.fromEntries(
        Object.entries(filters).filter(([_, filter]) => filter !== undefined)
      ),
    });

    return from(
      fetch(`http://localhost:8080/client-projects?${params}`)
        .then(async response => {
          const json = await response.json();
          return (json as UserProjectHttpResponse[]).map(r => ({
            nom: r.clientName,
            projet: r.projectName,
          }));
        })
    );
  }
}

interface UserProjectHttpResponse {
  projectName: string;
  clientName: string;
}
