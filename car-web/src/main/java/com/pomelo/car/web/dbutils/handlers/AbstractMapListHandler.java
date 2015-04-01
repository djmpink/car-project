package com.pomelo.car.web.dbutils.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.ResultSetHandler;

public abstract class AbstractMapListHandler<T> implements ResultSetHandler<Map<String,List<T>>> {
	protected String columnName;
	
	public Map<String, List<T>> handle(ResultSet rs) throws SQLException {
		Map<String, List<T>> map = new HashMap<String, List<T>>();
		 while (rs.next()) {
			 String columnValue = rs.getString(columnName);
			 List<T> list = map.get(columnValue);
			 if (list == null) {
				list = new ArrayList<T>();
				map.put(columnValue, list);
			}
			 list.add(handleRow(rs));
		 }		
		return map;
	}

	
	protected abstract T handleRow(ResultSet rs) throws SQLException;
}
