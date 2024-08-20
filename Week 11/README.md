# 👩🏻‍🏫 Lecture 19 - Basic Angular 
> This repository is created as a part of assignment for Lecture 19 - Basic Angular

## 🔄 Assignment 01 - Angular LifeCycle

Angular components have a well-defined lifecycle, from creation to destruction. Understanding these lifecycle hooks is crucial for controlling how the component behaves at different stages, managing resources efficiently, and ensuring optimal performance. Below is a deep dive into the key lifecycle hooks and how they are used:

### 👉 **Lifecycle Sequence**

<div align=center>
    <img src="/Week%2011/img/lifecycle.png" width="600">
    <br>
      <b>Fig 1.</b> Angular LifeCycle.
    <br>
    <br>
</div>

The lifecycle hooks are called in the following order:

1. `ngOnChanges()` - whenever an input property changes.
2. `ngOnInit()` - after the first `ngOnChanges()`.
3. `ngDoCheck()` - during every change detection run.
4. `ngAfterContentInit()` - after content is projected into the component.
5. `ngAfterContentChecked()` - after every check of the projected content.
6. `ngAfterViewInit()` - after the component's view has been initialized.
7. `ngAfterViewChecked()` - after every check of the component's view.
8. `ngOnDestroy()` - before the component is destroyed.

### ✅ **Use Cases and Best Practices**

- **ngOnChanges**: Use this to react to changes in input properties, such as fetching new data when a filter value changes.
- **ngOnInit**: Ideal for initializing data, starting services, and making the first call to an API. Most initialization logic should go here.
- **ngDoCheck**: Rarely needed, but useful for detecting changes in deep or complex data structures that Angular's default change detection might miss.
- **ngAfterContentInit / ngAfterContentChecked**: Useful when we need to interact with or manipulate the content projected into the component.
- **ngAfterViewInit / ngAfterViewChecked**: Commonly used for DOM manipulation or interacting with child components.
- **ngOnDestroy**: Crucial for cleanup activities, such as unsubscribing from observables or detaching event listeners.

### 💻 **Practical Example**

Let's consider a component that fetches data from a service and displays it, with proper resource management and lifecycle hooks:

```typescript
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { DataService } from './data.service';

@Component({
    selector: 'app-data-viewer',
    template: `<div *ngIf="data">{{ data }}</div>`,
})
export class DataViewerComponent implements OnInit, OnDestroy {
    data: string;
    private dataSubscription: Subscription;

    constructor(private dataService: DataService) {}

    ngOnInit() {
        this.dataSubscription = this.dataService.getData().subscribe((result) => {
            this.data = result;
        });
    }

    ngOnDestroy() {
        if (this.dataSubscription) {
            this.dataSubscription.unsubscribe();
            console.log('Unsubscribed from dataService');
        }
    }
}
```

In this example:
- `ngOnInit` is used to subscribe to a data service when the component initializes.
- `ngOnDestroy` ensures that the subscription is properly cleaned up, preventing memory leaks.

---

## 🤔 Assignment 02 - Creating and Comparing Standalone and Non-Standalone Angular Projects

In Angular, components can be configured in two different ways: as **standalone** components or as **non-standalone** components (traditional, module-based). Standalone components are a newer feature introduced in Angular 14 that allows a component to be self-contained without needing to be declared in a module, making it more modular and easier to reuse.

### 💡 **Step-by-Step Guide to Create Two Angular Projects: Standalone and Non-Standalone**

Let's go through the process of creating two Angular projects: one with standalone components and another with traditional (non-standalone) components.

#### **0. Initial Setup**

First, ensure to have Angular CLI installed:

```bash
$ npm install -g @angular/cli
```

#### **1. Create the Non-Standalone Angular Project**

**Step 1: Create the Angular Project**
1. Open a terminal or command prompt.
2. Run the following command to create a new Angular project:
   ```bash
   $ ng new non-standalone-app --no-standalone --routing --ssr=false
   ```
3. Angular CLI will ask a few configuration questions:
   - **Would you like to add Angular routing?** Type `Y` (Yes).
   - **Which stylesheet format would you like to use?** Choose your preferred style (CSS, SCSS, etc.).

**Step 2: Create a Component, Service, and Model**
1. Navigate to the project directory:
   ```bash
   $ cd non-standalone-app
   ```
2. Generate a new component:
   ```bash
   $ ng generate component components/login
   ```
3. Generate a new service:
   ```bash
   $ ng generate service services/auth
   ```

   and create the service for doing login

   ```typescript
    import { Injectable } from '@angular/core';
    import { HttpClient } from '@angular/common/http';
    import { Observable } from 'rxjs';
    import { User } from '../models/user.model';

    @Injectable({
    providedIn: 'root'
    })
    export class AuthService {

    private apiUrl = 'https://example.com/api/login';  // Replace with actual API URL

    constructor(private http: HttpClient) { }

    login(user: User): Observable<any> {
        // Send a POST request to the server with the user's credentials
        return this.http.post<any>(this.apiUrl, user);
    }
    }
   ```
4. Create a new model file (e.g., `user.model.ts`) inside the `src/app/models` directory:
    ```typescript
    // src/app/models/user.model.ts
    export interface User {
        id: number;
        username: string;
        password: string;
    }
    ```

**Step 3: Use the Service in the Component**
1. Inject the service into the component:
    ```typescript
    // src/app/components/login/login.component.ts
    import { Component } from '@angular/core';
    import { AuthService } from '../../services/auth.service';
    import { User } from '../../models/user.model';

    @Component({
        selector: 'app-login',
        templateUrl: './login.component.html',
        styleUrls: ['./login.component.css']
    })
    export class LoginComponent {
        user: User = { id: 0, username: '', password: '' };

        constructor(private authService: AuthService) {}

        login() {
            this.authService.login(this.user).subscribe(response => {
                console.log('Login successful', response);
            });
        }
    }
    ```

**Step 4: Declaring the Component in a Module**
1. In the traditional approach, we must declare the component in the module:
    ```typescript
    // src/app/app.module.ts
    import { NgModule } from '@angular/core';
    import { BrowserModule } from '@angular/platform-browser';
    import { AppRoutingModule } from './app-routing.module';
    import { AppComponent } from './app.component';
    import { LoginComponent } from './components/login/login.component';
    import { AuthService } from './services/auth.service';

    @NgModule({
        declarations: [
            AppComponent,
            LoginComponent // Declare the component here
        ],
        imports: [
            BrowserModule,
            AppRoutingModule
        ],
        providers: [AuthService],
        bootstrap: [AppComponent]
    })
    export class AppModule { }
    ```

**Step 5: Run the application**
Run the application in port 3000 by typing this command.

```bash
$ ng serve --port=3000
```

and here is the result.

<div align=center>
    <img src="/Week%2011/img/demo-non.png" width="800">
    <br>
      <b>Fig 2.</b> Non-Standalone Demo Result.
    <br>
    <br>
</div>

#### **2. Create the Standalone Angular Project**

**Step 1: Create the Angular Project**
1. Open another terminal or command prompt.
2. Run the following command to create a new Angular project:
   ```bash
   $ ng new standalone-app --ssr=false
   ```
3. As with the previous project, Angular CLI will ask some configuration questions:
   - **Would you like to add Angular routing?** Type `Y` (Yes).
   - **Which stylesheet format would you like to use?** Choose your preferred style.

**Step 2: Create a Standalone Component**
1. Navigate to the project directory:
   ```bash
   $ cd standalone-app
   ```
2. Generate a new standalone component:
   ```bash
   $ ng generate component components/login --standalone
   ```
   The `--standalone` flag indicates that the component should be created as a standalone component.

**Step 3: Create a Service and Model**
1. Generate a new service:
   ```bash
   $ ng generate service services/auth
   ```
   use the same service content with the non-standalone one.
2. Create the model as before in the `src/app/models` directory:
    ```typescript
    // src/app/models/user.model.ts
    export interface User {
        id: number;
        username: string;
        password: string;
    }
    ```

**Step 4: Use the Service in the Standalone Component**
1. Inject the service into the standalone component:
    ```typescript
    // src/app/components/login/login.component.ts
    import { Component } from '@angular/core';
    import { AuthService } from '../../services/auth.service';
    import { User } from '../../models/user.model';

    @Component({
        selector: 'app-login',
        standalone: true,  // Important! This marks the component as standalone.
        templateUrl: './login.component.html',
        styleUrls: ['./login.component.css'],
        providers: [AuthService], // Services can be provided directly in the component
        imports: [] // Import necessary modules directly into the component
    })
    export class LoginComponent {
        user: User = { id: 0, username: '', password: '' };

        constructor(private authService: AuthService) {}

        login() {
            this.authService.login(this.user).subscribe(response => {
                console.log('Login successful', response);
            });
        }
    }
    ```
   Notice that we don't need to declare this component in a module because it's standalone. Instead, we can directly use the `standalone` property in the `@Component` decorator.

**Step 5: Run the application**
Run the application in port 3000 by typing this command.

```bash
$ ng serve --port=3000
```

and here is the result when accessing `/login`

<div align=center>
    <img src="/Week%2011/img/demo-standalone.png" width="800">
    <br>
      <b>Fig 3.</b> Standalone Demo Result.
    <br>
    <br>
</div>

#### **3. Why Use Standalone Components?**

**Benefits of Standalone Components:**
1. **Modularity**: Standalone components are self-contained, making them easy to reuse across different projects without worrying about module dependencies.
2. **Simplified Structure**: No need to declare the component in an `NgModule`, reducing boilerplate code.
3. **Direct Imports**: We can import other Angular modules and components directly into the standalone component, improving encapsulation.
4. **Performance**: Standalone components can be more efficient as they eliminate unnecessary dependencies from the module level.

**When to Use Standalone Components:**
- **Micro-frontend Architectures**: Where components need to be independently deployable.
- **Reusable Libraries**: Standalone components are ideal for creating reusable Angular libraries.
- **Simplifying Small Applications**: For applications with simple structures, standalone components reduce the overhead of managing multiple modules.

**Drawbacks of Non-Standalone Components:**
- **Tight Coupling**: Components are tightly coupled to modules, making them less modular.
- **Additional Boilerplate**: We need to declare components in modules, which adds more code and complexity.

### 📝 **Conclusion**

By creating both standalone and non-standalone Angular projects, we can experience firsthand the differences in setup, structure, and component management. Standalone components offer a more modular and efficient approach, particularly beneficial in modern Angular applications where reuse and simplicity are key.

---

## 👨🏻‍💻 Assignment 03 - Creating a New Standalone "Login" Component

In this assignment, we'll modify the "Login" component as a standalone Angular component from the previous one, building on the concepts learned in Assignment 02. The focus here is on achieving a clean, modular, and efficient design using Tailwind CSS for styling.

### 🚀 **Objective**

The goal is to design and implement a standalone "Login" component that:
1. **Leverages Tailwind CSS**: Utilize Tailwind CSS to create a modern, responsive design for the login interface.
2. **Implements Data Binding**: Ensure two-way data binding for the form inputs (username and password).
3. **Incorporates Service Interaction**: Use the `AuthService` to manage user authentication.
4. **Ensures Reusability**: The component should be self-contained and easily reusable in other Angular projects.

### 🛠️ **Steps to Create the Standalone Login Component**

#### **1. Setting Up the Standalone Component**

1. **Generate the Component**
   
   ```bash
   $ ng generate component components/login --standalone
   ```
   
   This will create a new standalone component named `LoginComponent`.

2. **Implement the Template**
   Replace the default HTML in [`login.component.html`](/Week%2011/standalone-app/src/app/components/login/login.component.html) with the following template that uses Tailwind CSS. This template provides a clean, modern login form with Tailwind CSS for styling.

3. **Update the Component Logic**:
   Modify [`login.component.ts`](/Week%2011/standalone-app/src/app/components/login/login.component.ts) to handle the form submission and authentication.

4. **Integrate Tailwind CSS**
    Ensure that Tailwind CSS is properly integrated into the Angular project. Update `styles.css` or `tailwind.config.js` as needed.
    1. Install the TailwindCSS
        
        ```bash
        $ npm install -D tailwindcss postcss autoprefixer
        $ npx tailwindcss init
        ```
    2. Configure [tailwind.config.js](/Week%2011/standalone-app/tailwind.config.js).
    3. Add tailwind to the global styles, which is [src/style.css](/Week%2011/standalone-app/src/styles.css).

5. **Test the Component**:
   Run the application and navigate to the `/login` route to test the standalone component.

### 🎉 **Result**

Here is the preview of "Login" page i already created as a standalone component.

<div align=center>
    <img src="/Week%2011/img/login.png" width="1000">
    <br>
      <b>Fig 4.</b> Stunning login page with Tailwind CSS!
    <br>
    <br>
</div>

<div style="display: flex; justify-content: center; align-items: center;">
    <div style="margin-right: 20px; text-align: center;">
        <img src="/Week%2011/img/laptop.png" height="500">
        <br>
        <b>Fig 5.</b> Laptop-size Mockup.
    </div>
    <div style="text-align: center;">
        <img src="/Week%2011/img/mobile.png" height="500">
        <br>
        <b>Fig 6.</b> Mobile device-size Mockup.
    </div>
</div>