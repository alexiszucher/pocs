import { Component } from '@angular/core';
import { AgGridAngular } from 'ag-grid-angular';
import {UserProjectGridPm} from './user-project-grid.pm';
import {RestApiUserProjectFinder} from '../infrastructure/RestApiUserProjectFinder';

@Component({
  selector: 'user-project-grid',
  standalone: true,
  imports: [AgGridAngular],
  template: `
    <button (click)="pm.toggleCheckboxColumn()">
      {{ pm.isCheckboxVisible ? 'Masquer' : 'Afficher' }} les checkboxes
    </button>

    <ag-grid-angular
      style="width: 100%; height: 500px;"
      [rowData]="pm.rowData()"
      [columnDefs]="pm.columns()"
    />
  `
})
export class UserProjectGridComponent {
  readonly pm = new UserProjectGridPm(new RestApiUserProjectFinder());
}
