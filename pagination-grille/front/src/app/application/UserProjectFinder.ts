import {Observable} from 'rxjs';

export default interface UserProjectFinder {
  findUserProjects(page: number, pageSize: number): Observable<UserProject[]>;
}

export type UserProject = { nom: string, projet: string };
