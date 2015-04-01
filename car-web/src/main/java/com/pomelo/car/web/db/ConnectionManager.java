package com.pomelo.car.web.db;



import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionManager {
	private static Logger log = LoggerFactory.getLogger(ConnectionManager.class);
	public static String CONFIG_FILE_LOCATION = "db.properties";
	private final ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();
	private String configFile = CONFIG_FILE_LOCATION;
	private ComboPooledDataSource ds;
	
//	private static ConnectionManager instance;
	private static ConcurrentHashMap<String, ConnectionManager> connPool = new ConcurrentHashMap<String, ConnectionManager>();

	public synchronized static ConnectionManager getInstance(String path) {
		
			if (path == null) {
				path = CONFIG_FILE_LOCATION;
			}
			
			ConnectionManager instance = connPool.get(path);
			if (instance == null) {
				instance = new ConnectionManager(path);
				connPool.put(path, instance);
				
			}
			
		return instance;
	}

	public static ConnectionManager getInstance() {
		return getInstance(CONFIG_FILE_LOCATION);
	}

	private void init() {
		log.debug("A1");
		Properties dbProps = new Properties();
		try {
			InputStream is = ConnectionManager.class.getResourceAsStream("/"+configFile);
			dbProps.load(is);
			log.info("db config load success!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DB config load failed.");
			throw new RuntimeException("DB config load failed.");
		}
		ds = new ComboPooledDataSource();
		try {
			String driverClass = dbProps.getProperty("c3p0.driverClass", "com.mysql.jdbc.Driver").trim();
			ds.setDriverClass(driverClass);
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("com.mysql.jdbc.Driver加载失败");
		}
		log.info(dbProps.toString());
		ds.setJdbcUrl(dbProps.getProperty("c3p0.JdbcUrl").trim());
		ds.setUser(dbProps.getProperty("c3p0.user").trim());
		String password = dbProps.getProperty("c3p0.password").trim();
		log.info("password="+password);
		ds.setPassword(password);
		// ds.setPassword(UtilCommon.dec(dbProps.getProperty("c3p0.password").trim()));
		// 连接关闭时默认将所有未提交的操作回滚。Default: false autoCommitOnClose
		ds.setAutoCommitOnClose(true);

		// 定义所有连接测试都执行的测试语句。在使用连接测试的情况下这个一显著提高测试速度。注意：
		// 测试的表必须在初始数据源的时候就存在。Default: null preferredTestQuery
		ds.setPreferredTestQuery("select 1");
		// 因性能消耗大请只在需要的时候使用它。如果设为true那么在每个connection提交的
		// 时候都将校验其有效性。建议使用idleConnectionTestPeriod或automaticTestTable
		// 等方法来提升连接测试的性能。Default: false testConnectionOnCheckout
		ds.setTestConnectionOnCheckout(false);
		// 如果设为true那么在取得连接的同时将校验连接的有效性。Default: false testConnectionOnCheckin
		ds.setTestConnectionOnCheckin(false);
		// 获取连接失败将会引起所有等待连接池来获取连接的线程抛出异常。但是数据源仍有效
		// 保留，并在下次调用getConnection()的时候继续尝试获取连接。如果设为true，那么在尝试
		// 获取连接失败后该数据源将申明已断开并永久关闭。Default: false breakAfterAcquireFailure
		ds.setBreakAfterAcquireFailure(false);
		
		
		
		

		try {
			// 初始化时获取三个连接，取值应在minPoolSize与maxPoolSize之间。Default: 3
			// initialPoolSize
			ds.setInitialPoolSize(Integer.parseInt(dbProps.getProperty("c3p0.initialPoolSize").trim()));
			// ds.setInitialPoolSize(3);
			// 连接池中保留的最大连接数。Default: 15 maxPoolSize
			ds.setMaxPoolSize(Integer.parseInt(dbProps.getProperty("c3p0.maxPoolSize").trim()));
			// ds.setMaxPoolSize(10);
			// 连接池中保留的最小连接数。
			ds.setMinPoolSize(Integer.parseInt(dbProps.getProperty("c3p0.minPoolSize").trim()));
			// ds.setMinPoolSize(1);
			// 当连接池中的连接耗尽的时候c3p0一次同时获取的连接数。Default: 3 acquireIncrement
			ds.setAcquireIncrement(Integer.parseInt(dbProps.getProperty("c3p0.acquireIncrement").trim()));
			// ds.setAcquireIncrement(1);
			// 每60秒检查所有连接池中的空闲连接。Default: 0 idleConnectionTestPeriod
			ds.setIdleConnectionTestPeriod(Integer.parseInt(dbProps.getProperty("c3p0.idleConnectionTestPeriod").trim()));
			// ds.setIdleConnectionTestPeriod(60);
			// 最大空闲时间,25000秒内未使用则连接被丢弃。若为0则永不丢弃。Default: 0 maxIdleTime
			ds.setMaxIdleTime(Integer.parseInt(dbProps.getProperty("c3p0.maxIdleTime").trim()));
			// ds.setMaxIdleTime(25000);
			// 定义在从数据库获取新连接失败后重复尝试的次数。Default: 30 acquireRetryAttempts
			ds.setAcquireRetryAttempts(Integer.parseInt(dbProps.getProperty("c3p0.acquireRetryAttempts").trim()));
			// ds.setAcquireRetryAttempts(30);
			// 两次连接中间隔时间，单位毫秒。Default: 1000 acquireRetryDelay
			ds.setAcquireRetryDelay(Integer.parseInt(dbProps.getProperty("c3p0.acquireRetryDelay").trim()));
			// ds.setAcquireRetryDelay(1000);
			ds.setMaxStatements(0);
			
			ds.setUnreturnedConnectionTimeout(Integer.parseInt(dbProps.getProperty("c3p0.unreturnedConnectionTimeout", "0")));
			ds.setDebugUnreturnedConnectionStackTraces(Boolean.parseBoolean(dbProps.getProperty("c3p0.debugUnreturnedConnectionStackTraces","false")));
			
			log.info("db set config success!");
		} catch (Exception e) {
			log.error("oh, db set config failed!");
			e.printStackTrace();
		}

		
	}

	private ConnectionManager() {
		init();
		try {
//			InitialContext ictx = new InitialContext();
//			DataSource dSource = (DataSource) ictx.lookup("");
			log.debug("A1");
			
		} catch (Exception e) {
			log.error("", e);
		}
		
		log.debug(threadLocal.toString());
	}

	private ConnectionManager(String dbFilePath) {
		configFile = dbFilePath;
		log.debug(threadLocal.toString());
		init();
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
	
}
