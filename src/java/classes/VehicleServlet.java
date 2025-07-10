package classes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;  
import java.io.PrintWriter;  
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;  
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;  
import jakarta.servlet.annotation.WebServlet;  
import jakarta.servlet.http.HttpServlet;  
import jakarta.servlet.http.HttpServletRequest;  
import jakarta.servlet.http.HttpServletResponse;  

/**
 *
 * @author kevin
 */
//@WebServlet("/VehicleServlet")
public class VehicleServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet VehicleServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet VehicleServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        System.out.println("VehicleServlet.doGet() called with params: " + request.getQueryString());
        try {
//            int k = 8;
            int kv2 = Integer.parseInt(getServletContext().getInitParameter("k"));
            response.setContentType("text/html");  
            PrintWriter out=response.getWriter(); 
            
            // Get parameters
            String type;
            String sorting;
            String sortStore;
            
            String vehicleType = request.getParameter("vehicle");
            String spageid=request.getParameter("page"); 
            String sortType = request.getParameter("sort");
            
            
            // specify type
            if(vehicleType != null && vehicleType.equals("b")){
                type = "boat";
            } else if (vehicleType != null && vehicleType.equals("c")) {
                type = "car";
            }
            else {
                type = "motorcycle";
            }
            
            if(sortType != null && sortType.equals("n")) {
                sorting = "";
                sortStore = "n";
            }
            else if (sortType != null && sortType.equals("p")){
                sorting = "ORDER BY price";
                sortStore = "p";
            }
            else {
                sorting = "ORDER BY name";
                sortStore = "nm";
            }
            
            // Load Driver
            Class.forName("org.h2.Driver");

            // Get connection
            String dbURL = "jdbc:h2:/workspaces/Vehicle-E-commerce-website/database/vehicles;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1";
            String username = "sa";
            String password = "";
            Connection connection = DriverManager.getConnection(
                 dbURL, username, password);
            String preparedSQL = "SELECT * FROM vehicle "
                           + "ORDER BY type ASC LIMIT " + kv2 + " OFFSET 0";
            PreparedStatement statement = connection.prepareStatement(preparedSQL);
//                    compare with Statement statement = connection.createStatement()

//               statement.setString(1, vehicles.getName());

//                   Statement statement = connection.createStatement();
////                    int num = 17;
////                    int start;
//                   ResultSet vehicles = statement.executeQuery(
//                        "SELECT * FROM vehicle "
//                                + "ORDER BY type ASC LIMIT 0, 8");

            //======================== Query 2 =========================//
            //======================== Get total amount of data listings =========================//
            // Fixed: COUNT query doesn't need LIMIT, and removed incorrect boat table reference
            PreparedStatement statement2 = connection.prepareStatement("SELECT COUNT(*) FROM vehicle WHERE type = ?");
            statement2.setString(1, type);
            ResultSet counterBoat = statement2.executeQuery();
            // get data listing amount
            counterBoat.next();

            // Gets total amount of entire table
             int totalAmount = counterBoat.getInt(1);
             System.out.println("Number of records in the cricketers_data table: "+totalAmount);
            
            // pagination
            Integer pageid=Integer.parseInt(spageid); 
            Integer pageid2 = Integer.parseInt(spageid);
//            int total=8;  
            if(pageid==1){}  
            else{  
                pageid=pageid-1;  
                pageid=pageid*kv2+1;  
            }  
            
            
            out.print("<body style='display: flex; justify-content: center; align-content: center; flex-direction: column; background: linear-gradient(45deg, #FFF, #A9A9A9 1%, transparent);'>");
            out.print("<h1 style='align-self: center; margin-left: 0.5rem;'>Page No: "+spageid+"</h1>");  
            out.print("<table style='margin-left: auto; margin-right: auto;' border='1' cellpadding='4' width='60%'>");  

            //========================================== SORTING ==================================================//
            // Sorting area
            out.print("<tbody>");
                // Sort by price
                out.print("<tr>");
                    out.print("<div style='display: flex; justify-content: center; align-content: center;'>");
//                            out.print("<form action=\"SortingTime\" method=\"post\">");
//                                out.print("<label for'price'>Price</label>");
//                                out.print("<input type='radio' name='filter' id='price' value='price'>");
                            out.print("<a style='text-decoration: none; padding-left: 1rem; padding-right: 1rem; color: #FFF; background-color: #121212; padding: 0.5rem 1rem; margin: 0.5rem; align-self: center;' href=VehicleServlet?sort=p&vehicle=" + vehicleType + "&page=" + 1 + ">" + "Price" + "</a>");


                        // Sort by name

//                                  out.print("<label for'name'>Name</label>");
//                                  out.print("<input type='radio' name='filter' id='name' value='name'>");
                            out.print("<a style='text-decoration: none; padding-left: 1rem; padding-right: 1rem; color: #FFF; background-color: #121212; padding: 0.5rem 1rem; margin: 0.5rem; align-self: center;' href=VehicleServlet?sort=nm&vehicle=" + vehicleType + "&page=" + 1 + ">" + "Name" + "</a>");
//                                  out.print("<input name=\"submit\" type=\"submit\">");
//                            out.print("</form>");
                    out.print("</div>");
                out.print("</tr>");
            out.print("</tbody>");
 
             out.print("<thead>"); //thead
            out.print("<tr><th>Name</th><th>Price</th><th>Type</th><th>Image</th><th>Add to carts</th></tr>");  
//                    for(Emp e:list){  
//                        out.print("<tr><td>"+e.getName()+"</td><td>"+e.getPrice()+"</td><td>"+e.getImage()+"</td></tr>");  
//                    }  
            out.print("<tbody>");

            // display information
            for (int i = 0; i < 1; i++) {
                RepeatBoat(pageid, kv2, response, type, sorting);    
            }
            
             out.print("</tbody>");

            int kv3 = 8;
            out.print("<tbody>");
            out.print("<tr>");
                    out.print("<div style='display: flex; justify-content: center; align-content: center;'>");
//                        int k = 8;
                        for (int i = 1; i <= (totalAmount/kv2) + 1; i++) {
                           // if vehicle=b&sort=nm&page=2
                            if (pageid2.equals(i)){ // if 1 = 1
                                out.print("<p style='padding-left: 1rem; color: #121212;  align-self: center; text-decoration: none;'>" + i + "</p>");
                            }
                            
                            else if(!pageid2.equals(i)) {
                                out.print("<a style='padding-left: 1rem; color: #121212;  align-self: center;' href=\"VehicleServlet?sort=" + sortType + "&vehicle=" + vehicleType + "&page=" + i + "\">" + i + "</a>");
                            }
                            
                        }
                    out.print("</div>");
                out.print("</tr>");
            out.print("</tbody>");

            out.print("</table>");  
            out.print("</body>");
        out.close();
        }
        catch(SQLException e){
            System.err.println("SQL Exception in VehicleServlet: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VehicleServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // call sql query and their data
     public static void RepeatBoat(int start, int total, HttpServletResponse response, String type, String sorting) throws IOException {
        try {
            PrintWriter out=response.getWriter(); 
            Connection con=getConnection();  
            // H2 syntax: LIMIT count OFFSET offset (not MySQL LIMIT offset,count)
            String sql = "select * from vehicle where type = '" + type + "' " + sorting + " limit " + total + " offset " + (start-1);
            PreparedStatement ps=con.prepareStatement(sql);  
            ResultSet rs=ps.executeQuery();  
            while(rs.next()) {
                out.print("<tr style='align-self: center;'><th>" + rs.getString("name") + "</th><th>" + rs.getString("price") + "</th><th>" + rs.getString("type") +  "</th><th>" + "<img style='height: 200px; width: 200px;' src='image/" + rs.getString("image") + ".jpg'>"  + "</th>");
                    out.print("<th>");
                        out.print("<form action='cart' method='post' class='form' style='align-self: center; display: flex; justify-content: flex-start; align-content: center;'>");  
                                out.print("<div style='align-self: center; margin-left: 7rem;'>");
                                    out.print("<h2>Quantity:</h2>");
                                    out.print("<input type='text' name='quantity' style='background-color: #FFF; outline: none;'>"); // submit quantity
                                    out.print("<input name='submit' type='submit'>");
                                    out.print("<input type='hidden' name='productId' value='" + rs.getInt("idvehicle") + "'>"); // submits price
                                    out.print("<input type='hidden' name='price' value='" + rs.getString("price") + "'>"); // submits price
                                    out.print("<input type='hidden' name='name' value='" + rs.getString("name") + "'>"); // submits name
                                out.print("</div>"); 
                        out.print("</form>");
                    out.print("</th>");
                out.print("</tr>");
            }
        } catch(SQLException e){
            System.err.println("SQL Exception in RepeatBoat: " + e.getMessage());
            e.printStackTrace();
        } 
     }
     
     // Get connection to sql database
     public static Connection getConnection(){  
        Connection con=null;  
        try{  
            Class.forName("org.h2.Driver");  
            con=DriverManager.getConnection("jdbc:h2:/workspaces/Vehicle-E-commerce-website/database/vehicles;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1","sa","");  
        }catch(Exception e){System.out.println(e);}  
        return con;  
    }  
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
