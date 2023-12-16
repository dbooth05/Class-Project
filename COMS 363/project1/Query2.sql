SELECT major.name, major.level FROM major INNER JOIN students ON major.snum = students.snum WHERE students.name = 'Kevin';
