package com.pomelo.car.web.start;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * Created by zl on 2015/3/31.
 */
public class MainStart {

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
                System.out.println(String.valueOf(p.get(key)));
            }
            xmlConfig.getProperties().putAll(props);
            xmlConfig.configure();
            xmlConfig.configure(server);
            //
            System.out.println(System.getProperty("user.dir"));
            WebAppContext webAppContext = new WebAppContext();
            FileSystemResource webappFile = new FileSystemResource("car-web/src/main/webapp/");
            if (!webappFile.exists())
            {
                String path=System.getProperty("user.dir");
                webappFile = new FileSystemResource(path+"car-web/src/main/webapp/");
                System.out.println("mch-web start sucess!1");
            }
            if (!webappFile.exists())
            {
                System.out.println("mch-web start sucess!2");
                throw new Exception("webapp path is wrong!");
            }
            webAppContext.setResourceBase(webappFile.getFile().getCanonicalPath());
            webAppContext.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
            webAppContext.setInitParameter("org.eclipse.jetty.servlet.Default.redirectWelcome", "false");
            webAppContext.setInitParameter("org.eclipse.jetty.servlet.Default.welcomeServlets", "exact");
            webAppContext.setContextPath("/");
            webAppContext.setWelcomeFiles(new String[]{"index.htm"});
            server.setHandler(webAppContext);
            server.start();
            System.out.println("mch-web start sucess!");
            server.join();

        } catch (Exception e) {
            System.out.println("mch-web start fail!");
            System.exit(0);
        }
    }


}
