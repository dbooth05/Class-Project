SELECT degree_name, degree_level, count FROM (
	SELECT major.name AS degree_name, major.level AS degree_level, COUNT(*) AS count
    FROM major GROUP BY major.name, major.level UNION ALL
    SELECT minor.name AS degree_name, minor.level AS degree_level, COUNT(*) AS count
    FROM minor GROUP BY minor.name, minor.level) AS degreeCounts
    WHERE (count, degree_name) In (SELECT MAX(count) AS max_count, degree_name FROM (
		SELECT major.name AS degree_name, major.level AS degree_level, COUNT(*) AS count
			FROM major GROUP BY major.name, major.level UNION ALL
		SELECT minor.name AS degree_name, minor.level AS degree_level, COUNT(*) AS count
			FROM minor GROUP BY minor.name, minor.level)
	AS sub GROUP BY degree_name HAVING MAX(count) = (SELECT MAX(count)
		FROM (SELECT major.name AS degree_name, major.level AS degree_level, COUNT(*) AS count
			FROM major GROUP BY major.name, major.level UNION ALL
		SELECT minor.name AS degree_name, minor.level AS degree_level, COUNT(*) AS count
			FROM minor GROUP BY minor.name, minor.level ) AS sub ) ) ORDER BY degree_name;