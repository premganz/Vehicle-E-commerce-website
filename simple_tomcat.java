import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class simple_tomcat {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        
        // Set base directory
        tomcat.setBaseDir("/tmp/tomcat");
        
        // Add webapp
        Context ctx = tomcat.addWebapp("/vehicle-ecommerce", new File("/tmp/webapps/vehicle-ecommerce").getAbsolutePath());
        
        // Extract WAR
        File warFile = new File("/tmp/webapps/vehicle-ecommerce.war");
        if (warFile.exists()) {
            tomcat.addWebapp("/vehicle-ecommerce", warFile.getAbsolutePath());
        }
        
        tomcat.start();
        System.out.println("Tomcat started at http://localhost:8080/vehicle-ecommerce/");
        tomcat.getServer().await();
    }
}
