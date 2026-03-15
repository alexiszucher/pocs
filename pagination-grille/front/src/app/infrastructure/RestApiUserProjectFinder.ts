import UserProjectFinder, {UserProject} from '../application/UserProjectFinder';
import {from, Observable} from 'rxjs';

export class RestApiUserProjectFinder implements UserProjectFinder {
  findUserProjects(page: number, pageSize: number): Observable<UserProject[]> {
    console.log(`Fetching user projects for page ${page} with pageSize ${pageSize}`);
    return from(
      fetch(`http://localhost:8080/client-projects?page=${page}&pageSize=${pageSize}`)
        .then(async response => {
          const json = await response.json();
          return (json as UserProjectHttpResponse[])
            .map(userProjectHttpResponse => ({
              nom: userProjectHttpResponse.clientName,
              projet: userProjectHttpResponse.projectName,
            } as UserProject));
        })
    );
  }
}

interface UserProjectHttpResponse {
  projectName: string;
  clientName: string;
}
