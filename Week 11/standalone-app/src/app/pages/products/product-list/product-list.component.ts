import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../services/product.service';
import { AgGridAngular, AgGridModule } from 'ag-grid-angular';
import { ColDef, GridSizeChangedEvent, FirstDataRenderedEvent, GridOptions, GridApi } from 'ag-grid-community';
import { Router } from '@angular/router';
import { DateFormatPipe } from '../../../core/pipes/date-format.pipe';
import { PriceFormatPipe } from '../../../core/pipes/price-format.pipe';
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
    PriceFormatPipe,
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
  styleUrls: ['./product-list.component.css'],
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  colDefs: ColDef[] = [
    { field: 'name', headerClass: 'text-center', minWidth: 200 },
    { 
      field: 'price', 
      sortable: true, 
      filter: "agNumberColumnFilter", 
      headerClass: 'text-center',
      minWidth: 200,
      valueFormatter: (params: any) => new PriceFormatPipe().transform(params.value)
    },
    {
      field: 'status',
      headerClass: 'text-center',
      cellClass: 'text-center',
      minWidth: 200,
      cellRenderer: StatusCellRendererComponent
    },
    { field: 'createdAt', sortable: true, filter: "agDateColumnFilter", headerClass: 'text-center', minWidth: 200, valueFormatter: (params: any) => new DateFormatPipe().transform(params.value) },
    { field: 'updatedAt', sortable: true, filter: "agDateColumnFilter", headerClass: 'text-center', minWidth: 200, valueFormatter: (params: any) => new DateFormatPipe().transform(params.value) },
    {
      headerName: 'Actions',
      cellRenderer: ActionCellRendererComponent,
      headerClass: 'text-center',
      minWidth: 225,
      cellClass: 'text-center',
    },
  ];

  public defaultColDef: ColDef = {
    filter: "agTextColumnFilter",
    floatingFilter: true,
    resizable: true,
  };

  public gridOptions: GridOptions = {
    getRowStyle: (params) => {
      if (!params.data.status) {
        return { backgroundColor: '#f5f5f5', color: '#aaa' }; // Dark background for entire row when inactive
      }
      return undefined;
    }
  };

  private gridApi!: GridApi;

  constructor(private productService: ProductService, private router: Router) {}

  ngOnInit() {
    this.loadProducts();
    window.addEventListener('resize', this.adjustGridForScreenSize.bind(this)); // Listen for resize events
  }

  onGridReady(params: any) {
    this.gridApi = params.api;
    this.adjustGridForScreenSize(); // Initial check
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
    params.api.sizeColumnsToFit(); // Ensure columns fit the grid width
  }

  onFirstDataRendered(params: FirstDataRenderedEvent) {
    params.api.sizeColumnsToFit(); // Fit columns on initial render
  }

  adjustGridForScreenSize() {
    if (this.gridApi) {
      this.gridApi.sizeColumnsToFit(); // Adjust columns for screen size
    }
  }
}