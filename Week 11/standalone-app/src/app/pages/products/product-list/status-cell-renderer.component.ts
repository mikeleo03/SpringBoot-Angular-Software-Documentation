import { Component } from '@angular/core';
import { ICellRendererParams } from 'ag-grid-community';
import { HlmSwitchComponent } from '@spartan-ng/ui-switch-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-status-cell-renderer',
  standalone: true,
  imports: [HlmLabelDirective, HlmSwitchComponent, CommonModule],
  template: `
    <label class="flex items-center w-full justify-center mt-1">
        <hlm-switch class="mr-2 mt-1" [checked]="params.value" (change)="onStatusToggle()" />
        <span class="text-sm font-semibold mt-1" [ngClass]="{'text-green-500': params.value, 'text-red-500': !params.value}">
            {{ params.value ? 'Active' : 'Inactive' }}
        </span>
    </label>
  `
})
export class StatusCellRendererComponent {
  params!: ICellRendererParams;

  agInit(params: ICellRendererParams): void {
    this.params = params;
  }

  onStatusToggle() {
    const updatedProduct = this.params.data;
    updatedProduct.status = !updatedProduct.status;
    this.params.context.componentParent.onStatusToggle(updatedProduct);
  }
}