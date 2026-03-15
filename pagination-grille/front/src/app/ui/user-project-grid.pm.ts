import type { ColDef, GridOptions } from 'ag-grid-community';
import {signal} from '@angular/core';
import UserProjectFinder, {UserProjectFilters} from '../application/UserProjectFinder';
import {map, Observable} from 'rxjs';

export interface UserProjectRow {
  nom: string;
  projet: string;
}

const COLUMNS: ColDef[] = [
  {
    field: 'checkbox',
    headerCheckboxSelection: true,
    checkboxSelection: true,
    width: 50,
    hide: true,
  },
  {
    field: 'nom',
    filter: 'agTextColumnFilter',
    filterParams: {
      filterOptions: ['contains'],
      maxNumConditions: 1,
    },
  },
  { field: 'projet' },
];

export class UserProjectGridPm {

  private _isCheckboxVisible = false;
  columns = signal<ColDef[]>([]);
  readonly gridOptions: GridOptions = {
    rowModelType: 'infinite',
    pagination: true,
    paginationPageSize: 3,
    cacheBlockSize: 3,
  };

  constructor(readonly userProjectFinder: UserProjectFinder) {
    this.columns.set(COLUMNS);
  }

  get isCheckboxVisible(): boolean {
    return this._isCheckboxVisible;
  }

  toggleCheckboxColumn(): void {
    this._isCheckboxVisible = !this._isCheckboxVisible;
    this.columns.update(cols =>
      cols.map(col =>
        col.field === 'checkbox'
          ? { ...col, hide: !this._isCheckboxVisible }
          : col
      )
    );
  }

  pageRequestFromRange(startRow: number, endRow: number): { page: number; pageSize: number } {
    const pageSize = endRow - startRow;
    return { page: startRow / pageSize + 1, pageSize };
  }

  find(page: number, pageSize: number, filters: UserProjectFilters): Observable<UserProjectRow[]> {
    return this.userProjectFinder.findUserProjects(page, pageSize, filters).pipe(
      map(userProjects => userProjects.map(userProject => ({
        nom: userProject.nom,
        projet: userProject.projet,
      } as UserProjectRow)))
    );
  }

  filtersFromModel(filterModel: Record<string, any>): UserProjectFilters {
    return Object.entries(filterModel)
      .filter(([_, colFilter]) => colFilter.filter != null)
      .reduce((userProjectFilters, [field, colFilter]) => ({
        ...userProjectFilters,
        [field]: colFilter.filter,
      }), {} as UserProjectFilters);
  }
}
