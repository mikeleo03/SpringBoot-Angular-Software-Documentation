import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../services/product.service';
import { AgGridAngular, AgGridModule } from 'ag-grid-angular';
import { ColDef, GridSizeChangedEvent, FirstDataRenderedEvent } from 'ag-grid-community';
import { Router } from '@angular/router';
import { DateFormatPipe } from '../../../core/pipes/date-format.pipe';
import { HlmSheetComponent, HlmSheetContentComponent, HlmSheetHeaderComponent, HlmSheetFooterComponent, HlmSheetTitleDirective, HlmSheetDescriptionDirective } from '@spartan-ng/ui-sheet-helm';
import { BrnSheetContentDirective, BrnSheetTriggerDirective } from '@spartan-ng/ui-sheet-brain';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { ProductFormComponent } from '../product-form/product-form.component';
import { StatusCellRendererComponent } from './status-cell-renderer.component';
import { ActionCellRendererComponent } from './action-cell-renderer.component';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { Product } from '../../../models/product.model';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    AgGridAngular, 
    DateFormatPipe, 
    CommonModule,
    ReactiveFormsModule,
    AgGridModule,
    ProductFormComponent,

    BrnSheetTriggerDirective,
    BrnSheetContentDirective,
    HlmSheetComponent,
    HlmSheetContentComponent,
    HlmSheetHeaderComponent,
    HlmSheetFooterComponent,
    HlmSheetTitleDirective,
    HlmSheetDescriptionDirective,
    HlmLabelDirective,

    ActionCellRendererComponent,
    StatusCellRendererComponent
  ],
  templateUrl: './product-list.component.html',
  styleUrls: [],
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  colDefs: ColDef[] = [
    { field: 'name', headerClass: 'text-center', cellClass: 'text-center' },
    { field: 'price', sortable: true, filter: "agNumberColumnFilter", headerClass: 'text-center', cellClass: 'text-center' },
    {
      field: 'status',
      headerClass: 'text-center',
      cellClass: 'text-center',
      cellRenderer: StatusCellRendererComponent,
      cellStyle: (params) => {
        return params.value
          ? null
          : { backgroundColor: '#f5f5f5', color: '#aaa' };
      }
    },
    { field: 'createdAt', sortable: true, filter: "agDateColumnFilter", headerClass: 'text-center', cellClass: 'text-center', valueFormatter: (params: any) => new DateFormatPipe().transform(params.value) },
    { field: 'updatedAt', sortable: true, filter: "agDateColumnFilter", headerClass: 'text-center', cellClass: 'text-center', valueFormatter: (params: any) => new DateFormatPipe().transform(params.value) },
    {
      headerName: 'Actions',
      cellRenderer: ActionCellRendererComponent,
      headerClass: 'text-center',
      cellClass: 'text-center',
    },
  ];

  public defaultColDef: ColDef = {
    filter: "agTextColumnFilter",
    floatingFilter: true,
  };

  constructor(private productService: ProductService, private router: Router) {}

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts() {
    this.productService.getProducts().subscribe((products) => {
      this.products = products;
    });
  }

  onAddProduct(product: any) {
    this.loadProducts(); // Reload products after adding
  }

  onProductEdited(product: any) {
    this.loadProducts(); // Reload products after edited
  }

  onProductToggle(event: any) {
    const updatedProduct = event.data;
    this.productService.updateProduct(updatedProduct).subscribe(() => {
      this.loadProducts();
    });
  }

  onStatusToggle(product: any) {
    this.productService.updateProductStatus(product.id, product.status).subscribe(() => {
      this.loadProducts();
    });
  }

  onDeleteProduct(product: any) {
    if (confirm(`Are you sure you want to delete ${product.name}?`)) {
      this.productService.deleteProduct(product.id).subscribe(() => {
        this.loadProducts();
      });
    }
  }

  onGridSizeChanged(params: GridSizeChangedEvent) {
    let gridWidth = document.querySelector(".ag-body-viewport")!.clientWidth;
    let columnsToShow = [];
    let columnsToHide = [];
    let totalColsWidth = 0;
    let allColumns = params.api.getColumns();
    if (allColumns && allColumns.length > 0) {
      for (const element of allColumns) {
        let column = element;
        totalColsWidth += column.getMinWidth();
        if (totalColsWidth > gridWidth) {
          columnsToHide.push(column.getColId());
        } else {
          columnsToShow.push(column.getColId());
        }
      }
    }
    params.api.setColumnsVisible(columnsToShow, true);
    params.api.setColumnsVisible(columnsToHide, false);
    window.setTimeout(() => {
      params.api.sizeColumnsToFit();
    }, 10);
  }

  onFirstDataRendered(params: FirstDataRenderedEvent) {
    params.api.sizeColumnsToFit();
  }
}
