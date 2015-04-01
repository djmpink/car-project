package com.pomelo.car.web.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionManager {
	private static Logger log = LoggerFactory.getLogger(ConnectionManager.class);
	private final ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

	private ComboPooledDataSource ds;
	
//	private static ConnectionManager instance;
	private static ConcurrentHashMap<String, ConnectionManager> connPool = new ConcurrentHashMap<String, ConnectionManager>();

	public static ConnectionManager getInstance() {
		ConnectionManager instance = connPool.get("key");
		if (instance == null) {
			instance = new ConnectionManager();
			connPool.put("key", instance);
		}
		return instance;
	}

	private ConnectionManager() {
		
		log.debug(threadLocal.toString());
	}

	public Connection getConnection() {
//		log.debug("[GetConnection]begin");
//		printCurrentState();
		Connection connection = threadLocal.get();
		if (connection == null) {
			try {
				connection = ds.getConnection();
			} catch (SQLException e) {
				log.error(e.getMessage(), e);
			}
			threadLocal.set(connection);
		}
		
//		log.info(""+ds);
//		log.debug("[GetConnection]end -->"+connection);
		
		return connection;
	}

	public void closeConnection() {
//		log.debug("[Connection Close]start");
		Connection connection = threadLocal.get();
		try {
			if (connection != null && !connection.isClosed()) {
//				log.debug("[Connection Close]A:"+connection);
				if (connection.getAutoCommit()) { //判断连接是否有事务开启
//					log.debug("[Connection Close]B:"+connection);
					connection.close();
					threadLocal.set(null);
				}	
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		
//		log.debug("[Connection Close]end");
	}
	
	
	//获得当前线程上绑定的连接,并开启事务
	   public  void startTransaction() {
//		   log.debug("[Transaction Start]start");
	      Connection conn = getConnection();
	      try {
	          //此时当前线程上必定绑定了连接,开启事务!
	         conn.setAutoCommit(false);
	      } catch (SQLException e) {
	         throw new RuntimeException("Transaction Start Error.",e);
	      }
//	      log.debug("[Transaction Strat]end");
	   }
	   
	   //关闭事务
	   public void closeTransaction(){
//		   log.debug("[Transaction close]start");
		   Connection conn = getConnection();
		      try {
		    	  conn.setAutoCommit(true);
		    	  closeConnection();
		      } catch (SQLException e) {
		         throw new RuntimeException("Transaction Close Error.", e);
		      }
//		  log.debug("[Transaction close]end");
	   }
	   
	   
	   //ThreadLocal处理事务提交事务
	   public void commitTransaction() {
//		  log.debug("[Transaction commit]start");
	      Connection conn = getConnection();
	      try {
	         conn.commit();
	      } catch (Exception e) {
	         throw new RuntimeException("Transaction Commit Error." ,e);
	      }
//	      log.debug("[Transaction commit]end");
	   }
	   
	   //事务回滚
	   public void rollbackTransaction(){
//		   log.debug("[Transaction rollback]start");
		   Connection conn = getConnection();
		      try {
		    	  conn.rollback();
		      } catch (SQLException e) {
		         throw new RuntimeException("Transaction Rollback Error." ,e);
		      }			
//		  log.debug("[Transaction rollback]end");  
		}
	   
	   public void printCurrentState(){
//		   System.out.println("A2");
//		   if (connPool != null && !connPool.isEmpty()) {
//			   for (String key : connPool.keySet()) {
//				ConnectionManager  cm = connPool.get(key);
				
				
//				try {
//					System.out.println("A:"+cm.ds.sampleThreadPoolStackTraces());
//					System.out.println("B:\n"+cm.ds.sampleThreadPoolStatus());
//					System.out.println("C:"+cm.ds.sample);
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}			
//		}
		   
	   }

	public void setDs(ComboPooledDataSource ds) {
		this.ds = ds;
	}
	   
}
