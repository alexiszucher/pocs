import {Observable} from 'rxjs';

export default interface UserProjectFinder {
  findUserProjects(): Observable<UserProject[]>;
}

export type UserProject = { nom: string, projet: string };
