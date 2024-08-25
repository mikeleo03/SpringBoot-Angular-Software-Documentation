import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../services/product.service';
import { AgGridAngular } from 'ag-grid-angular';
import { ColDef, GridSizeChangedEvent, FirstDataRenderedEvent } from 'ag-grid-community';
import { Router } from '@angular/router';
import { DateFormatPipe } from '../../../core/pipes/date-format.pipe';
import { ActionCellRendererComponent } from './action-cell-renderer.component';
import { StatusCellRendererComponent } from './status-cell-renderer.component';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [AgGridAngular, DateFormatPipe, ActionCellRendererComponent, StatusCellRendererComponent],
  templateUrl: './product-list.component.html',
  styleUrls: [],
})
export class ProductListComponent implements OnInit {
  products: any[] = [];
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
    this.productService.getProducts().subscribe((data) => (this.products = data));
  }

  onAddProduct() {
    this.loadProducts();
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

  onEditProduct(product: any) {
    this.router.navigate(['/products/edit', product.id]);
  }

  onDeleteProduct(product: any) {
    if (confirm(`Are you sure you want to delete ${product.name}?`)) {
      this.productService.deleteProduct(product.id).subscribe(() => {
        this.loadProducts();
      });
    }
  }

  onGridSizeChanged(params: GridSizeChangedEvent) {
    // get the current grids width
    let gridWidth = document.querySelector(".ag-body-viewport")!.clientWidth;
    // keep track of which columns to hide/show
    let columnsToShow = [];
    let columnsToHide = [];
    // iterate over all columns (visible or not) and work out
    // now many columns can fit (based on their minWidth)
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
    // show/hide columns based on current grid width
    params.api.setColumnsVisible(columnsToShow, true);
    params.api.setColumnsVisible(columnsToHide, false);
    // wait until columns stopped moving and fill out
    // any available space to ensure there are no gaps
    window.setTimeout(() => {
      params.api.sizeColumnsToFit();
    }, 10);
  }

  onFirstDataRendered(params: FirstDataRenderedEvent) {
    params.api.sizeColumnsToFit();
  }
}