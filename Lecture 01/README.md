# Lecture 01 - Java Core
> This repository is created as a part of assignment for Lecture 01 - Java Core

## Task 01 - Value Types and Reference Types
### Value Types
In Java, all the primitive data types (int, float, double, char) are value types. Value types store the actual data value directly in the variable, and they are passed by value.
When using a primitive data type, we are working with the actual value, not a reference to an object in memory.

Example
```java
int a = 35;         // a stores the actual value 35
float b = 1.0f;     // b stores the actual value 1.0
```

### Reference Types
Reference types are used to store the references or memory addresses which pointing to the actual data or objects. These types are typically used for complex data structures and objects that can be of varying sizes.

Example
**a. Classes**
User-defined classes are reference types. We can use `new` keyword to instantiate an instance of a class. What actually done is, it creates an object in memory, and a reference to that object is stored in a variable.

```java
Person person = new Person();       // obj is a reference to an instance of Person class
```

**b. Arrays**
Arrays in Java are reference types, which can hold references to other objects or arrays.

```java
int[] arrayOfLinteger = new int[5];         // arrayOfLinteger is a reference to an array of int
```

And any other examples like Strings and Interfaces.

## Task 02 - How OOP Principles perform in Java
### Abstraction
Abstraction involves hiding the complex implementation details of a system and exposing only the necessary and relevant parts. It helps in reducing programming complexity and effort. For instance, when you use a List in Java, you don't need to know the internal workings of how the list is managed; you only use the methods provided to interact with the list.
> In java concepts, we could use abstract class and interfaces.
```java
// Abstract class
abstract class Animal {
    // Abstract method
    public abstract void animalSound();
    // Regular method
    public void sleep() {
        System.out.println("Zzz");
    }
}

// Subclass, inherit the animal class
class Pig extends Animal {
    public void animalSound() {
        // The body of animalSound() is provided here
        System.out.println("Oink oink");
    }
}
```

### Encapsulation
Encapsulation is the practice of bundling attributes and methods that operate on the data into a single unit or class. It restricts direct access to some of an object’s components, which can prevent the accidental modification of data. Instead, access is controlled through public methods, often referred to as getters and setters.
> In java concepts, it could perform as access modifiers and getters-setters.
```java
public class Person {
    // Private attribute
    private String name;

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String newName) {
        this.name = newName;
    }
}
```

### Inheritance
Inheritance allows a new class, known as a subclass or derived class, to inherit attributes and methods from an existing class, known as a superclass or base class. This promotes code reuse and can create a natural hierarchy. For example, a Car class can inherit from a Vehicle class, meaning it automatically has the properties and behaviors of a vehicle.
> In java concepts, it could be implemented using extends to class
```java
class Vehicle {
    // Vehicle properties
    protected String brand = "Ford";
    public void honk() {
        System.out.println("Tuut, tuut!");
    }
}

class Car extends Vehicle {
    // Add new Car attribute
    private String modelName = "Mustang";
    public static void main(String[] args) {
        // Create a myCar object
        Car newCar = new Car();

        // Call the honk() method (from the Vehicle class)
        myCar.honk(); // return "Tuut, tuut!"
    }
}
```

### Polymorphism
Polymorphism enables a single function, method, or operator to operate in different ways based on the context. It allows objects to be treated as instances of their parent class rather than their actual class, making the code more flexible and easier to extend. Method overriding and method overloading are common examples.
> In java concepts, it perform like method overloading and overriding.
```java
class Animal {
    public void animalSound() {
        System.out.println("The animal makes a sound");
    }
}

class Pig extends Animal {
    // Also implements the animalSound(), but the output will be "Oink oink"
    public void animalSound() {
        System.out.println("Oink oink");
    }
}
```