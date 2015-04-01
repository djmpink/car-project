package com.pomelo.car.web.dbutils.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.RowProcessor;

public class MapBeanHandler<T> extends AbstractMapBeanHandler<T> {
	
	private final Class<T> type;
	private final RowProcessor convert;
		
	
	public MapBeanHandler(Class<T> type, String columnName) {
		this.type = type;
		this.convert = new BasicRowProcessor();
		this.columnName = columnName;
	}

	public MapBeanHandler(Class<T> type, RowProcessor convert, String columnName) {
		this.type = type;
		this.convert = convert;
		this.columnName = columnName;
	}

	@Override
	protected T handleRow(ResultSet rs) throws SQLException {
		return this.convert.toBean(rs, type);
	}

}
