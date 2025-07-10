import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.sql.*;
import java.util.Map;
import java.util.HashMap;

public class SimpleServer {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // Test database connection
        server.createContext("/test-db", new DbTestHandler());
        
        // Serve static files
        server.createContext("/", new StaticFileHandler());
        
        server.setExecutor(null);
        System.out.println("Server starting on http://localhost:8080");
        server.start();
    }
    
    static class DbTestHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            try {
                Class.forName("org.h2.Driver");
                String dbURL = "jdbc:h2:/workspaces/Vehicle-E-commerce-website/database/vehicles;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1";
                Connection conn = DriverManager.getConnection(dbURL, "sa", "");
                
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM vehicle WHERE type = 'car'");
                rs.next();
                int count = rs.getInt("count");
                
                String response = "Database connection successful! Found " + count + " cars in database.";
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
                
                conn.close();
            } catch (Exception e) {
                String response = "Database error: " + e.getMessage();
                t.sendResponseHeaders(500, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
    
    static class StaticFileHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String path = t.getRequestURI().getPath();
            if (path.equals("/")) path = "/index.html";
            
            File file = new File("/workspaces/Vehicle-E-commerce-website/web" + path);
            if (file.exists() && file.isFile()) {
                byte[] content = java.nio.file.Files.readAllBytes(file.toPath());
                t.sendResponseHeaders(200, content.length);
                OutputStream os = t.getResponseBody();
                os.write(content);
                os.close();
            } else {
                String response = "File not found: " + path;
                t.sendResponseHeaders(404, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
}
