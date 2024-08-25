import { Component } from '@angular/core';
import { ICellRendererAngularComp } from 'ag-grid-angular';
import { ICellRendererParams } from 'ag-grid-community';

@Component({
  selector: 'app-action-cell-renderer',
  standalone: true,
  template: `
    <button 
      class="bg-blue-500 text-white text-xs px-4 py-1.5 rounded shadow hover:bg-blue-600 mr-2 disabled:bg-blue-300 disabled:cursor-not-allowed"
      (click)="onEditClick()" 
      [disabled]="!params.data.status">
      <i class="fas fa-pencil-alt"></i>&nbsp; Update
    </button>

    <button 
      class="bg-red-500 text-white text-xs px-4 py-1.5 rounded shadow hover:bg-red-600 disabled:bg-red-300 disabled:cursor-not-allowed"
      (click)="onDeleteClick()" 
      [disabled]="!params.data.status">
      <i class="fas fa-trash"></i>&nbsp; Delete
    </button>
  `
})
export class ActionCellRendererComponent implements ICellRendererAngularComp {
    params!: ICellRendererParams;
  
    agInit(params: ICellRendererParams): void {
      this.params = params;
    }

    refresh(params: ICellRendererParams) {
        return true;
    }
  
    onEditClick() {
        this.params.context.componentParent.onEditProduct(this.params.data);
    }
  
    onDeleteClick() {
        this.params.context.componentParent.onDeleteProduct(this.params.data);
    }
}