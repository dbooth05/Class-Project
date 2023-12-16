create database if not exists student_data;
use student_data;

create table students(
	snum integer,
    ssn integer,
    name varchar(10),
    gender varchar(1),
    dob datetime,
    c_addr varchar(20),
    c_phone varchar(10),
    p_addr varchar(20),
    p_phone varchar(10),
    primary key (ssn),
    constraint snum unique (snum)
);
    
create table departments(
	code integer,
    name varchar(50),
    phone varchar(10),
    college varchar(20),
    primary key (code)
);
    
create table degrees(
	name varchar(50),
    level varchar(5),
    department_code integer,
    primary key (name, level),
    foreign key (department_code) REFERENCES departments(code)
);

create table courses(
	number integer,
    name varchar(50),
    description varchar(50),
    credithours integer,
    level varchar(20),
    department_code integer,
    primary key (number),
    foreign key (department_code) REFERENCES departments(code)
);

create table register(
	snum integer,
    course_number integer,
    regtime varchar(20),
    grade integer,
    primary key (snum, course_number),
    foreign key (snum) REFERENCES students(snum),
    foreign key (course_number) REFERENCES courses(number)    
);

create table major(
	snum integer,
    name varchar(50),
    level varchar(5),
    primary key (snum, name, level),
    foreign key (snum) REFERENCES students(snum),
    foreign key (name, level) REFERENCES degrees(name, level)
);

create table minor(
	snum integer,
    name varchar(50),
    level varchar(5),
    primary key (snum, name, level),
    foreign key (snum) REFERENCES students(snum),
	foreign key (name, level) REFERENCES degrees(name, level)
);