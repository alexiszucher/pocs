import type { ColDef } from 'ag-grid-community';
import {signal} from '@angular/core';
import UserProjectFinder from '../application/UserProjectFinder';

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
  { field: 'nom' },
  { field: 'projet' },
];

export class UserProjectGridPm {

  private _isCheckboxVisible = false;
  columns = signal<ColDef[]>([]);
  rowData = signal<UserProjectRow[]>([]);

  constructor(userProjectFinder: UserProjectFinder) {
    this.columns.set(COLUMNS);
    userProjectFinder.findUserProjects().subscribe(userProjects => {
      const rows = userProjects.map(userProject => ({nom: userProject.nom, projet: userProject.projet} as UserProjectRow));
      this.rowData.set(rows);
      console.log(this.rowData());
    });
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
}
