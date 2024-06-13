# 👨🏻‍🏫 Lecture 02 - Java Core
> This repository is created as a part of assignment for Lecture 02 - Java Core

## 🧵 Task 05 - Thead-safe singleton
A thread-safe singleton guarantees that a class has only one instance and provides a global point of access to that instance, even in programs with multiple tasks (threads) running at once. This is crucial in concurrent applications where multiple threads might try to create an instance of a class at the same time, leading to potential race conditions.

### 🔑 Key Concepts of Thread-Safe Singleton
1. **Singleton Pattern**
A class that allows only one instance to be created and provides a global point of access to that instance.
2. **Thread-Safety**
Ensuring that the singleton instance is created and accessed safely when multiple threads are involved, preventing race conditions and ensuring consistent behavior.

### 💻 Implementation
One way to implement a thread-safe singleton is using a method named **Double-Checked Locking**. Here's the implementation of `Singleton` class by modifying the Lazy implementation on slide using double-checked locking.

```java
class Singleton {
    // Volatile variable to ensure visibility of changes across threads
    private static volatile Singleton instance;

    // Private constructor to prevent instantiation from outside
    private Singleton() {}

    // Public method to provide access to the singleton instance
    public static Singleton getInstance() {
        // First check if instance is null without synchronization
        if (instance == null) {
            // Synchronize on the class to ensure only one thread creates the instance
            synchronized (Singleton.class) {
                // Double-check if instance is still null within synchronized block
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }

        return instance;
    }

    public String showMessage() {
        return "Hello from double-checked locking Singleton!";
    }

    public static void main(String[] args) {
        // Accessing the singleton instance
        Singleton singleton = Singleton.getInstance();
        System.out.println(singleton.showMessage());    // Outputs : Hello from double-checked locking Singleton!
    }
}
```

Additionally, here is how to access the Singleton in multi-threaded environment.

```java
// Task to run in each thread
class SingletonTestTask implements Runnable {
    @Override
    public void run() {
        // Obtain the singleton instance
        Singleton instance = Singleton.getInstance();
        
        // Print the instance and thread name
        System.out.println(Thread.currentThread().getName() + ": " + instance.showMessage());
    }
}

public class Main {
    public static void main(String[] args) {
        // Number of threads to create
        int threadCount = 10;
        
        // Create and start threads
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new SingletonTestTask(), "Thread-" + i);
            thread.start();
        }
    }
}
```

And the output will be something like this.
![Screenshot](img/Task5.png)

Here is the detail on what it actually does.
1. The `volatile` keyword ensures that changes to the `instance` variable are visible to all threads. This prevents the scenario where one thread sees a partially constructed object.
2. The constructor is private to prevent the instantiation of the class from outside the `Singleton` class, enforcing the singleton property.
3. The `getInstance` method provides the global point of access to the singleton instance.
    1. **First Check**: The `if (instance == null)` check is performed outside the synchronized block to avoid synchronization overhead once the instance is created.
    2. **Synchronized Block**: synchronized `(Singleton.class)` ensures that only one thread can execute this block at a time, preventing multiple threads from creating separate instances.
    3. **Double-Checked Locking**: Inside the synchronized block, if `(instance == null)` is checked again to make sure the instance is still null before creating a new one. This is necessary because the first check might have passed for multiple threads before any of them entered the synchronized block.

There is other way on implementing thread-safe singleton named **Bill Pugh Singleton**. Here's the implementation of `Singleton` class using Bill Pugh singleton.

```java
public class Singleton {
    // Private constructor to prevent instantiation.
    private Singleton() {}

    // Static inner helper class responsible for holding the Singleton instance
    private static class SingletonHelper {
        // The instance is created when the class is loaded
        private static final Singleton INSTANCE = new Singleton();
    }

    // Public method to provide access to the instance
    public static Singleton getInstance() {
        return SingletonHelper.INSTANCE;
    }
}
```

The `SingletonHelper` class is not loaded until the `getInstance()` method is called, providing lazy initialization. This approach does not require synchronization and ensures that the instance is created only when it's needed.
<br>
