SELECT COUNT(DISTINCT students.snum) AS count_of_females FROM students
	INNER JOIN major ON students.snum = major.snum
    INNER JOIN minor ON students.snum = minor.snum
    WHERE (major.name = 'Software Engineering' OR minor.name = 'Software Engineering')
    AND students.gender = 'F';