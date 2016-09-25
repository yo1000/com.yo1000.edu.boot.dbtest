package com.yo1000.edu.boot.dbtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Repository
	public static class TestFrameworkRepository {
		private JdbcTemplate jdbcTemplate;

		public TestFrameworkRepository(JdbcTemplate jdbcTemplate) {
			this.jdbcTemplate = jdbcTemplate;
		}

		public List<Map<String, String>> findAll() {
			return getJdbcTemplate().query("SELECT GROUP_ID, ARTIFACT_ID, DESC FROM TEST_FRAMEWORK",
					(resultSet, i) -> {
						Map<String, String> item = new HashMap<>();

						item.put("groupId"	 	, resultSet.getString("GROUP_ID"));
						item.put("artifactId"	, resultSet.getString("ARTIFACT_ID"));
						item.put("desc"			, resultSet.getString("DESC"));

						return item;
					});
		}

		public JdbcTemplate getJdbcTemplate() {
			return jdbcTemplate;
		}
	}
}
