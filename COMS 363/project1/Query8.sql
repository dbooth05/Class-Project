SELECT name, snum, ssn FROM students
	WHERE name LIKE '%n%' OR name LIKE '%N%' ORDER BY snum;