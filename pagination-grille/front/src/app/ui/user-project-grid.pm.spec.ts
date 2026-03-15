import { UserProjectGridPm } from './user-project-grid.pm';
import UserProjectFinder, { UserProject } from '../application/UserProjectFinder';
import {firstValueFrom, Observable, of} from 'rxjs';

describe('UserProjectGridPm', () => {
  it('devrait exposer les colonnes de la grille user project', () => {
    const pm = new UserProjectGridPm(new FakeUserProjectFinder());
    const fields = pm.columns().map(col => col.field);
    expect(fields).toEqual(["checkbox", 'nom', 'projet']);
  });

  it('devrait avoir un filtre texte sur la colonne nom', () => {
    const pm = new UserProjectGridPm(new FakeUserProjectFinder());
    const nomCol = pm.columns().find(col => col.field === 'nom');
    expect(nomCol?.filter).toBe('agTextColumnFilter');
    expect(nomCol?.filterParams).toStrictEqual({
      filterOptions: ['contains'],
      maxNumConditions: 1,
    });
  });

  it('devrait exposer les données des utilisateurs avec projet', async () => {
    const userProjectFinder = new FakeUserProjectFinder();
    userProjectFinder.userProjects = [
      {nom: 'John Doe', projet: 'Project A' },
      {nom: 'Jane Smith', projet: 'Project B' },
    ];
    const pm = new UserProjectGridPm(userProjectFinder);
    const rowData = await firstValueFrom(pm.find(1, 100, {}));
    expect(rowData.length).toBe(2);
    expect(rowData.some(row => row.nom ==='John Doe' && row.projet === 'Project A')).toBe(true);
    expect(rowData.some(row => row.nom ==='Jane Smith' && row.projet === 'Project B')).toBe(true);
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

  describe('Pagination server-side', () => {
    it('devrait avoir le modèle de grille à infinite', () => {
      const pm = new UserProjectGridPm(new FakeUserProjectFinder());
      expect(pm.gridOptions.rowModelType).toBe('infinite');
    });

    it('devrait avoir la pagination activée', () => {
      const pm = new UserProjectGridPm(new FakeUserProjectFinder());
      expect(pm.gridOptions.pagination).toBe(true);
    });

    it('devrait avoir une pageSize par défaut de 3', () => {
      const pm = new UserProjectGridPm(new FakeUserProjectFinder());
      expect(pm.gridOptions.paginationPageSize).toBe(3);
    });

    it('devrait calculer page et pageSize depuis startRow et endRow', () => {
      const pm = new UserProjectGridPm(new FakeUserProjectFinder());
      expect(pm.pageRequestFromRange(0, 20)).toEqual({ page: 1, pageSize: 20 });
      expect(pm.pageRequestFromRange(40, 60)).toEqual({ page: 3, pageSize: 20 });
    });
  });

  describe('Filtres', () => {

    it('devrait retourner un filtre vide si aucun filtre appliqué', () => {
      const pm = new UserProjectGridPm(new FakeUserProjectFinder());
      expect(pm.filtersFromModel({})).toEqual({});
    });

    it('devrait extraire la valeur contains du filterModel AG Grid', () => {
      const pm = new UserProjectGridPm(new FakeUserProjectFinder());
      expect(pm.filtersFromModel({
        nom: { filterType: 'text', type: 'contains', filter: 'John' }
      })).toEqual({ nom: 'John' });
    });

    it('devrait ignorer les filtres sans valeur', () => {
      const pm = new UserProjectGridPm(new FakeUserProjectFinder());
      expect(pm.filtersFromModel({
        nom: { filterType: 'text', type: 'contains', filter: null }
      })).toEqual({});
    });

    it('devrait gérer plusieurs filtres simultanés', () => {
      const pm = new UserProjectGridPm(new FakeUserProjectFinder());
      expect(pm.filtersFromModel({
        nom: { filterType: 'text', type: 'contains', filter: 'John' },
        projet: { filterType: 'text', type: 'contains', filter: 'Alpha' },
      })).toEqual({ nom: 'John', projet: 'Alpha' });
    });
  });
});

class FakeUserProjectFinder implements UserProjectFinder {
    userProjects: UserProject[] = [];

    findUserProjects(page: number, pageSize: number): Observable<UserProject[]> {
        return of(this.userProjects);
    }
}
