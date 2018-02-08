package com.feature.persistence.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.feature.persistence.common.MySqlConnection;

import feature.deployer.resources.mysql.CurrentFrontDeployResource;

public class CurrentFrontDeployProcessDAOImpl implements CurrentFrontDeployProcessDAO {

	
	@Override
	public List<CurrentFrontDeployResource> all() {
		String sql = "select PID_BASH_PROCESS, PROJECT_NAME, PROJECT_DESCRIPTION, SERVER_PORT, BACKEND_SERVER_PORT from CURRENT_FRONT_DEPLOY";

		MySqlConnection mysqlConnection = new MySqlConnection();

		List<CurrentFrontDeployResource> currentFrontDeployResourceList = mysqlConnection.getJdbcTemplate().query(sql, new RowMapper() {
			public CurrentFrontDeployResource mapRow(ResultSet rs, int rowNum) throws SQLException {
				CurrentFrontDeployResource currentFrontDeployResource = new CurrentFrontDeployResource();
				currentFrontDeployResource.setPidBashProcess(Integer.valueOf(rs.getInt("PID_BASH_PROCESS")));
				currentFrontDeployResource.setProjectName(rs.getString("PROJECT_NAME"));
				currentFrontDeployResource.setProjectDescription(rs.getString("PROJECT_DESCRIPTION"));
				currentFrontDeployResource.setServerPort(Integer.valueOf(rs.getInt("SERVER_PORT")));
				currentFrontDeployResource.setBackendServerPort(Integer.valueOf(rs.getInt("BACKEND_SERVER_PORT")));
				return currentFrontDeployResource;
			}
		});
		return currentFrontDeployResourceList;
	}
	
	
	@Override
	public CurrentFrontDeployResource currentprojectName(String projectName) {
		String sql = "select PID_BASH_PROCESS, PROJECT_NAME, PROJECT_DESCRIPTION, SERVER_PORT, BACKEND_SERVER_PORT from CURRENT_FRONT_DEPLOY WHERE PROJECT_NAME = '#projectName'";
		sql = sql.replace("#projectName", projectName);

		MySqlConnection mysqlConnection = new MySqlConnection();

		List<CurrentFrontDeployResource> currentFrontDeployResourceList = mysqlConnection.getJdbcTemplate().query(sql, new RowMapper() {
			public CurrentFrontDeployResource mapRow(ResultSet rs, int rowNum) throws SQLException {
				CurrentFrontDeployResource currentFrontDeployResource = new CurrentFrontDeployResource();
				currentFrontDeployResource.setPidBashProcess(Integer.valueOf(rs.getInt("PID_BASH_PROCESS")));
				currentFrontDeployResource.setProjectName(rs.getString("PROJECT_NAME"));
				currentFrontDeployResource.setProjectDescription(rs.getString("PROJECT_DESCRIPTION"));
				currentFrontDeployResource.setServerPort(Integer.valueOf(rs.getInt("SERVER_PORT")));
				currentFrontDeployResource.setBackendServerPort(Integer.valueOf(rs.getInt("BACKEND_SERVER_PORT")));
				return currentFrontDeployResource;
			}
		});
		
		if (currentFrontDeployResourceList.isEmpty()) {
			return null;
		}
		return currentFrontDeployResourceList.get(0);
	}

	@Override
	public void insert(CurrentFrontDeployResource currentFrontDeployResource) {

		String sql = "INSERT INTO CURRENT_FRONT_DEPLOY (PID_BASH_PROCESS, PROJECT_NAME, PROJECT_DESCRIPTION, SERVER_PORT, BACKEND_SERVER_PORT, PROCESS_DATE) VALUES (#pidBashProcess, '#projectName', '#projectDescription', #serverPort, #backendServerPort, #processDate)";
		sql = sql.replace("#pidBashProcess", "" + currentFrontDeployResource.getPidBashProcess());
		sql = sql.replace("#projectName", currentFrontDeployResource.getProjectName());
		sql = sql.replace("#projectDescription", currentFrontDeployResource.getProjectDescription());
		sql = sql.replace("#serverPort", "" + currentFrontDeployResource.getServerPort());
		sql = sql.replace("#backendServerPort", "" + currentFrontDeployResource.getBackendServerPort());
		sql = sql.replace("#processDate", "STR_TO_DATE('" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+ "', '%Y-%m-%d %H:%i:%s')");
		
		MySqlConnection mysqlConnection = new MySqlConnection();
		mysqlConnection.getJdbcTemplate().execute(sql);
	}

	@Override
	public void delete(CurrentFrontDeployResource currentFrontDeployResource) {

		String sql = "DELETE FROM CURRENT_FRONT_DEPLOY WHERE PROJECT_NAME = '#projectName'";
		sql = sql.replace("#projectName", currentFrontDeployResource.getProjectName());
		MySqlConnection mysqlConnection = new MySqlConnection();
		mysqlConnection.getJdbcTemplate().execute(sql);
		
	}	
	
	@Override
	public void delete(String projectName) {

		String sql = "DELETE FROM CURRENT_FRONT_DEPLOY WHERE PROJECT_NAME = '#projectName'";
		sql = sql.replace("#projectName", projectName);
		MySqlConnection mysqlConnection = new MySqlConnection();
		mysqlConnection.getJdbcTemplate().execute(sql);
		
	}

}
