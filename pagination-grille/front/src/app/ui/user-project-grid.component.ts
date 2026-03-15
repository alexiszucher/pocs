import { Component } from '@angular/core';
import { AgGridAngular } from 'ag-grid-angular';
import {UserProjectGridPm} from './user-project-grid.pm';
import {RestApiUserProjectFinder} from '../infrastructure/RestApiUserProjectFinder';
import type { IDatasource, IGetRowsParams } from 'ag-grid-community';

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
      [datasource]="data"
      [columnDefs]="pm.columns()"
      [gridOptions]="pm.gridOptions"
    />
  `
})
export class UserProjectGridComponent {
  readonly pm = new UserProjectGridPm(new RestApiUserProjectFinder());

  readonly data: IDatasource = {
    getRows: (params: IGetRowsParams) => {
      const { page, pageSize } = this.pm.pageRequestFromRange(params.startRow, params.endRow);
      const filters = this.pm.filtersFromModel(params.filterModel);

      this.pm.find(page, pageSize, filters).subscribe({
        next: (result) => params.successCallback(result, 50),
        error: () => params.failCallback(),
      });
    },
  };
}
