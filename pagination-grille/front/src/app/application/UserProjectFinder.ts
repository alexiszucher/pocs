import {Observable} from 'rxjs';

export default interface UserProjectFinder {
  findUserProjects(page: number, pageSize: number, filters: UserProjectFilters): Observable<UserProject[]>;
}

export type UserProject = { nom: string, projet: string };

export interface UserProjectFilters {
  [field: string]: string | undefined;
}
