SELECT degree_name, degree_level, counts FROM (
	SELECT major.name AS degree_name, major.level AS degree_level, COUNT(*) AS counts
		FROM major GROUP BY major.name, major.level) AS degreeCounts
	WHERE (counts, degree_name) IN (SELECT MAX(counts) AS max_counts, degree_name FROM (
		SELECT major.name AS degree_name, major.level AS degree_level, COUNT(*) AS counts
			FROM major GROUP BY major.name, major.level)
		AS sub GROUP BY degree_name HAVING MAX(counts) = (SELECT MAX(counts)
			FROM (SELECT major.name AS degree_name, major.level AS degree_level, COUNT(*) AS counts
				FROM major GROUP BY major.name, major.level) AS sub ) ) ORDER BY degree_name;