# 👩🏻‍🏫 Lecture 04 - Java Core
> This repository is created as a part of assignment for Lecture 04 - Java Core

## 📣 Assignment 08 - Serialization
### 🤔 Task 1 - What Is the `serialVersionUID`?
The `serialVersionUID` is a version control identifier used in Java for `Serializable` classes. It plays a crucial role in the serialization and deserialization process, ensuring that the versions of the class being serialized and deserialized are compatible.

#### 🗝️ Key Points
1. **Definition and Purpose**
    - The `serialVersionUID` is a long value that serves as a unique identifier for a `Serializable` class.
    - It is used during deserialization to verify that the sender and receiver of a serialized object have loaded classes for that object that are compatible in terms of serialization.
2. **Default Behavior**
    - If we do not explicitly declare a `serialVersionUID`, the Java runtime will generate one based on various aspects of the class such as its fields, methods, and constructors. This default generation can be inconsistent because even minor changes to the class structure can lead to a different `serialVersionUID`.
3. **Explicit Declaration**
    - It is recommended to explicitly define `serialVersionUID` to ensure consistent serialization behavior and to avoid InvalidClassException. This is done using the private static final long `serialVersionUID` field in the class.
        ```java
        private static final long serialVersionUID = 1L;
        ```
    - This value should be changed whenever the structure of the class changes in a way that affects the serialized form (e.g., adding/removing fields).
4. **Exception Handling**
    - If the `serialVersionUID` of a class does not match the `serialVersionUID `of the serialized object, Java throws an `InvalidClassException` during deserialization. This prevents incompatible classes from being deserialized and ensures data integrity.
5. **Backward and Forward Compatibility**
    - Explicitly defining `serialVersionUID` can help manage backward compatibility. If a class is updated but remains backward compatible, the same `serialVersionUID` can be maintained.
    - For forward compatibility (making sure older versions can still read the new serialized data), careful design and versioning using `serialVersionUID` are crucial.

#### 🎬 Scenario
Consider the `Employee` class from the serialization example. Suppose we initially define the class as follows.
```java
import java.io.Serializable;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int id;
    private double salary;

    public Employee(String name, int id, double salary) {
        this.name = name;
        this.id = id;
        this.salary = salary;
    }
}
```

Suppose we later update the class by adding a new field.
```java
import java.io.Serializable;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L; // Ensure it matches the old version

    private String name;
    private int id;
    private double salary;
    private String department; // New field added

    public Employee(String name, int id, double salary, String department) {
        this.name = name;
        this.id = id;
        this.salary = salary;
        this.department = department;
    }
}
```

If we had serialized objects of the old version of the Employee class and try to deserialize them using the new class definition.
- **If `serialVersionUID` is the same**: Java will attempt to deserialize using the existing serialized form. We may need to handle the new field appropriately (e.g., provide default values or modify the read process).
- **If `serialVersionUID` is different**: An `InvalidClassException` will be thrown, indicating the serialized object does not match the current class version.

#### ❓ Why Should We Use `serialVersionUID`?
1. **Consistency**: By defining `serialVersionUID`, we avoid the pitfalls of the automatically generated value changing due to even trivial modifications in the class.
2. **Control**: We gain control over the serialization compatibility between different versions of the class, making it easier to maintain and evolve your application.
3. **Avoiding Runtime Exceptions**: Prevents unexpected exceptions related to class compatibility, making your application more robust.

<br>

### 🖼️ Task 2 - Illustrate Serialization and Deserialization
Serialization in Java is the process of converting an object into a byte stream to save it to a file or transmit it over a network. Deserialization is the reverse process, where the byte stream is converted back into an object. This is particularly useful for saving object states, transferring objects between different JVMs, or persisting objects across program runs.

#### 🐾 Steps for Serialization and Deserialization
1. Define the `Employee` Class.
    - Implement the `Serializable` interface.
    - Declare `serialVersionUID` for version control.
2. Serialize the List of `Employee` Objects.
    - Create a list of `Employee` objects.
    - Write the list to a file using `ObjectOutputStream`.
3. Deserialize the List of `Employee` Objects.
    - Read the list from the file using `ObjectInputStream`.
    - Convert the byte stream back into a list of `Employee` objects.

#### ✍ Implementation
Here is all the class i implemented.
1. **[Employee Class](/Week%2002%20-%20Jun%2017-21/Lecture%2004/Assignment%208/Employee.java)**

    This class is created as the object that will be serialized and deserialized.
2. **[SerializeExample Class](/Week%2002%20-%20Jun%2017-21/Lecture%2003/Assignment%204/BankAccountDemo.java)**

    This class is created to serialize the list of `Employee` objects.
    - Creates a list of `Employee` objects.
    - Uses `FileOutputStream` to create a file named `employees.ser`.
    - `ObjectOutputStream` writes the list of `Employee` objects to the file, converting them to a byte stream.
3. **[DeserializeExample Class](/Week%2002%20-%20Jun%2017-21/Lecture%2004/Assignment%208/DeserializeExample.java)**

    This class is created to deserialize the list of `Employee` objects.
    - Opens the `employees.ser` file using `FileInputStream`.
    - `ObjectInputStream` reads the byte stream and converts it back into a list of `Employee` objects.
    - Iterates through the list and prints each `Employee` object.

Here is the detailed explanation.
##### Serialization Process
1. **Creating the Object Stream**
    - **`FileOutputStream`**: Opens a file to write data.
    - **`ObjectOutputStream`**: Wraps the `FileOutputStream` and provides methods to serialize objects.
2. **Writing the Objects**
    - `writeObject` method of `ObjectOutputStream` is used to write the list of objects to the file.
    - This method traverses each object, converts it into a byte stream, and stores it in the file.

###### Deserialization Process
1. Opening the Object Stream
    - **`FileInputStream`**: Opens the file to read data.
    - **`ObjectInputStream`**: Wraps the `FileInputStream` and provides methods to deserialize objects.
2. Reading the Objects
    - `readObject` method of `ObjectInputStream` reads the byte stream from the file.
    - It converts the byte stream back into a list of `Employee` objects.

#### 🎯 Handling Compatibility with `serialVersionUID`
##### Why Use `serialVersionUID`?
1. To ensure the `Employee` class version during serialization matches the version during deserialization.
2. If the class structure changes (e.g., adding/removing fields), the `serialVersionUID` helps in maintaining compatibility or identifying incompatibility.

##### Example Scenario
1. **Initial version**

    ```java
    public class Employee implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private int id;
        private double salary;
    }
    ```

2. **Modified Version (with the same `serialVersionUID`)**

    ```java
    public class Employee implements Serializable {
        private static final long serialVersionUID = 1L; // Remain same for compatibility
        private String name;
        private int id;
        private double salary;
        private String department; // New field added
    }
    ```
3. **Deserializing Old Objects**

    If we use the same `serialVersionUID` and add a new field (`department`), older serialized objects can still be deserialized but may need default values or special handling for the new field.

##### When to Change `serialVersionUID`?
If changes in the class are not backward compatible (e.g., changing field types or names, removing fields), update `serialVersionUID` to reflect a new version, indicating the serialized objects are incompatible with previous versions.