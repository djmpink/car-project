package com.pomelo.car.web.dbutils.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapBeanColumnHandler<T> extends AbstractMapBeanHandler<T> {
	
	private final int valueColumnIndex;
	private final String valueColumnName;
		
	
	public MapBeanColumnHandler(String keyColumnName, String valueColumnName) {
		this(keyColumnName, 1, valueColumnName);
	}

	public MapBeanColumnHandler( String keyColumnName, int valueColumnIndex, String valueColumnName) {
		super();
		this.valueColumnIndex = valueColumnIndex;
		this.valueColumnName = valueColumnName;
		this.columnName = keyColumnName;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected T handleRow(ResultSet rs) throws SQLException {
		if (this.valueColumnName == null) {
			
			return (T) rs.getObject(this.valueColumnIndex);
		} else {
			return (T) rs.getObject(this.valueColumnName);
		}
	}

}
