package com.pomelo.car.web.start;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * Created by zl on 2015/3/31.
 */
public class MainStart {
    private static final Logger logger = LoggerFactory.getLogger(MainStart.class);
    public static void main(String[] args)  throws Exception{
        try {
            Server server = new Server();
//            Pay1payErrorHandler errorHandler = new Pay1payErrorHandler();
//            server.addBean(errorHandler);
            ClassPathResource configPath = new ClassPathResource("config.properties");
            Properties p = new Properties(System.getProperties());
            p.load(configPath.getInputStream());

            ClassPathResource classpath = new ClassPathResource("jetty.xml");
            XmlConfiguration xmlConfig = new XmlConfiguration(classpath.getInputStream());
            Map<String, String> props = new HashMap<String, String>();
            for (Object key : p.keySet()) {
                props.put(key.toString(), String.valueOf(p.get(key)));
                logger.info(String.valueOf(p.get(key)));
            }
            xmlConfig.getProperties().putAll(props);
            xmlConfig.configure();
            xmlConfig.configure(server);
            //
            logger.info(System.getProperty("user.dir"));
            WebAppContext webAppContext = new WebAppContext();
            FileSystemResource webappFile = new FileSystemResource("src/main/webapp/");
            if (!webappFile.exists())
            {
                //String path=System.getProperty("user.dir");
                webappFile = new FileSystemResource("car-web/src/main/webapp/");
                logger.info("start 1!");
            }
            if (!webappFile.exists())
            {
                logger.info("mch-web start sucess!2");
                throw new Exception("webapp path is wrong!");
            }
            System.out.println("mch-web start sucess!2");
            webAppContext.setResourceBase(webappFile.getFile().getCanonicalPath());
            webAppContext.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
            webAppContext.setInitParameter("org.eclipse.jetty.servlet.Default.redirectWelcome", "false");
            webAppContext.setInitParameter("org.eclipse.jetty.servlet.Default.welcomeServlets", "exact");
            webAppContext.setContextPath("/");
            webAppContext.setWelcomeFiles(new String[]{"index.html"});
            server.setHandler(webAppContext);
            server.start();
            logger.info("Jetty Start Sucess!");
            server.join();

        } catch (Exception e) {
            logger.info("Jetty Start Fail!");
            System.exit(0);
        }
    }


}
