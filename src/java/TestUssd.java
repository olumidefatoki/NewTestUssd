/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author olumidefatoki
 */
@WebServlet(urlPatterns = {"/TestUssd"})
public class TestUssd extends HttpServlet {
    String RESPONSE_1= "Welcome to Buy Airtime Menu ";
    String RESPONSE_2= "Welcome to Send Money Menu ";


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
            DBCon myDBUtils = new DBCon();
            JSONObject ussdResponse, ussdRequest;
            String resp = "", endofsession = "false",code="";
            int CurrentStep;
            String msisdn = request.getParameter("msisdn");
            String userdata = request.getParameter("userdata");
            String sessionid = request.getParameter("sessionid");
            String network = request.getParameter("network");
            String platformid = request.getParameter("platformid");
            String session = myDBUtils.getSessionData(sessionid, msisdn);
            if (session != null) {
                JSONObject userRequest = new JSONObject(session);
                ussdRequest = new JSONObject();
                String Menu = userRequest.getString("Menu");
                if (Menu.equals("0")) {
                    Menu = userdata;
                }
                CurrentStep = userRequest.getInt("CurrentStep");
                code = userRequest.getString("Code");
                if (Menu.equals("1")) {
                    switch (CurrentStep) {
                        case 0: {
                            //recipientType Menu
                            ussdRequest.put("Menu", "1");
                            ussdRequest.put("Code", code);
                            ussdRequest.put("CurrentStep", 1);
                            myDBUtils.UpdateRequest(ussdRequest.toString(),sessionid);
                            endofsession="true";
                            resp = RESPONSE_1;
                            break;
                        }
                    }
                }else if (Menu.equals("2")) {
                    switch (CurrentStep) {
                        case 0: {
                            //recipientType Menu
                            ussdRequest.put("Menu", "1");
                            ussdRequest.put("Code", code);
                            ussdRequest.put("CurrentStep", 1);
                            myDBUtils.UpdateRequest(ussdRequest.toString(),sessionid);
                            endofsession="true";
                            resp =RESPONSE_2;
                            break;
                        }
                    }
                }
                else{
                ussdRequest = new JSONObject();
                ussdRequest.put("Menu", "0");
                ussdRequest.put("CurrentStep", "0");
                ussdRequest.put("Code", userdata);
                resp = showMenu();
                }

            } else {
                ussdRequest = new JSONObject();
                ussdRequest.put("Menu", "0");
                ussdRequest.put("CurrentStep", "0");
                ussdRequest.put("Code", userdata);
                myDBUtils.insertUssdRequest(sessionid, ussdRequest.toString(), msisdn, network);
                resp = showMenu();
            }

            if (endofsession.equals("true")) {
                System.out.println("delete");
                myDBUtils.deleteRequest(sessionid);
            }
            ussdResponse = new JSONObject();
            ussdResponse.put("msisdn", msisdn);
            ussdResponse.put("userdata", resp);
            ussdResponse.put("sessionid", sessionid);
            ussdResponse.put("network", network);
            ussdResponse.put("platformid", platformid);
            ussdResponse.put("endofsession", endofsession);
            ussdResponse.put("type", "Release");
            out.println(ussdResponse);

        } catch (Exception ex) {
          ex.printStackTrace();
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
        processRequest(request, response);
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

    public static String showMenu() {
        String menu = "Welcome to My Ussd\n"
                + "1) Buy Airtime\n"
                + "2) Send Money\n";
        return menu;
    }
    
}
