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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author kevin
 */
public class CartServlet extends HttpServlet {

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
            out.println("<title>Servlet CartServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CartServlet at " + request.getContextPath() + "</h1>");
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
        doPost(request, response);
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
        
            System.out.println("CartServlet.doPost() called");
            response.setContentType("text/html");  
            PrintWriter out=response.getWriter();
        
            String url = "/index.jsp";
            ServletContext sc = getServletContext();
            
             // get current action
            String action = request.getParameter("action");
            if (action == null) {
                action = "cart";  // default action
            }
            System.out.println("Cart action: " + action);
            
            // perform action and set URL to appropriate page
            if (action.equals("shop")) {
                url = "/index.jsp";    // the "index" page
            } 
            else if (action.equals("cart")) { 
                String quantityString = request.getParameter("quantity"); // check
                String price = request.getParameter("price");
                String nameTag = request.getParameter("name");
                String productCode = request.getParameter("productId"); // check
                
                System.out.println("Cart parameters - productId: " + productCode + ", quantity: " + quantityString + ", price: " + price + ", name: " + nameTag);
                
                
//                int codeConverted = Integer.parseInt(productCode);
//                double priceMain = Double.parseDouble(price);
              
                HttpSession session = request.getSession();
                Cart cart = (Cart) session.getAttribute("cart");
                if (cart == null) {
                    cart = new Cart();
                }
                
                //if the user enters a negative or invalid quantity,
                //the quantity is automatically reset to 1.
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityString);
                    if (quantity < 0) {
                        quantity = 1;
                    }
                } catch (NumberFormatException nfe) {
                    quantity = 1;
                }
                
                // Get product from H2 database instead of products.txt file
                Product product = null;
                if (productCode != null) {
                    try {
                        Class.forName("org.h2.Driver");
                        String dbURL = "jdbc:h2:/workspaces/Vehicle-E-commerce-website/database/vehicles;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1";
                        Connection connection = DriverManager.getConnection(dbURL, "sa", "");
                        
                        String sql = "SELECT * FROM vehicle WHERE idvehicle = ?";
                        PreparedStatement stmt = connection.prepareStatement(sql);
                        stmt.setInt(1, Integer.parseInt(productCode));
                        ResultSet rs = stmt.executeQuery();
                        
                        if (rs.next()) {
                            product = new Product();
                            product.setCode(productCode);
                            product.setDescription(rs.getString("name"));
                            product.setPrice(rs.getDouble("price"));
                        }
                        
                        connection.close();
                    } catch (ClassNotFoundException | SQLException | NumberFormatException e) {
                        System.err.println("Error getting product from database: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                
                if (product == null) {
                    // Create a default product if database lookup fails
                    product = new Product();
                    product.setCode(productCode != null ? productCode : "unknown");
                    product.setDescription(nameTag != null ? nameTag : "Unknown Product");
                    product.setPrice(price != null ? Double.parseDouble(price) : 0.0);
                }
                
                LineItem lineItem = new LineItem();
                lineItem.setProduct(product);
                lineItem.setQuantity(quantity);
                if (quantity > 0) {
                    cart.addItem(lineItem);
                } else if (quantity == 0) {
                    cart.removeItem(lineItem);
                }

//                ArrayList <Storage> cartList = new ArrayList<>();
//                
//                Storage storage = new Storage();
//                
//                storage.setName(nameTag);
//                storage.setQty(quantityString);
//                storage.setPrice(priceMain);
//                storage.setIdvehicle(codeConverted);
//                cartList.add(storage);
                
                
               
                
//                request.setAttribute("cartList", cartList);
//                request.setAttribute("storage", storage);
//                
//                HttpSession session = request.getSession(false);
//                
//                if (session == null){
//                    session = request.getSession();
//                    session.setAttribute("cart", cartList);
//                }
//                else {
//                    out.println("already created");
//                }
                
//                session.setAttribute()
//                
                session.setAttribute("cart", cart);
                url = "/cart.jsp";
            }
            else if (action.equals("checkout")) {
                url = "/checkout.jsp";
                String fullName = request.getParameter("totalName");
        
                MoneyCarrier mc = new MoneyCarrier();
                mc.setFullPrice(fullName);
                request.setAttribute("mc", mc);
            }
            
            // Forward to the appropriate JSP page
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
            dispatcher.forward(request, response);
            
           
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
