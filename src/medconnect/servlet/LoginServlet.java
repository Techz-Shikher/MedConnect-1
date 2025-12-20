package medconnect.servlet;

import medconnect.dao.PatientDAO;
import medconnect.model.Patient;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        PatientDAO dao = new PatientDAO();
        Patient patient = dao.validateLogin(email, password);

        if (patient != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", patient);
            response.sendRedirect("dashboard.jsp");
        } else {
            response.sendRedirect("loginError.jsp");
        }
    }
}
