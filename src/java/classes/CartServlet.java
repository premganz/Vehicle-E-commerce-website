package classes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
        
            response.setContentType("text/html");  
            PrintWriter out=response.getWriter();
        
            String url = "/index.jsp";
            ServletContext sc = getServletContext();
            
             // get current action
            String action = request.getParameter("action");
            if (action == null) {
                action = "cart";  // default action
            }
            
            // perform action and set URL to appropriate page
            if (action.equals("shop")) {
                url = "/index.jsp";    // the "index" page
            } 
            else if (action.equals("cart")) { 
                String quantityString = request.getParameter("quantity"); // check
                String price = request.getParameter("price");
                String nameTag = request.getParameter("name");
                String productCode = request.getParameter("productId"); // check
                
                
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
                
                String path = sc.getRealPath("/WEB-INF/products.txt");
                Product product = ProductIO.getProduct(productCode, path);
                
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
                
//                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);    
//                dispatcher.forward(request, response);
                

                
                
                
            }
            else if (action.equals("checkout")) {
                url = "/checkout.jsp";
                String fullName = request.getParameter("totalName");
        
                MoneyCarrier mc = new MoneyCarrier();
                mc.setFullPrice(fullName);
                request.setAttribute("mc", mc);
                
                
                
            }
//            sc.getRequestDispatcher(url).forward(request, response);
//            response.setContentType("text/html"); 
//        
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
