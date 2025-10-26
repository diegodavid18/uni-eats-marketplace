package com.remington.unieats.marketplace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import java.util.logging.Logger;

@Service
public class EmailService {

    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.mail.from-name:UniEats Marketplace}")
    private String fromName;

    /**
     * Envía un correo HTML con soporte para múltiples destinatarios
     */
    public boolean enviarEmailHtml(String asunto, String contenidoHtml, String... destinatarios) {
        try {
            if (destinatarios == null || destinatarios.length == 0) {
                logger.warning("No hay destinatarios para enviar el correo");
                return false;
            }

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(destinatarios);
            helper.setSubject(asunto);
            helper.setText(contenidoHtml, true);

            mailSender.send(message);
            logger.info("Correo enviado exitosamente a: " + String.join(", ", destinatarios));
            return true;

        } catch (MessagingException e) {
            logger.severe("Error al enviar correo HTML: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            logger.severe("Error inesperado al enviar correo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Envía un correo de texto plano
     */
    public boolean enviarEmailTexto(String asunto, String contenido, String... destinatarios) {
        try {
            if (destinatarios == null || destinatarios.length == 0) {
                logger.warning("No hay destinatarios para enviar el correo");
                return false;
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(destinatarios);
            message.setSubject(asunto);
            message.setText(contenido);

            mailSender.send(message);
            logger.info("Correo de texto enviado exitosamente a: " + String.join(", ", destinatarios));
            return true;

        } catch (Exception e) {
            logger.severe("Error al enviar correo de texto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Envía correos masivos con plantilla HTML (para campañas CRM)
     */
    public int enviarCampanaMasiva(String asunto, String plantillaHtml, List<String> destinatarios) {
        int enviados = 0;
        for (String destinatario : destinatarios) {
            if (enviarEmailHtml(asunto, plantillaHtml, destinatario)) {
                enviados++;
            }
        }
        logger.info("Campaña masiva completada: " + enviados + " de " + destinatarios.size() + " correos enviados");
        return enviados;
    }

    /**
     * Envía correo de confirmación/notificación
     */
    public boolean enviarNotificacion(String destinatario, String asunto, String contenido) {
        return enviarEmailTexto(asunto, contenido, destinatario);
    }

    /**
     * Obtiene el correo configurado del remitente
     */
    public String getFromEmail() {
        return fromEmail;
    }

    /**
     * Obtiene el nombre configurado del remitente
     */
    public String getFromName() {
        return fromName;
    }
}
