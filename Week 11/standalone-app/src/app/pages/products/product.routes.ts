import { Routes } from '@angular/router';
import { ProductListComponent } from './product-list/product-list.component';
import { ProductFormComponent } from './product-form/product-form.component';

export const productRoutes: Routes = [
    { path: '', component: ProductListComponent }, // List products
    { path: 'add', component: ProductFormComponent }, // Add new product
    { path: 'edit/:id', component: ProductFormComponent }, // Edit existing product
];