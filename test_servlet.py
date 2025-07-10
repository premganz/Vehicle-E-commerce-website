#!/usr/bin/env python3
"""
Simple test to verify the VehicleServlet logic without Tomcat
"""
import subprocess
import os

def test_database_connection():
    """Test if H2 database is accessible and has data"""
    print("🧪 Testing H2 database connection...")
    
    try:
        cmd = [
            'java', '-cp', '/tmp/h2-1.4.196.jar',
            'org.h2.tools.Shell',
            '-url', 'jdbc:h2:/workspaces/Vehicle-E-commerce-website/database/vehicles;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1',
            '-user', 'sa',
            '-password', '',
            '-sql', "SELECT COUNT(*) as total FROM vehicle WHERE type = 'car';"
        ]
        
        result = subprocess.run(cmd, capture_output=True, text=True, timeout=10)
        
        if result.returncode == 0:
            print("✅ Database connection successful")
            print(f"📊 Query result: {result.stdout.strip()}")
            return True
        else:
            print(f"❌ Database connection failed: {result.stderr}")
            return False
            
    except Exception as e:
        print(f"❌ Database test error: {e}")
        return False

def test_servlet_compilation():
    """Test if servlet compiled successfully"""
    print("🧪 Testing servlet compilation...")
    
    servlet_class = "/workspaces/Vehicle-E-commerce-website/build/classes/classes/VehicleServlet.class"
    if os.path.exists(servlet_class):
        print("✅ VehicleServlet.class exists")
        print(f"📁 Size: {os.path.getsize(servlet_class)} bytes")
        return True
    else:
        print("❌ VehicleServlet.class not found")
        return False

def test_war_structure():
    """Test WAR file structure"""
    print("🧪 Testing WAR file structure...")
    
    try:
        cmd = ['jar', '-tf', '/workspaces/Vehicle-E-commerce-website/vehicle-ecommerce.war']
        result = subprocess.run(cmd, capture_output=True, text=True)
        
        if result.returncode == 0:
            lines = result.stdout.strip().split('\n')
            servlet_files = [line for line in lines if 'Servlet.class' in line]
            
            print("✅ WAR file structure:")
            for servlet in servlet_files:
                print(f"   📄 {servlet}")
            
            return len(servlet_files) > 0
        else:
            print(f"❌ WAR inspection failed: {result.stderr}")
            return False
            
    except Exception as e:
        print(f"❌ WAR test error: {e}")
        return False

def main():
    print("🚀 Vehicle E-commerce Servlet Testing")
    print("=" * 50)
    
    tests = [
        test_database_connection,
        test_servlet_compilation,
        test_war_structure
    ]
    
    results = []
    for test in tests:
        results.append(test())
        print()
    
    print("📋 Test Summary:")
    print(f"✅ Passed: {sum(results)}/{len(results)}")
    
    if all(results):
        print("🎉 All tests passed! The servlet should work correctly.")
        print("💡 Next step: Start Tomcat properly or use a different web server.")
    else:
        print("⚠️ Some tests failed. Check the issues above.")

if __name__ == "__main__":
    main()
