package com.pomelo.car.web.dbutils.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbutils.ResultSetHandler;

public abstract class AbstractMapBeanHandler<T> implements ResultSetHandler<Map<String,T>> {
	protected String columnName;
	public Map<String,T> handle(ResultSet rs) throws SQLException {
		Map<String,T> map = new HashMap<String,T>();
		 while (rs.next()) {
			 String columnValue = rs.getString(columnName);
			 map.put(columnValue, handleRow(rs));
		 }		
		return map;
	}

	protected abstract T handleRow(ResultSet rs) throws SQLException;
}
