package com.feature.persistence.common;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class MySqlConnection {
	
	
	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;

	public MySqlConnection() {
		setDataSource(getMySQLDataSource());
	}

	public static DataSource getMySQLDataSource() {
		MysqlDataSource mysqlDS = null;
		try {
			String db = "jdbc:mysql://localhost:33061/FEATURE_DEPLOYER";
			String user = "root";
			String password = "jta010es";
			mysqlDS = new MysqlDataSource();
			mysqlDS.setURL(db);
			mysqlDS.setUser(user);
			mysqlDS.setPassword(password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mysqlDS;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public DataSource getDataSource() {
		return this.dataSource;
	}

	public JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
