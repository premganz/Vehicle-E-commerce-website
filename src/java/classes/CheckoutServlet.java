/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author kevin
 */
public class CheckoutServlet extends HttpServlet {

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
            out.println("<title>Servlet CheckoutServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CheckoutServlet at " + request.getContextPath() + "</h1>");
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
    
    public void sendMail(String to, String from, String password, String subject, String body, boolean bodyIsHTML) throws MessagingException
    {
        // 1 - get a mail session
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", "smtp.gmail.com");
        props.put("mail.smtps.port", 465);
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtps.quitwait", "false");
        Session session = Session.getDefaultInstance(props);
        session.setDebug(false);	// show more information or not in the output

        // 2 - create a message
        Message message = new MimeMessage(session);
        message.setSubject(subject);
        if (bodyIsHTML)
            message.setContent(body, "text/html");
        else
            message.setText(body);

        // 3 - address the message
        Address fromAddress = new InternetAddress(from);
        Address toAddress = new InternetAddress(to);
        message.setFrom(fromAddress);
        message.setRecipient(Message.RecipientType.TO, toAddress);

        // 4 - send the message
        Transport transport = session.getTransport();
        transport.connect(from, password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
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
            action = "checkOut";  // default action
        }
        if (action.equals("shop")) {
            url = "/index.jsp";    // the "index" page
        } else if (action.equals("checkOut")){
             String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String txtEmail = request.getParameter("txtEmail");
            String address = request.getParameter("address");
            String country = request.getParameter("country");
            String state = request.getParameter("state");
            String zipCode = request.getParameter("zipCode");
            String payment = request.getParameter("paymentMethod");
            String fullPrice = request.getParameter("totalPrice");
            String nameCard = request.getParameter("cardName");
            String cardNumber = request.getParameter("cardNumber");
            String cardExpire = request.getParameter("cardExpire");
            String cardCVV = request.getParameter("cardCVV");

            
            url = "/confirm.jsp";
            
            // call function
            // function(firstName,  lastName, txtEmail, address, country, state, zipCode);

            if (!payment.contains("debit")){

            }
            else {
                payment="debit";
            }

//            GMailTest g = new GMailTest();
            // Provide the following three values.
            String from = "kevintran1142@gmail.com"; // enter your email address at gmail, such as xyz or xyz@gmail.com
            String password = "Kimwipes1@"; // enter your gmail password
            String to = txtEmail; // enter a recipient email address, such as abc@hotmail.com

            String subject = "testing email from GMail";
            String body =                
                            "Billing Information: \n" +
                            "Name: " + firstName + " " + lastName + "\n" +
                            "Email: " + txtEmail + "\n" +
                            "Address: " + address + "\n" +
                            "Country: " + country + "\n" +
                            "State: " + state + "\n" +
                            "Zip Code: " + zipCode + "\n" +
                            "Payment type: " + payment + "\n" +
                            "Total Amount: $" + fullPrice + "0" + "\n" +
                            "Thank you for choosing our product!"
                    
                    ;

            boolean b = false;
               
            try {
                 sendMail(to, from, password, subject, body, b);
            }
            catch(MessagingException e)
            {
                System.out.println(e);
            }
            
            
            
        }
        sc.getRequestDispatcher(url).forward(request, response);
        
       
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
