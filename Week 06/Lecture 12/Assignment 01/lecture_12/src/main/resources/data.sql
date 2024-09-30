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
('1989-11-30', 'Nancy', 'Rodriguez', 'F', '2009-11-30'),
('1977-09-05', 'Michael', 'Clark', 'M', '1997-09-05'),
('1982-11-02', 'Barbara', 'Lewis', 'F', '2002-11-02'),
('1987-10-18', 'James', 'Lee', 'M', '2007-10-18'),
('1992-01-26', 'Susan', 'Walker', 'F', '2012-01-26'),
('1978-04-17', 'Brian', 'Hall', 'M', '1998-04-17'),
('1983-03-30', 'Sarah', 'Allen', 'F', '2003-03-30'),
('1988-07-14', 'Christopher', 'Young', 'M', '2008-07-14'),
('1993-02-20', 'Patricia', 'King', 'F', '2013-02-20'),
('1979-11-23', 'George', 'Wright', 'M', '1999-11-23'),
('1984-08-09', 'Linda', 'Scott', 'F', '2004-08-09'),
('1989-06-15', 'Thomas', 'Green', 'M', '2009-06-15'),
('1994-09-29', 'Donna', 'Adams', 'F', '2014-09-29'),
('1975-12-31', 'Daniel', 'Baker', 'M', '1995-12-31'),
('1980-10-23', 'Betty', 'Gonzalez', 'F', '2000-10-23'),
('1985-05-05', 'Steven', 'Nelson', 'M', '2005-05-05'),
('1990-11-08', 'Sandra', 'Carter', 'F', '2010-11-08'),
('1976-07-15', 'Eric', 'Mitchell', 'M', '1996-07-15'),
('1981-02-27', 'Sharon', 'Perez', 'F', '2001-02-27'),
('1986-12-10', 'Kevin', 'Roberts', 'M', '2006-12-10'),
('1991-08-21', 'Carol', 'Turner', 'F', '2011-08-21'),
('1977-05-03', 'Edward', 'Phillips', 'M', '1997-05-03'),
('1982-03-11', 'Martha', 'Campbell', 'F', '2002-03-11'),
('1987-09-25', 'Joshua', 'Parker', 'M', '2007-09-25'),
('1992-06-30', 'Rebecca', 'Evans', 'F', '2012-06-30'),
('1978-12-08', 'Gregory', 'Edwards', 'M', '1998-12-08'),
('1983-07-22', 'Virginia', 'Collins', 'F', '2003-07-22'),
('1988-05-29', 'Andrew', 'Stewart', 'M', '2008-05-29'),
('1993-11-13', 'Kathleen', 'Sanchez', 'F', '2013-11-13'),
('1979-04-19', 'Henry', 'Morris', 'M', '1999-04-19'),
('1984-10-01', 'Diane', 'Rogers', 'F', '2004-10-01'),
('1989-02-16', 'Patrick', 'Reed', 'M', '2009-02-16'),
('1994-12-22', 'Deborah', 'Cook', 'F', '2014-12-22'),
('1975-03-28', 'Adam', 'Morgan', 'M', '1995-03-28'),
('1980-09-14', 'Frances', 'Bell', 'F', '2000-09-14'),
('1985-04-24', 'Raymond', 'Murphy', 'M', '2005-04-24'),
('1990-03-09', 'Jacqueline', 'Bailey', 'F', '2010-03-09'),
('1976-01-30', 'Jack', 'Rivera', 'M', '1996-01-30'),
('1981-11-18', 'Janet', 'Cooper', 'F', '2001-11-18'),
('1986-08-03', 'Walter', 'Richardson', 'M', '2006-08-03'),
('1991-04-26', 'Christine', 'Cox', 'F', '2011-04-26'),
('1977-06-06', 'Peter', 'Howard', 'M', '1997-06-06'),
('1982-12-31', 'Kathryn', 'Ward', 'F', '2002-12-31'),
('1987-05-21', 'Harold', 'Torres', 'M', '2007-05-21'),
('1992-10-15', 'Maria', 'Peterson', 'F', '2012-10-15'),
('1978-08-07', 'Douglas', 'Gray', 'M', '1998-08-07'),
('1983-01-14', 'Evelyn', 'Ramirez', 'F', '2003-01-14'),
('1988-11-27', 'Jerry', 'James', 'M', '2008-11-27'),
('1993-05-19', 'Janice', 'Watson', 'F', '2013-05-19'),
('1979-07-31', 'Ryan', 'Brooks', 'M', '1999-07-31'),
('1984-02-05', 'Heather', 'Kelly', 'F', '2004-02-05'),
('1989-10-22', 'Lawrence', 'Sanders', 'M', '2009-10-22'),
('1994-03-15', 'Judith', 'Price', 'F', '2014-03-15'),
('1975-11-29', 'Albert', 'Bennett', 'M', '1995-11-29'),
('1980-06-04', 'Ann', 'Wood', 'F', '2000-06-04'),
('1985-07-08', 'Joe', 'Barnes', 'M', '2005-07-08'),
('1990-02-03', 'Rachel', 'Ross', 'F', '2010-02-03'),
('1976-03-20', 'Arthur', 'Henderson', 'M', '1996-03-20'),
('1981-09-09', 'Julia', 'Coleman', 'F', '2001-09-09'),
('1986-11-30', 'Bruce', 'Jenkins', 'M', '2006-11-30'),
('1991-07-17', 'Hannah', 'Perry', 'F', '2011-07-17'),
('1977-02-12', 'Philip', 'Powell', 'M', '1997-02-12'),
('1982-04-29', 'Catherine', 'Long', 'F', '2002-04-29'),
('1987-03-06', 'Chris', 'Patterson', 'M', '2007-03-06'),
('1992-08-11', 'Kathy', 'Hughes', 'F', '2012-08-11'),
('1978-10-28', 'Jonathan', 'Flores', 'M', '1998-10-28'),
('1983-06-01', 'Megan', 'Washington', 'F', '2003-06-01'),
('1988-09-13', 'Albert', 'Butler', 'M', '2008-09-13'),
('1993-01-05', 'Katherine', 'Simmons', 'F', '2013-01-05'),
('1979-05-15', 'Anthony', 'Foster', 'M', '1999-05-15'),
('1984-08-25', 'Diana', 'Gonzales', 'F', '2004-08-25'),
('1989-12-02', 'Johnny', 'Bryant', 'M', '2009-12-02'),
('1994-11-14', 'Theresa', 'Alexander', 'F', '2014-11-14'),
('1975-04-07', 'Randy', 'Russell', 'M', '1995-04-07'),
('1980-01-19', 'Stephanie', 'Griffin', 'F', '2000-01-19'),
('1985-12-08', 'Jesse', 'Diaz', 'M', '2005-12-08'),
('1990-04-25', 'Angela', 'Hayes', 'F', '2010-04-25'),
('1976-08-18', 'Billy', 'Myers', 'M', '1996-08-18'),
('1981-07-04', 'Helen', 'Ford', 'F', '2001-07-04'),
('1986-05-10', 'Ralph', 'Hamilton', 'M', '2006-05-10'),
('1991-10-03', 'Frances', 'Graham', 'F', '2011-10-03'),
('1977-11-23', 'Roy', 'Sullivan', 'M', '1997-11-23'),
('1982-02-16', 'Virginia', 'Wallace', 'F', '2002-02-16'),
('1987-01-29', 'Bobby', 'Woods', 'M', '2007-01-29'),
('1992-07-20', 'Janet', 'Cole', 'F', '2012-07-20'),
('1978-06-24', 'Terry', 'West', 'M', '1998-06-24'),
('1983-09-06', 'Maria', 'Jordan', 'F', '2003-09-06'),
('1988-04-03', 'Bruce', 'Owens', 'M', '2008-04-03'),
('1993-10-30', 'Paula', 'Reynolds', 'F', '2013-10-30'),
('1979-03-18', 'Scott', 'Fisher', 'M', '1999-03-18'),
('1984-12-26', 'Kelly', 'Ellis', 'F', '2004-12-26'),
('1989-08-14', 'Sean', 'Harrison', 'M', '2009-08-14'),
('1994-05-09', 'Anne', 'Gibson', 'F', '2014-05-09'),
('1975-10-20', 'Walter', 'Mcdonald', 'M', '1995-10-20'),
('1980-05-17', 'Denise', 'Cruz', 'F', '2000-05-17'),
('1985-01-03', 'Eugene', 'Marshall', 'M', '2005-01-03'),
('1990-07-28', 'Judith', 'Ortiz', 'F', '2010-07-28'),
('1976-04-05', 'Jesse', 'Gomez', 'M', '1996-04-05'),
('1981-10-12', 'Jacqueline', 'Murray', 'F', '2001-10-12');

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
