# 👩🏻‍🏫 Lecture 04 - Java Core
> This repository is created as a part of assignment for Lecture 04 - Java Core

## 🎞️ Assignment 06 - Stream Processing
### 💻 Task 1 - Parallel Streams
#### 🤔 When to Use Parallel Streams?
**Parallel streams** in Java provide a way to perform parallel processing. They split the source data into multiple parts and process them simultaneously on different CPU cores. This can significantly reduce the time required for processing large datasets or computationally expensive operations. Here are some scenarios where parallel streams can be particularly beneficial.
1. **Large Datasets**: When dealing with large collections, the performance gain from parallelism can be substantial.
2. **Independent Tasks**: Tasks that can be executed independently without relying on the results of other tasks.
3. **Computationally Intensive Operations**: Operations that require significant CPU time, such as complex calculations, can benefit from parallel execution.
4. **Sufficient Hardware Resources**: Systems with multiple cores can take full advantage of parallel streams. For systems with fewer cores, the benefits might be limited or negligible.

#### 📌 Considerations of Using Parallel Stream
There are some considerations on using parallel streams.
1. **Thread Safety**: Operations on data should be stateless and thread-safe to avoid race conditions and inconsistencies.
2. **Splitting Overhead**: The cost of splitting the data into smaller chunks and merging the results can sometimes outweigh the benefits of parallelism, especially for small datasets.
3. **Order Sensitivity**: Parallel processing can lead to different orders of execution. If the order of results matters, ensure that the operation is suitable for parallel processing or use additional mechanisms to preserve order.
4. **System Load**: Running many parallel tasks can lead to high system load and resource contention. Monitor system performance to ensure it doesn't degrade.

#### ❓ How to Use Parallel Streams
Using parallel streams in Java is straightforward. We can convert a stream to a parallel stream using the `parallelStream()` method on a collection or the `parallel()` method on a sequential stream.

Here are an example to demonstrate the uses of parallel streams.

**Summing elements in parallel**

```java
import java.util.stream.LongStream;

public class ParallelSumExample {
    public static void main(String[] args) {
        long sum = LongStream.range(1, 1_000_001)
                             .parallel()
                             .sum();

        System.out.println("Sum: " + sum);
        // Output: Sum: 500000500000
    }
}
```

**Filtering in parallel**

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParallelFilterExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Anna", "Bob", "Charlie", "David", "Edward", "Anna");

        List<String> distinctNames = names.parallelStream()
                                          .distinct()
                                          .filter(name -> name.length() > 3)
                                          .collect(Collectors.toList());

        distinctNames.forEach(System.out::println);
        // Output: Anna Charlie David Edward
    }
}
```

**Reducing data in parallel**

```java
import java.util.Arrays;
import java.util.List;

public class ParallelReduceExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Sum all numbers using reduce in parallel
        int sum = numbers.parallelStream()
                         .reduce(0, Integer::sum);

        System.out.println("Sum: " + sum);
    }
}
```
<br>

### 👥 Task 2 - Remove Duplicate Elements from a List of Strings Using Streams
#### 📢 Concept and Approach
Removing duplicates from a list involves transforming the list into a new list that contains only unique elements. Java Streams facilitate this through the `distinct()` method, which returns a stream consisting of distinct elements (according to `Object.equals(Object)`).

Here's a step-by-step of the approach:
1. **Create the List**: Start with a list of strings that may contain duplicates.
2. **Convert to Stream**: Convert this list into a stream.
3. **Apply `distinct()`**: Use the `distinct()` method to filter out duplicate elements.
4. **Collect Results**: Collect the unique elements into a new list using the `collect()` method.

#### 👨🏻‍💻 Implementation
Detail implementation is written on [this code](/Week%2002%20-%20Jun%2017-21/Lecture%2004/Assignment%206/RemoveDuplicates.java). Here’s what the program actually done.
1. **Map to Lowercase**: map `(String::toLowerCase)` converts all strings to lowercase, ensuring that case variations are treated as duplicates.
2. **Remove Duplicates**: The `distinct()` method then removes duplicates as before.
3. **Collect Results**: The final list of unique strings is collected.

Given the input of the array like this

```java
List<String> fruits = Arrays.asList("Apple", "banana", "APPLE", "Cherry", "banana", "Date");
```

The output of the program shows like this.

![Screenshot](img/Task2.png)

<br>

### 🗝️ Task 3 - Remove Lines with Duplicate Data by Key Field
#### 📢 Concept and Approach
Here’s a detailed process on how to remove duplicate lines from files based on a key field, using Java Streams. This involves reading the file, processing the data to identify and remove duplicates, and writing the results back to a new file.
1. **Read the File**: Load the file's contents into a list of strings, where each string represents a line in the file.
2. **Identify the Key Field**: Determine which part of specific column in a CSV will be used to identify duplicates.
3. **Filter Duplicates**: Use a combination of mapping and collecting methods to filter out duplicates based on the key field.
4. **Write the Output**: Write the processed, duplicate-free data to a new file.

#### 👨🏻‍💻 Implementation
Detail implementation is written on [this code](/Week%2002%20-%20Jun%2017-21/Lecture%2004/Assignment%206/RemoveDuplicatesCSV.java). Here’s what the program actually done.
1. **Read All Lines**: `Files.readAllLines(Paths.get(inputFilePath))` reads the CSV file into a list of strings.
2. **Extract Header**: The first line is treated as the header to determine the key field's index.
3. **Stream Processing**: The stream skips the header, then collects lines into a map using the key field (`id`). If a duplicate key is found, the first occurrence is retained.
4. **Write Results**: The header is re-added, and the list is written to the new file.

**Best Practices Highlighted**
1. **`BufferedReader` for Large Files**: Using `BufferedReader` with `lines()` streams data efficiently.
2. **`Try-with-Resources`**: Ensures that the `BufferedReader` is closed automatically.
3. **Error Handling**: Captures and handles `IOException`.
4. **Avoid Magic Numbers**: Using descriptive variable names like `keyFieldName` makes the code more readable.

Given the CSV file as an input on this [data.csv](/Week%2002%20-%20Jun%2017-21/Lecture%2004/Assignment%206/data/data.csv) and the result is showed up on this [unique.csv](/Week%2002%20-%20Jun%2017-21/Lecture%2004/Assignment%206/data/unique.csv).
<br>

### #️⃣ Task 4 - Count the Number of Strings Starting with a Specific Letter Using Streams
#### 📢 Concept and Approach
To count the number of strings that start with a specific letter, follow these steps.
1. **Create the `List`**: Have a list of strings that we want to analyze.
2. **Convert to Stream**: Convert the list into a stream to facilitate processing.
3. **Filter Strings**: Use the filter method to keep only those strings that start with the specified letter.
4. **Count Matches**: Use the count method to count the filtered strings.

#### 👨🏻‍💻 Implementation
Detail implementation is written on [this code](/Week%2002%20-%20Jun%2017-21/Lecture%2004/Assignment%206/CountStrings.java). Here’s what the program actually done.
1. **Stream Conversion**: `colors.stream()` converts the list to a stream.
2. **Filter Strings**: `filter(color -> color.startsWith(letter))` retains only the strings that start with the specified letter.
3. **Count Matches**: `count()` counts these filtered strings.

Given the input of the array like this

```java
List<String> colors = Arrays.asList("Red", "Green", "Blue", "Pink", "Brown");
String letter = "G";
```

The output of the program shows like this.

![Screenshot](img/Task4.png)

#### 💡 Considerations for Case Sensitivity
By default, the startsWith method is case-sensitive. If we want to make the comparison case-insensitive, convert both the string and the letter to lower (or upper) case before comparing.

```java
long count = colors.stream()
                   .filter(color -> color.toLowerCase().startsWith(letter.toLowerCase()))
                   .count();
```
<br>

### 🙋🏻‍♂️ Task 5 - Sorting and Finding Data in a List of Students Using Java Streams
When working with a list of students represented as objects, we might often need to perform operations such as sorting, finding the student with the maximum age, or checking if any student's name matches a specific keyword. Java Streams offer a clear and concise way to perform these tasks.

#### 📖 Tasks and Approach
- Sort Names Alphabetically (Ascending)
- Find the Student with Maximum Age
- Check if Any Student's Name Matches a Specific Keyword

#### 📶 1. Sort Names Alphabetically (Ascending)
Sorting the list of students by their names involves converting the list to a stream, applying the sorted method with a comparator, and collecting the results back into a list.

Here is approach on how to implement this.
- **`Comparator.comparing(Student::getName)`**: Creates a comparator for sorting by the name property.
- **`collect(Collectors.toList())`**: Collects the sorted elements into a new list.

#### 🔍 2. Find Student with Maximum Age
To find the student with the maximum age, use the max method with a comparator. It returns an `Optional` which ensures safe handling if the list is empty.

Here is approach on how to implement this.
- **`max(Comparator.comparing(Student::getAge))`**: Finds the student with the maximum age.
- **`ifPresent`**: Handles the result safely, printing the student if present.

#### 👀 3. Check if Any Student’s Name Matches a Specific Keyword
To check if any student’s name matches a specific keyword, use the `anyMatch` method with a predicate that checks if the name contains the keyword.

Here is approach on how to implement this.
- **`map(Student::getName)`**: Maps each Student object to its name property.
- **`anyMatch(name -> name.contains(keyword)`**: Checks if any name contains the keyword.

#### 👨🏻‍💻 Implementation
Detail implementation is written on [this code](/Week%2002%20-%20Jun%2017-21/Lecture%2004/Assignment%206/StudentOperations.java). Given the input of the array like this

```java
List<Student> students = Arrays.asList(
    new Student("Alice", 24),
    new Student("David", 21),
    new Student("Bob", 22),
    new Student("Charlie", 23),
    new Student("Frank", 19),
    new Student("Eve", 20)
);
```

The output of the program shows like this.

![Screenshot](img/Task5.png)

#### ✨ Best Practices
- **Immutable Classes**: Ensure Student is immutable by making fields final and providing no setters.
- **Error Handling**: Handle potential errors gracefully, especially when dealing with optional results.
- **Performance Considerations**: For large lists, consider parallel streams if the operations are independent and performance is critical.