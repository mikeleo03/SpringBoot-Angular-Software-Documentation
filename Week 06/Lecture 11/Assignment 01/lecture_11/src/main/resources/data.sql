-- Database schema initializer
-- Create employees table
CREATE TABLE employees (
    emp_no INT AUTO_INCREMENT PRIMARY KEY,
    birth_date DATE NOT NULL,
    first_name VARCHAR(14) NOT NULL,
    last_name VARCHAR(16) NOT NULL,
    gender ENUM('M', 'F') NOT NULL,
    hire_date DATE NOT NULL
);

-- Create departments table
CREATE TABLE departments (
    dept_no CHAR(4) PRIMARY KEY,
    dept_name VARCHAR(40) NOT NULL UNIQUE
);

-- Create dept_emp table
CREATE TABLE dept_emp (
    emp_no INT NOT NULL,
    dept_no CHAR(4) NOT NULL,
    from_date DATE NOT NULL,
    to_date DATE NOT NULL,
    PRIMARY KEY (emp_no, dept_no),
    FOREIGN KEY (emp_no) REFERENCES employees(emp_no) ON DELETE CASCADE,
    FOREIGN KEY (dept_no) REFERENCES departments(dept_no) ON DELETE CASCADE
);

-- Create dept_manager table
CREATE TABLE dept_manager (
    emp_no INT NOT NULL,
    dept_no CHAR(4) NOT NULL,
    from_date DATE NOT NULL,
    to_date DATE NOT NULL,
    PRIMARY KEY (emp_no, dept_no),
    FOREIGN KEY (emp_no) REFERENCES employees(emp_no) ON DELETE CASCADE,
    FOREIGN KEY (dept_no) REFERENCES departments(dept_no) ON DELETE CASCADE
);

-- Create salaries table
CREATE TABLE salaries (
    emp_no INT NOT NULL,
    from_date DATE NOT NULL,
    salary INT NOT NULL,
    to_date DATE NOT NULL,
    PRIMARY KEY (emp_no, from_date),
    FOREIGN KEY (emp_no) REFERENCES employees(emp_no) ON DELETE CASCADE
);

-- Create titles table
CREATE TABLE titles (
    emp_no INT NOT NULL,
    title VARCHAR(50) NOT NULL,
    from_date DATE NOT NULL,
    to_date DATE,
    PRIMARY KEY (emp_no, title, from_date),
    FOREIGN KEY (emp_no) REFERENCES employees(emp_no) ON DELETE CASCADE
);

-- Database Initial Seeding
-- Insert employees
INSERT INTO employees (birth_date, first_name, last_name, gender, hire_date) VALUES
('1980-01-01', 'John', 'Doe', 'M', '2000-01-01'),
('1985-05-23', 'Jane', 'Smith', 'F', '2005-05-01'),
('1990-07-11', 'Alice', 'Johnson', 'F', '2010-06-01'),
('1975-02-14', 'Bob', 'Brown', 'M', '1995-03-01'),
('1988-12-25', 'Charlie', 'Davis', 'M', '2008-12-01'),
('1981-04-10', 'David', 'Evans', 'M', '2001-04-10'),
('1986-08-15', 'Laura', 'Wilson', 'F', '2006-08-15'),
('1991-03-22', 'Karen', 'Garcia', 'F', '2011-03-22'),
('1976-06-12', 'Paul', 'Martinez', 'M', '1996-06-12'),
('1989-11-30', 'Nancy', 'Rodriguez', 'F', '2009-11-30');

-- Insert departments
INSERT INTO departments (dept_no, dept_name) VALUES
('d001', 'Marketing'),
('d002', 'Finance'),
('d003', 'Human Resources'),
('d004', 'Engineering'),
('d005', 'Sales');

-- Insert dept_emp
INSERT INTO dept_emp (emp_no, dept_no, from_date, to_date) VALUES
(1, 'd001', '2000-01-01', '2002-01-01'),
(1, 'd002', '2002-01-01', '9999-01-01'),
(2, 'd002', '2005-05-01', '2010-05-01'),
(2, 'd003', '2010-05-01', '9999-01-01'),
(3, 'd003', '2010-06-01', '9999-01-01'),
(3, 'd004', '2011-01-01', '9999-01-01'),
(4, 'd004', '1995-03-01', '9999-01-01'),
(4, 'd005', '2000-01-01', '9999-01-01'),
(5, 'd001', '2008-12-01', '9999-01-01'),
(5, 'd005', '2010-01-01', '9999-01-01'),
(6, 'd002', '2001-04-10', '2003-04-10'),
(6, 'd003', '2003-04-10', '9999-01-01'),
(7, 'd003', '2006-08-15', '2011-08-15'),
(7, 'd004', '2011-08-15', '9999-01-01'),
(8, 'd001', '2011-03-22', '9999-01-01'),
(9, 'd004', '1996-06-12', '2006-06-12'),
(9, 'd005', '2006-06-12', '9999-01-01'),
(10, 'd005', '2009-11-30', '9999-01-01');

-- Insert dept_manager
INSERT INTO dept_manager (emp_no, dept_no, from_date, to_date) VALUES
(1, 'd001', '2000-01-01', '2002-01-01'),
(2, 'd002', '2005-05-01', '2010-05-01'),
(3, 'd003', '2010-06-01', '2011-01-01');

-- Insert salaries
INSERT INTO salaries (emp_no, salary, from_date, to_date) VALUES
(1, 60000, '2000-01-01', '2002-01-01'),
(1, 65000, '2002-01-01', '9999-01-01'),
(2, 75000, '2005-05-01', '2010-05-01'),
(2, 80000, '2010-05-01', '9999-01-01'),
(3, 80000, '2010-06-01', '2011-01-01'),
(3, 85000, '2011-01-01', '9999-01-01'),
(4, 90000, '1995-03-01', '2000-01-01'),
(4, 95000, '2000-01-01', '9999-01-01'),
(5, 85000, '2008-12-01', '2010-01-01'),
(5, 90000, '2010-01-01', '9999-01-01'),
(6, 65000, '2001-04-10', '2003-04-10'),
(6, 70000, '2003-04-10', '9999-01-01'),
(7, 70000, '2006-08-15', '2011-08-15'),
(7, 75000, '2011-08-15', '9999-01-01'),
(8, 72000, '2011-03-22', '9999-01-01'),
(9, 95000, '1996-06-12', '2006-06-12'),
(9, 100000, '2006-06-12', '9999-01-01'),
(10, 86000, '2009-11-30', '9999-01-01');

-- Insert titles
INSERT INTO titles (emp_no, title, from_date, to_date) VALUES
(1, 'Manager', '2000-01-01', '2002-01-01'),
(1, 'Senior Manager', '2002-01-01', '9999-01-01'),
(2, 'Analyst', '2005-05-01', '2010-05-01'),
(2, 'Senior Analyst', '2010-05-01', '9999-01-01'),
(3, 'HR Specialist', '2010-06-01', '2011-01-01'),
(3, 'HR Manager', '2011-01-01', '9999-01-01'),
(4, 'Engineer', '1995-03-01', '2000-01-01'),
(4, 'Senior Engineer', '2000-01-01', '9999-01-01'),
(5, 'Sales Representative', '2008-12-01', '2010-01-01'),
(5, 'Senior Sales Representative', '2010-01-01', '9999-01-01'),
(6, 'Finance Specialist', '2001-04-10', '2003-04-10'),
(6, 'Senior Finance Specialist', '2003-04-10', '9999-01-01'),
(7, 'HR Manager', '2006-08-15', '2011-08-15'),
(7, 'Senior HR Manager', '2011-08-15', '9999-01-01'),
(8, 'Marketing Specialist', '2011-03-22', '9999-01-01'),
(9, 'Senior Engineer', '1996-06-12', '2006-06-12'),
(9, 'Chief Engineer', '2006-06-12', '9999-01-01'),
(10, 'Senior Sales Representative', '2009-11-30', '9999-01-01');
