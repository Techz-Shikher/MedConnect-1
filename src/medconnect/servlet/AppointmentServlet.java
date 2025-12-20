package medconnect.servlet;

import medconnect.dao.AppointmentDAO;
import medconnect.model.Appointment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

@WebServlet("/bookAppointment")
public class AppointmentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int patientId = Integer.parseInt(request.getParameter("patientId"));
        int doctorId = Integer.parseInt(request.getParameter("doctorId"));

        // Get date & time from request
        String dateStr = request.getParameter("date"); // yyyy-MM-dd
        String timeStr = request.getParameter("time"); // HH:mm

        // Convert to LocalDateTime
        LocalDate date = LocalDate.parse(dateStr);
        LocalTime time = LocalTime.parse(timeStr);
        LocalDateTime appointmentDateTime = LocalDateTime.of(date, time);

        // Create Appointment object
        Appointment appointment = new Appointment();
        appointment.setPatientId(patientId);
        appointment.setDoctorId(doctorId);
        appointment.setAppointmentDate(appointmentDateTime);
        appointment.setStatus("Scheduled");

        // Save using DAO
        AppointmentDAO dao = new AppointmentDAO();
        dao.addAppointment(appointment);

        response.sendRedirect("appointmentSuccess.jsp");
    }
}
