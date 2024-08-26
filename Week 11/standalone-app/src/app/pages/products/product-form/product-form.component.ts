import { Component, EventEmitter, Output, Input, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { ProductService } from '../../../services/product.service';
import { CommonModule } from '@angular/common';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';

@Component({
  selector: 'app-product-form',
  standalone: true,
  templateUrl: './product-form.component.html',
  styleUrls: [],
  imports: [
    ReactiveFormsModule, 
    CommonModule,
    HlmButtonDirective,
    HlmInputDirective,
    HlmIconComponent,
    HlmLabelDirective,
  ],
})
export class ProductFormComponent implements OnInit {
  @Input() product: any;  // Pass the product data for editing
  @Input() isEditMode = false;  // Flag to differentiate between add and edit mode
  @Output() productSaved = new EventEmitter<any>();
  @Output() formClosed = new EventEmitter<void>();

  productForm!: FormGroup;
  lastProductId!: number;

  constructor(private fb: FormBuilder, private productService: ProductService) {}

  ngOnInit() {
    this.productForm = this.fb.group({
      id: [''],
      name: [this.product?.name || '', Validators.required],
      price: [this.product?.price || '', [Validators.required, Validators.min(0)]],
      status: [this.product?.status !== undefined ? this.product.status : true],
      createdAt: [this.product?.createdAt || new Date()],
      updatedAt: [new Date()],
    });

    if (!this.isEditMode) {
      // Fetch the last product ID if creating a new product
      this.productService.getLastProductId().subscribe((lastId: number) => {
        this.lastProductId = lastId;
        this.productForm.patchValue({ id: (this.lastProductId + 1).toString() });
      });
    } else {
      this.productForm.patchValue(this.product); // Pre-fill the form with existing product data
    }
  }

  onSubmit() {
    const productData = this.productForm.value;
    if (this.isEditMode) {
      this.productService.updateProduct(productData).subscribe(() => {
        this.productSaved.emit(productData);
        this.formClosed.emit(); // Close sheet
      });
    } else {
      // Add new product
      this.productService.addProduct(productData).subscribe(() => {
        this.productSaved.emit(productData);
        this.formClosed.emit(); // Close sheet
      });
    }
  }

  get name() {
    return this.productForm.get('name');
  }

  get price() {
    return this.productForm.get('price');
  }
}