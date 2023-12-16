SELECT students.name, students.snum FROM students 
	INNER JOIN major ON students.snum = major.snum
    INNER JOIN register ON students.snum = register.snum
	INNER JOIN courses ON register.course_number = courses.number 
    WHERE courses.name = 'database' AND major.level != 'BS' ORDER BY students.snum;