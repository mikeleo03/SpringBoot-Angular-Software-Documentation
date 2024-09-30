import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, switchMap } from 'rxjs';
import { Product } from '../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private apiUrl = 'http://localhost:8080/products';

  constructor(private http: HttpClient) { }

  // Method to get all products and determine the last ID
  getLastProductId(): Observable<number> {
    return new Observable<number>(observer => {
      this.http.get<any[]>(this.apiUrl).subscribe(products => {
        const lastId = products.reduce((maxId, product) => Math.max(maxId, product.id), 0);
        observer.next(lastId);
        observer.complete();
      });
    });
  }
  
  // Get all products
  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  // Get a single product by ID
  getProductById(id: string): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`);
  }

  // Add a new product
  addProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, product);
  }

  // Update an existing product
  updateProduct(product: Product): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${product.id}`, product);
  }

  // Delete a product
  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Toggle product status (active/inactive)
  updateProductStatus(id: string, status: boolean): Observable<Product> {
    return this.getProductById(id).pipe(
      switchMap(product => {
        const updatedProduct: Product = { ...product, status: status as boolean };  
        return this.updateProduct(updatedProduct);
      })
    );
  }
}