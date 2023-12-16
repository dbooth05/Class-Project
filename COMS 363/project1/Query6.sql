SELECT students.snum, students.name FROM students 
	INNER JOIN minor ON students.snum = minor.snum 
    ORDER BY students.snum;