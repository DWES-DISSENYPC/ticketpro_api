package com.ticketpro.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
public void enviarCorreoHTML(String emailDestino, String token) {
    try {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        helper.setTo(emailDestino);
        helper.setSubject("Restablece tu contraseña de TicketPro");
        
        String htmlBody = " <div style='font-family: Arial; border: 1px solid #ddd; padding: 20px;'>" +
                          " <h2 style='color: #2c3e50;'>TicketPro</h2>" +
                          " <p>Haz clic en el botón para cambiar tu contraseña:</p>" +
                          " <a href='http://localhost:4200/reset-password?token=" + token + "' " +
                          " style='background: #e67e22; color: white; padding: 10px 20px; text-decoration: none;'>Restablecer Contraseña</a>" +
                          " </div>";
        
        helper.setText(htmlBody, true);
        mailSender.send(message);
    } catch (MessagingException e) {
        throw new RuntimeException("Error enviando el correo");
    }
}

}
