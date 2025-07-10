#!/usr/bin/env python3
import http.server
import socketserver
import urllib.request
import urllib.parse
import os
from pathlib import Path

class ProxyHandler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        # Set the directory to serve static files from
        self.webapp_dir = "/workspaces/Vehicle-E-commerce-website/target/webapp"
        super().__init__(*args, directory=self.webapp_dir, **kwargs)
    
    def do_GET(self):
        # Parse the URL
        parsed_path = urllib.parse.urlparse(self.path)
        path = parsed_path.path
        query = parsed_path.query
        
        # Define servlet paths that should be forwarded to Tomcat
        servlet_paths = ['/VehicleServlet', '/cart', '/checkOut', '/moneyTime']
        jsp_extensions = ['.jsp']
        
        # Check if this is a servlet request or JSP  
        is_servlet = any(path.startswith(sp) for sp in servlet_paths) or path.startswith('/VehicleServlet')
        is_jsp = any(path.endswith(ext) for ext in jsp_extensions)
        
        # Log the request for debugging
        print(f"Request: {self.path} -> path: {path}, query: {query}")
        print(f"Checking servlet paths: {servlet_paths}")
        for sp in servlet_paths:
            print(f"  Does '{path}' start with '{sp}'? {path.startswith(sp)}")
        print(f"Is servlet: {is_servlet}, Is JSP: {is_jsp}")
        
        if is_servlet or is_jsp:
            # Forward to Tomcat
            print(f"Forwarding to Tomcat: {self.path}")
            self.forward_to_tomcat()
        elif path == '/' or path == '/index.html':
            # Redirect root to index.jsp by forwarding to Tomcat
            original_path = self.path
            self.path = '/index.jsp'
            print(f"Redirecting {original_path} to /index.jsp")
            self.forward_to_tomcat()
            self.path = original_path
            return
        else:
            # Handle static files
            print(f"Serving static file: {path}")
            super().do_GET()
    
    def do_POST(self):
        # All POST requests go to Tomcat
        self.forward_to_tomcat()
    
    def forward_to_tomcat(self):
        try:
            # Get the Codespace name from environment variable
            codespace_name = os.environ.get('CODESPACE_NAME')
            github_codespaces_port_forwarding_domain = os.environ.get('GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN')
            
            # Construct the Tomcat URL
            if codespace_name and github_codespaces_port_forwarding_domain:
                # Use Codespace URL
                tomcat_url = f"https://{codespace_name}-8080.{github_codespaces_port_forwarding_domain}/vehicle-ecommerce{self.path}"
            else:
                # Fallback to localhost (for local development)
                tomcat_url = f"http://localhost:8080/vehicle-ecommerce{self.path}"
            
            print(f"Forwarding to Tomcat URL: {tomcat_url}")
            
            # Handle POST data if present
            content_length = int(self.headers.get('Content-Length', 0))
            post_data = None
            if content_length > 0:
                post_data = self.rfile.read(content_length)
            
            # Create the request
            if post_data:
                req = urllib.request.Request(tomcat_url, data=post_data)
            else:
                req = urllib.request.Request(tomcat_url)
            
            # Copy headers
            for header, value in self.headers.items():
                if header.lower() not in ['host', 'content-length']:
                    req.add_header(header, value)
            
            # Make the request
            with urllib.request.urlopen(req, timeout=10) as response:
                # Send response status
                self.send_response(response.status)
                
                # Copy response headers
                for header, value in response.headers.items():
                    if header.lower() not in ['server', 'connection']:
                        self.send_header(header, value)
                self.end_headers()
                
                # Copy response body
                response_data = response.read()
                print(f"Response received: {len(response_data)} bytes")
                self.wfile.write(response_data)
                
        except urllib.error.HTTPError as e:
            print(f"HTTP Error forwarding request: {e.code} {e.reason}")
            self.send_error(e.code, f"Backend Error: {e.reason}")
        except urllib.error.URLError as e:
            print(f"URL Error forwarding request: {e}")
            self.send_error(502, f"Bad Gateway: {e}")
        except Exception as e:
            print(f"Error forwarding request: {e}")
            self.send_error(502, f"Bad Gateway: {e}")
    
    def forward_to_tomcat_jsp(self, jsp_path):
        # Special handler for JSP redirects  
        original_path = self.path
        self.path = jsp_path
        self.forward_to_tomcat()
        self.path = original_path

if __name__ == "__main__":
    PORT = 3000
    
    with socketserver.TCPServer(("0.0.0.0", PORT), ProxyHandler) as httpd:
        print(f"Proxy server running on port {PORT}")
        print(f"Static files served from: /workspaces/Vehicle-E-commerce-website/target/webapp")
        print(f"Servlet requests forwarded to: http://localhost:8080/vehicle-ecommerce")
        print(f"Access via: https://friendly-guide-4jqqw7jv4h7p4x-{PORT}.app.github.dev/")
        httpd.serve_forever()
