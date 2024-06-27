# 👨🏻‍🏫 Lecture 06 - RESTful API & Relational Database
> This repository is created as a part of assignment for Lecture 06 - RESTful API & Relational Database

## 🟩 Assignment 01 - Normalization
### 📝 Normalization and Normal Forms

**Normalization** is a process in database design to organize data to reduce redundancy and improve data integrity. This involves dividing large tables into smaller ones and defining relationships between them. The primary goal is to make the database efficient by minimizing redundancy and dependency.

Normalization is accomplished through a series of steps called **Normal Forms** (NF). Each step ensures a higher level of data integrity and efficiency. Here’s a detailed explanation of the first three normal forms with examples:

### 1️⃣ **First Normal Form (1NF)**

A table is in **1NF** if:
1. **All columns contain atomic (indivisible) values.** 
2. **Each column contains values of a single type.**
3. **Each column must have a unique name.**
4. **The order in which data is stored does not matter.**

#### Example

Consider a table `Student` with the following data:

| StudentID | Name      | Courses        |
|-----------|-----------|----------------|
| 1         | Alice     | Math, Physics  |
| 2         | Bob       | Chemistry      |
| 3         | Charlie   | Math, Biology  |

In this table, the `Courses` column contains multiple values (Math, Physics). To convert this to 1NF, we need to ensure each column has atomic values:

**1NF Table:**

| StudentID | Name    | Course   |
|-----------|---------|----------|
| 1         | Alice   | Math     |
| 1         | Alice   | Physics  |
| 2         | Bob     | Chemistry|
| 3         | Charlie | Math     |
| 3         | Charlie | Biology  |

### 2️⃣ **Second Normal Form (2NF)**

A table is in **2NF** if:
1. **It is in 1NF.**
2. **All non-key attributes are fully functional dependent on the primary key.**

This means that any attribute that is not part of a candidate key must be dependent on the entire primary key, not just part of it.

#### Example

Consider a `StudentCourse` table that’s in 1NF:

| StudentID | Course     | Instructor   |
|-----------|------------|--------------|
| 1         | Math       | Dr. Smith    |
| 1         | Physics    | Dr. Johnson  |
| 2         | Chemistry  | Dr. Lee      |
| 3         | Math       | Dr. Smith    |
| 3         | Biology    | Dr. Adams    |

Here, `Instructor` depends only on `Course`, not on the `StudentID`. To convert this to 2NF, we need to create separate tables where `Instructor` depends only on `Course`.

**2NF Tables:**

1. `StudentCourse`

    | StudentID | Course     |
    |-----------|------------|
    | 1         | Math       |
    | 1         | Physics    |
    | 2         | Chemistry  |
    | 3         | Math       |
    | 3         | Biology    |

2. `CourseInstructor`

    | Course     | Instructor   |
    |------------|--------------|
    | Math       | Dr. Smith    |
    | Physics    | Dr. Johnson  |
    | Chemistry  | Dr. Lee      |
    | Biology    | Dr. Adams    |

### 3️⃣ **Third Normal Form (3NF)**

A table is in **3NF** if:
1. **It is in 2NF.**
2. **All the attributes are functionally dependent only on the primary key.**

This means there are no transitive dependencies for non-prime attributes.

#### Example

Consider a `StudentCourseInstructor` table that’s in 2NF:

| StudentID | Course     | Instructor   | Department |
|-----------|------------|--------------|------------|
| 1         | Math       | Dr. Smith    | Mathematics|
| 1         | Physics    | Dr. Johnson  | Physics    |
| 2         | Chemistry  | Dr. Lee      | Chemistry  |
| 3         | Math       | Dr. Smith    | Mathematics|
| 3         | Biology    | Dr. Adams    | Biology    |

Here, `Department` is dependent on `Instructor`, not directly on `StudentID` or `Course`. To convert this to 3NF, we need to remove the transitive dependency by creating a new table for `InstructorDepartment`.

**3NF Tables:**

1. `StudentCourse`

    | StudentID | Course     |
    |-----------|------------|
    | 1         | Math       |
    | 1         | Physics    |
    | 2         | Chemistry  |
    | 3         | Math       |
    | 3         | Biology    |

2. `CourseInstructor`

    | Course     | Instructor   |
    |------------|--------------|
    | Math       | Dr. Smith    |
    | Physics    | Dr. Johnson  |
    | Chemistry  | Dr. Lee      |
    | Biology    | Dr. Adams    |

3. `InstructorDepartment`

    | Instructor   | Department   |
    |--------------|--------------|
    | Dr. Smith    | Mathematics  |
    | Dr. Johnson  | Physics      |
    | Dr. Lee      | Chemistry    |
    | Dr. Adams    | Biology      |

### 📌 Summary

- **1NF**: Ensures each column contains atomic, indivisible values.
- **2NF**: Ensures all non-key attributes are fully dependent on the entire primary key.
- **3NF**: Ensures there are no transitive dependencies; non-key attributes are dependent only on the primary key.

Each step in normalization helps in organizing the data more efficiently, reducing redundancy, and improving data integrity.