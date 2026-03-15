import { UserProjectGridPm } from './user-project-grid.pm';
import UserProjectFinder, { UserProject } from '../application/UserProjectFinder';
import {Observable, of} from 'rxjs';

describe('UserProjectGridPm', () => {
  it('devrait exposer les colonnes de la grille user project', () => {
    const pm = new UserProjectGridPm(new FakeUserProjectFinder());
    const fields = pm.columns().map(col => col.field);
    expect(fields).toEqual(["checkbox", 'nom', 'projet']);
  });

  it('devrait exposer les données des utilisateurs avec projet', () => {
    const userProjectFinder = new FakeUserProjectFinder();
    userProjectFinder.userProjects = [
      {nom: 'John Doe', projet: 'Project A' },
      {nom: 'Jane Smith', projet: 'Project B' },
    ];
    const pm = new UserProjectGridPm(userProjectFinder);
    expect(pm.rowData().length).toBe(2);
    expect(pm.rowData().some(row => row.nom ==='John Doe' && row.projet === 'Project A')).toBe(true);
    expect(pm.rowData().some(row => row.nom ==='Jane Smith' && row.projet === 'Project B')).toBe(true);
  });

  describe('Colonne checkbox', () => {
    it('devrait avoir la colonne checkbox cachée par défaut', () => {
      const pm = new UserProjectGridPm(new FakeUserProjectFinder());
      const checkboxCol = pm.columns().find(col => col.field === 'checkbox');
      expect(checkboxCol?.hide).toBe(true);
    });

    it('devrait afficher la colonne checkbox après toggle', () => {
      const pm = new UserProjectGridPm(new FakeUserProjectFinder());
      pm.toggleCheckboxColumn();
      const checkboxCol = pm.columns().find(col => col.field === 'checkbox');
      expect(checkboxCol?.hide).toBe(false);
    });

    it('devrait cacher la colonne checkbox après deux toggles', () => {
      const pm = new UserProjectGridPm(new FakeUserProjectFinder());
      pm.toggleCheckboxColumn();
      pm.toggleCheckboxColumn();
      const checkboxCol = pm.columns().find(col => col.field === 'checkbox');
      expect(checkboxCol?.hide).toBe(true);
    });
  });
});

class FakeUserProjectFinder implements UserProjectFinder {
    userProjects: UserProject[] = [];

    findUserProjects(): Observable<UserProject[]> {
        return of(this.userProjects);
    }
}
