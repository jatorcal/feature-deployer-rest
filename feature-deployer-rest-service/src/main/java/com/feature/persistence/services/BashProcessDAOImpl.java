package com.feature.persistence.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.feature.persistence.common.MySqlConnection;

import feature.deployer.resources.mysql.BashProcessResource;

public class BashProcessDAOImpl implements BashProcessDAO {

	@Override
	public List<BashProcessResource> all() {
		String sql = "select PID, PROCESS_NAME, PROCESS_DESCRIPTION, TYPE_DESCRIPTION, ASSOCIATED_PID, ASSOCIATED_PROCESS_NAME from BASH_PROCESS";

		MySqlConnection mysqlConnection = new MySqlConnection();

		List<BashProcessResource> bashProcessResourceList = mysqlConnection.getJdbcTemplate().query(sql, new RowMapper() {
			public BashProcessResource mapRow(ResultSet rs, int rowNum) throws SQLException {
				BashProcessResource bashProcessResource = new BashProcessResource();
				bashProcessResource.setPid(Integer.valueOf(rs.getInt("PID")));
				bashProcessResource.setProcessName(rs.getString("PROCESS_NAME"));
				bashProcessResource.setProcessDescription(rs.getString("PROCESS_DESCRIPTION"));
				bashProcessResource.setTypeDescription(rs.getString("TYPE_DESCRIPTION"));
				bashProcessResource.setAssociatedPid(Integer.valueOf("ASSOCIATED_PID"));
				bashProcessResource.setAssociatedProcessName(rs.getString("ASSOCIATED_PROCESS_NAME"));
				return bashProcessResource;
			}
		});
		return bashProcessResourceList;
	}

	@Override
	public List<BashProcessResource> id(Integer pid) {
		String sql = "select PID, PROCESS_NAME, PROCESS_DESCRIPTION, TYPE_DESCRIPTION, ASSOCIATED_PID, ASSOCIATED_PROCESS_NAME from BASH_PROCESS where PID = #pid";
		sql = sql.replace("#pid", "" + pid);

		MySqlConnection mysqlConnection = new MySqlConnection();

		List<BashProcessResource> bashProcessResourceList = mysqlConnection.getJdbcTemplate().query(sql, new RowMapper() {
			public BashProcessResource mapRow(ResultSet rs, int rowNum) throws SQLException {
				BashProcessResource bashProcessResource = new BashProcessResource();
				bashProcessResource.setPid(Integer.valueOf(rs.getInt("PID")));
				bashProcessResource.setProcessName(rs.getString("PROCESS_NAME"));
				bashProcessResource.setProcessDescription(rs.getString("PROCESS_DESCRIPTION"));
				bashProcessResource.setTypeDescription(rs.getString("TYPE_DESCRIPTION"));
				bashProcessResource.setAssociatedPid(Integer.valueOf(rs.getInt("ASSOCIATED_PID")));
				bashProcessResource.setAssociatedProcessName(rs.getString("ASSOCIATED_PROCESS_NAME"));
				return bashProcessResource;
			}
		});
		return bashProcessResourceList;
	}
	

	@Override
	public void insert(BashProcessResource bashProcessResource) {
		
		String sql = "INSERT INTO BASH_PROCESS (PID, PROCESS_NAME, PROCESS_DESCRIPTION, TYPE_DESCRIPTION, ASSOCIATED_PID, ASSOCIATED_PROCESS_NAME, PROCESS_DATE ) VALUES (#pid, '#processName', '#processDescription', '#typeDescription', #associatedPid, '#associatedProcessName', #processDate)";
		sql = sql.replace("#pid", "" + bashProcessResource.getPid());
		sql = sql.replace("#processName", bashProcessResource.getProcessName());
		sql = sql.replace("#processDescription", bashProcessResource.getProcessDescription());
		sql = sql.replace("#typeDescription", bashProcessResource.getTypeDescription());
		sql = sql.replace("#associatedPid", "" + bashProcessResource.getAssociatedPid());
		sql = sql.replace("#associatedProcessName", bashProcessResource.getAssociatedProcessName());
		sql = sql.replace("#processDate", "STR_TO_DATE('" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+ "', '%Y-%m-%d %H:%i:%s')");
		
		MySqlConnection mysqlConnection = new MySqlConnection();
		mysqlConnection.getJdbcTemplate().execute(sql);
		
	}
	
	
	@Override
	public void delete(Integer pid) {
		
		String sql = "DELETE FROM BASH_PROCESS WHERE PID = #pid";
		sql = sql.replace("#pid", "" + pid);
		MySqlConnection mysqlConnection = new MySqlConnection();
		mysqlConnection.getJdbcTemplate().execute(sql);
		
	}

}
