SELECT number, name, COUNT(register.snum) AS course_number FROM courses
	LEFT JOIN register ON courses.number = register.course_number
    GROUP BY courses.number, courses.name ORDER BY courses.number;