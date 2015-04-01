package com.pomelo.car.web.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.log4j.Logger;

import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;
import com.pomelo.duoduo.db.ConnectionManager;
import com.pomelo.duoduo.db.GenBeanProcess;
import com.pomelo.duoduo.dbutils.handlers.MapBeanColumnHandler;
import com.pomelo.duoduo.dbutils.handlers.MapBeanHandler;
import com.pomelo.duoduo.dbutils.handlers.MapListBeanColumnHandler;
import com.pomelo.duoduo.dbutils.handlers.MapListBeanHandler;
import com.pomelo.duoduo.utils.DBUtil;

public class BaseDao<T> {
	//new
	public static final String COUNT_FIELD = "total";
	private Class<T> type;
	public ConnectionManager connManager;
	protected Logger log  = Logger.getLogger(this.getClass());
	//public Logger log;

	public BaseDao(Class<T> type) {
		this(type, ConnectionManager.CONFIG_FILE_LOCATION);
		
	}
	
	public BaseDao(Class<T> type, String configFile){
		log = Logger.getLogger(this.getClass());
		this.type = type;
		connManager = ConnectionManager.getInstance(configFile);
	}	

	/**
	 * 数据查询基础方法，所有的查询最终都是调用到该方法上
	 * 
	 * @param sql
	 *            查询的sql语句
	 * @param handler
	 *            结果集的处理策略
	 * @param params
	 *            查询的条件参数
	 * @return 返回查询结果的列表
	 * @author xiaosc
	 */
	protected <E> E find(String sql, ResultSetHandler<E> handler, Object... params) {
		log.debug("find(sql)-->" + sql);
		E result = null;
		QueryRunner qr = new QueryRunner();
		try {
			result = qr.query(connManager.getConnection(), sql, handler, params);
//			qr.query(conn, sql, params, rsh)
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		} finally {
			connManager.closeConnection();
		}
		return result;
	}
	
	
	protected <E> E find(String sql, ResultSetHandler<E> handler, List<Object> params)
	{
		if (params != null && !params.isEmpty()) {
			return find(sql, handler, params.toArray());
		}
		return find(sql, handler);
		
	}
	

	/**
	 * 查询单列数据
	 * 
	 * @param type
	 *            返回数据类型
	 * @param sql
	 *            sql语句
	 * @param columnName
	 *            列名
	 * @param params
	 *            查询参数
	 * @return
	 * @author xiaosc
	 */
	protected <E> List<E> findListForColumn(Class<E> type, String sql, String columnName, Object... params) {

		return find(sql, new ColumnListHandler<E>(columnName), params);
	}
	
	
	protected <E> List<E> findListForColumn(Class<E> type, String sql, String columnName, List<Object> params) {

		return find(sql, new ColumnListHandler<E>(columnName), params);
	}
	

	/**
	 * 根据条件查询数据列表
	 * 
	 * @param type
	 *            需要返回的数据类别
	 * @param sql
	 *            查询sql
	 * @param params
	 *            查询参数
	 * @return
	 */
	protected <E> List<E> findListForBean(Class<E> type, String sql, Object... params) {

		return find(sql, new BeanListHandler<E>(type, new BasicRowProcessor(new GenBeanProcess())), params);
	}
	
	protected <E> List<E> findListForBean(Class<E> type, String sql, List<Object> params) {

		return find(sql, new BeanListHandler<E>(type, new BasicRowProcessor(new GenBeanProcess())), params);
	}
	

	/**
	 * 根据条件查询单条数据
	 * 
	 * @param type
	 *            需要返回的数据类别
	 * @param sql
	 *            查询sql
	 * @param params
	 *            查询参数
	 * @return
	 */
	protected <E> E findForBean(Class<E> type, String sql, Object... params) {
		List<E> list = findListForBean(type, sql, params);
		return (list != null && list.size() != 0) ? list.get(0) : null;
	}
	
	protected <E> E findForBean(Class<E> type, String sql, List<Object> params) {
		List<E> list = findListForBean(type, sql, params);
		return (list != null && list.size() != 0) ? list.get(0) : null;
	}

	/**
	 * 查询当前T数据列表
	 * 
	 * @param sql
	 *            查询sql
	 * @param params
	 *            查询参数
	 * @return
	 */
	protected List<T> getList(String sql, Object... params) {
		return findListForBean(type, sql, params);
	}
	
	protected List<T> getList(String sql, List<Object> params) {
		return findListForBean(type, sql, params);
	}

	/**
	 * 查询当前单个T数据
	 * 
	 * @param sql
	 *            查询sql
	 * @param params
	 *            查询参数
	 * @return
	 */
	protected T get(String sql, Object... params) {
		return findForBean(type, sql, params);
	}
	
	protected T get(String sql, List<Object> params) {
		return findForBean(type, sql, params);
	}

	/**
	 * 统计符合sql条件的总记录数 <br/>
	 * 使用的时候必须用[COUNT(*) as total] *为要统计的字段名
	 * 
	 * @return
	 * @since 1.0
	 * @author xsc
	 */
	protected int getTotalNum(String sql, Object... params) {
		int total = 0;

		try {
			Map<String, Object> map = find(sql, new MapHandler(), params);
			if (map != null && !map.isEmpty()) {
				String n = map.get(COUNT_FIELD).toString();
				total = Integer.parseInt(n);
			}	
		} catch (Exception e) {
			log.error("", e);
		}

		return total;
	}
	
	
	protected int getTotalNum(String sql, List<Object> params) {
		int total = 0;

		try {
			Map<String, Object> map = find(sql, new MapHandler(), params);
			String n = map.get(COUNT_FIELD).toString();
			total = Integer.parseInt(n);
		} catch (Exception e) {
			log.error("", e);
		}

		return total;
	}

	/**
	 * 根据需求返回结果集，等同于数据库查询操作方法excuteQuery(String sql)
	 * 
	 * @param sql
	 * @return List<Map<String, Object>>类型结果，map中以查询的列名作为Key值，Object类型作为Value
	 * @throws SQLException
	 */
	protected List<Map<String, Object>> findForListMap(String sql, Object... params) throws SQLException {

		return find(sql, new MapListHandler(new BasicRowProcessor(new GenBeanProcess())), params);

	}
	
	protected List<Map<String, Object>> findForListMap(String sql, List<Object> params) throws SQLException {

		return find(sql, new MapListHandler(new BasicRowProcessor(new GenBeanProcess())), params);

	}
	
	/**
	 * 根据查询条件将查询出的结果
	 * <br/>根据某一列的值作为Key,符合数据的列值封装为Bean将其添加到List作为Value,返回其Map数据对象
	 * @param type            返回数据对象的类别
	 * @param sql             查询数据的sql
	 * @param columnName      需要作为key值的列名
	 * @param params          查询的参数
	 * @return
	 * @throws SQLException
	 */
	protected <E> Map<String,List<E>> findForMapList(Class<E> type, String sql, String columnName, Object... params) throws SQLException {

		return find(sql, new MapListBeanHandler<E>(type, new BasicRowProcessor(new GenBeanProcess()), columnName), params);
	}
	
	/**
	 * 根据查询条件将查询出的结果
	 * <br/>根据某一列的值作为key,符合数据的列值封装为Bean作为Value,返回其Map数据对象
	 * <br/>如果有多个符合key值的对象Bean,则数据只会返回其最后一个对应的Bean值
	 * @param type            返回数据对象的类别
	 * @param sql             查询数据的sql
	 * @param columnName      需要作为key值的列名
	 * @param params          查询的参数
	 * @return
	 * @throws SQLException
	 */
	protected <E> Map<String,E> findForMapBean(Class<E> type, String sql, String columnName, Object... params) throws SQLException {

		return find(sql, new MapBeanHandler<E>(type, new BasicRowProcessor(new GenBeanProcess()), columnName), params);
	}
	
	protected <E> Map<String,List<E>> getForMapList(Class<E> type, String sql, String columnName, Object... params){
		try {
			return findForMapList(type,  sql, columnName, params);
		} catch (SQLException e) {
			log.error("", e);
		}
		return null;
	}
	
	protected <E> Map<String,E> getForMapBean(Class<E> type, String sql, String columnName, Object... params){	
		try {
			return findForMapBean(type, sql, columnName, params);
		} catch (SQLException e) {
			log.error("", e);
		}
		return null;
	}
	

	
	/**
	 * 根据查询条件将查询出的结果
	 * <br/>根据某一列的值作为Key,然其对应的另外一列的值将其添加到List作为Value,返回其Map数据对象
	 * @param type             返回数据对象的类别
	 * @param sql              查询数据的sql
	 * @param keyColumnName    需要作为key值的列名
	 * @param valueColumnName  需要作为Value值的列名
	 * @param params           查询的参数
	 * @return
	 * @throws SQLException
	 */
	protected <E> Map<String,List<E>> findColumnForMapList(Class<E> type, String sql, String keyColumnName, String valueColumnName, Object... params) throws SQLException {
		return find(sql, new MapListBeanColumnHandler<E>(keyColumnName, valueColumnName), params);
	}
		
	/**
	 * 根据查询条件将查询出的结果
	 * <br/>根据某一列的值作为Key,然其对应的另外一列的值作为Value,返回其Map数据对象
	 * @param type             返回数据对象的类别
	 * @param sql              查询数据的sql
	 * @param keyColumnName    需要作为key值的列名
	 * @param valueColumnName  需要作为Value值的列名
	 * @param params           查询的参数
	 * @return
	 * @throws SQLException
	 */
	protected <E> Map<String,E> findColumnForMapBean(Class<E> type, String sql, String keyColumnName, String valueColumnName, Object... params) throws SQLException {
		return find(sql, new MapBeanColumnHandler<E>(keyColumnName, valueColumnName), params);
	}
	
	
	protected <E> Map<String,List<E>> getColumnForMapList(Class<E> type, String sql, String keyColumnName, String valueColumnName, Object... params){
		try {
			return findColumnForMapList(type, sql, keyColumnName, valueColumnName, params);
		} catch (SQLException e) {
			log.error("", e);
		}
		return null;
	}
	
	protected <E> Map<String,E> getColumnForMapBean(Class<E> type, String sql, String keyColumnName, String valueColumnName, Object... params){
		try {
			return findColumnForMapBean(type, sql, keyColumnName, valueColumnName, params);
		} catch (SQLException e) {
			log.error("", e);
		}
		return null;
	}
	
	

	/**
	 * 将记录插入数据库
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	protected int executeInsert(String sql, Object[] fields) throws SQLException {
		int id = -1;
		try {
			PreparedStatement ps = null;
			ResultSet rs = null;
			log.debug("execute-->" + sql);
			ps = connManager.getConnection().prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			if (fields != null && fields.length > 0) {
				for (int i = 0; i < fields.length; i++) {
					ps.setObject(i + 1, fields[i]);
				}
			}

			ps.executeUpdate();
			rs = ps.getGeneratedKeys();

			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			connManager.closeConnection();
		}
		return id;
	}
	
	/**
	 * 插入数据库记录
	 * <br/>注意:表中必须要有自增的id字段
	 * @param sql
	 * @param params
	 * @return 成功返回插入后自增的id,失败返回-1
	 * @throws SQLException
	 */
	protected int insert(String sql, Object... params) {
		try {
			return executeInsert(sql, params);
		} catch (SQLException e) {
			return -1;
		}
	}
	

	/**
	 * 等同于数据库更新操作方法executeUpdate(String sql)
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	protected int executeUpdate(String sql, Object... params) throws SQLException {
		log.debug("executeUpdate(sql)-->" + sql);
		int result = 0;

		Connection conn = connManager.getConnection();
		QueryRunner qr = new QueryRunner();

		try {			
			result = qr.update(conn, sql, params);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			connManager.closeConnection();
		}
		return result;
	}
	
	/**
	 * 更新数据
	 * <br/>当更新时影响的行数大于0时返回true,否则返回false
	 * @param sql
	 * @param params
	 * @return
	 */
	protected boolean update(String sql, Object... params){
		boolean flag = false;
		
		try {
			int affectedRows = executeUpdate(sql, params);
			if (affectedRows > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			log.error("", e);
		}
		
		return flag;
	}
	

	/**
	 * 批量执行插入、更新、删除操作
	 * @param sql  sql语句
	 * @param params  二维参数数组
	 * @return  受影响的行数的数组
	 * @throws SQLException
	 */
	protected int[] batchExecute(String sql, Object[][] params) throws SQLException {		
		Connection conn = connManager.getConnection();
		QueryRunner queryRunner = new QueryRunner();

		int[] affectedRows = new int[0];
		try {
			affectedRows = queryRunner.batch(conn, sql, params);			
		} catch (SQLException e) {
			throw e;
		} finally {
			connManager.closeConnection();
		}
		return affectedRows;
	}

	
	/**
	 * 批量执行SQL的 update insert delete
	 * <br/>当影响的行数大于0时返回true,否则返回false
	 * @param sql  sql语句
	 * @param params 二维参数数组
	 * @return
	 */
	protected boolean batchUpdate(String sql, Object[][] params) {
		boolean flag = false;
		try {
			int[] affectedRows = batchExecute(sql, params);
			int result = 0;
			for (int i = 0; i < affectedRows.length; i++) {
				result += affectedRows[i];
			}
			if (result > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			log.error("Error occured while attempting to batch update data", e);
		}
		return flag;
	}
	
	
	//---------------------------old---------------------------
	protected static Connection conn=null;
	protected static PreparedStatement ps = null;
	protected static ResultSet rs = null;
	
	
	/**
	 * 将bean对象插入到数据库
	 */
	public int insertBean(Class clazz, Object obj) {  
        if (obj == null || clazz.getSimpleName().equals(obj.getClass().getName())) { 
            return -1;  
        }
        Field[] fields = obj.getClass().getDeclaredFields();  
        int fieldSize = fields.length;  
        String tableName = clazz.getSimpleName().toLowerCase();// 表名  
        String[] types1 = { "int", "java.lang.String", "boolean", "char", "float", "double", "long", "short", "byte" };  
        String[] types2 = { "Integer","java.lang.Integer", "java.lang.String", "java.lang.Boolean","java.lang.Character", "java.lang.Float", "java.lang.Double","java.lang.Long", "java.lang.Short", "java.lang.Byte" };  
  
        StringBuffer sql = new StringBuffer("insert into " + tableName  + " values (");  
        for (int i = 0; i < fieldSize; i++) {  
            sql.append(" ?,");  
        }  
        sql.deleteCharAt(sql.length() - 1);  
        sql.append(")");  
        log.info(sql);
        int id = -1;
        try {  
        	conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql.toString(),Statement.RETURN_GENERATED_KEYS);
            for (int j = 0; j < fieldSize; j++) {  
                fields[j].setAccessible(true);  
                for (int i = 0; i < types1.length; i++) {  
                    if (fields[j].getType().getName().equalsIgnoreCase(types1[i])||fields[j].getType().getName().equalsIgnoreCase(types2[i])) {  
                        if (fields[j].get(obj) != null&& !"".equals(fields[j].get(obj))&& !"null".equals(fields[j].get(obj))) {  
                            ps.setObject(j + 1, fields[j].get(obj));  
                        } else {  
                            ps.setObject(j + 1, null);  
                        }  
                    }  
                }  
            }  
            
            System.out.println(ps.toString());  
            ps.executeUpdate();  
            rs = ps.getGeneratedKeys();
			if(rs.next()){
				id=rs.getInt(1);
				log.info("id : "+id);
			}
            //conn.commit();  
			DBUtil.close(conn);
        } catch (Exception e1) {  
            e1.printStackTrace();  
        }  
        return id;
    }  
	
	
    /**
     * 通过缓存结果集构造对象列表
     * @param <T>
     * @param clazz javaBean类
     * @param crs   缓存结果集
     * @return 对象列表
     */
    public static <E> List<E> getBeanList(Class<E> clazz){
         
        List<Object> result = new ArrayList<Object>();
        try {
            ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
            int columnCount = md.getColumnCount();
            Object o = null;
            while (rs.next()) {
                 o = clazz.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    if(rs.getObject(i) == null){
                        continue ;
                    }
                    md.getColumnType(i);
                    String className = md.getColumnClassName(i);
                    if("java.sql.Timestamp".equals(className) || "java.sql.Date".equals(className)){
                        className = "java.util.Date" ;
                    }else if("java.sql.Time".equals(className)){
                        className = "java.lang.String" ;
                    }else if("java.lang.Byte".equals(className) || "java.lang.Short".equals(className)){
                        className = "java.lang.Integer" ;
                    }
                    String methodName = "set"+md.getColumnName(i).substring(0,1).toUpperCase() + md.getColumnName(i).substring(1);
                    try{
                        clazz.getMethod(methodName,Class.forName(className)).invoke(o, rs.getObject(i));
                    }catch(NoSuchMethodException e){
                        continue;
                    }
                }
                result.add(o);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return  (List<E>) result;
    }

}
