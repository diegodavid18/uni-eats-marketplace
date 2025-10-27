package com.remington.unieats.marketplace.service;

import com.remington.unieats.marketplace.dto.EstudianteRegistroDTO;
import com.remington.unieats.marketplace.model.entity.Rol;
import com.remington.unieats.marketplace.model.entity.Usuario;
import com.remington.unieats.marketplace.model.repository.RolRepository;
import com.remington.unieats.marketplace.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;

    @Override
    public Usuario registrarEstudiante(EstudianteRegistroDTO registroDTO) {
        // Validar que el correo no exista
        if (usuarioRepository.findByCorreo(registroDTO.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya está en uso.");
        }

        // Crear el nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(registroDTO.getNombre());
        usuario.setApellido(registroDTO.getApellido());
        usuario.setCedula(registroDTO.getCedula());
        usuario.setTelefono(registroDTO.getTelefono());
        usuario.setCorreo(registroDTO.getCorreo());
        usuario.setContrasenaHash(passwordEncoder.encode(registroDTO.getPassword()));

        // Asignar el rol de ESTUDIANTE
        Rol rolEstudiante = rolRepository.findByNombre("ESTUDIANTE")
                .orElseThrow(() -> new RuntimeException("Error: El rol de ESTUDIANTE no se encuentra en la base de datos."));
        usuario.setRoles(Set.of(rolEstudiante));

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        // Enviar correo de bienvenida
        try {
            enviarCorreoBienvenida(usuarioGuardado);
        } catch (Exception e) {
            System.err.println("⚠️ Error al enviar correo de bienvenida: " + e.getMessage());
            // No lanzar excepción para no bloquear el registro
        }
        
        return usuarioGuardado;
    }
    
    private void enviarCorreoBienvenida(Usuario usuario) {
        String nombre = usuario.getNombre();
        String correo = usuario.getCorreo();
        
        String contenidoHtml = generarPlantillaBienvenida(nombre);
        emailService.enviarEmailHtml("¡Bienvenido a Uni-Eats! 🎉", contenidoHtml, correo);
    }
    
    private String generarPlantillaBienvenida(String nombre) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>¡Bienvenido a Uni-Eats!</title>\n" +
                "    <style>\n" +
                "        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f5f5f5; margin: 0; padding: 20px; }\n" +
                "        .container { max-width: 600px; margin: 0 auto; background-color: white; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }\n" +
                "        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 40px 20px; text-align: center; color: white; }\n" +
                "        .header h1 { margin: 0; font-size: 28px; font-weight: bold; }\n" +
                "        .header p { margin: 10px 0 0 0; font-size: 16px; opacity: 0.9; }\n" +
                "        .logo { font-size: 48px; margin-bottom: 15px; }\n" +
                "        .content { padding: 40px; }\n" +
                "        .greeting { font-size: 18px; color: #333; margin-bottom: 20px; }\n" +
                "        .greeting strong { color: #667eea; }\n" +
                "        .features { margin: 30px 0; }\n" +
                "        .feature-item { display: flex; align-items: center; margin: 15px 0; padding: 15px; background-color: #f9f9f9; border-left: 4px solid #667eea; border-radius: 4px; }\n" +
                "        .feature-icon { font-size: 24px; margin-right: 15px; }\n" +
                "        .feature-text { color: #555; }\n" +
                "        .feature-text strong { color: #333; display: block; margin-bottom: 5px; }\n" +
                "        .cta-button { display: inline-block; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 12px 30px; border-radius: 6px; text-decoration: none; font-weight: bold; margin: 20px 0; text-align: center; }\n" +
                "        .cta-button:hover { opacity: 0.9; }\n" +
                "        .benefits { background-color: #f0f4ff; padding: 20px; border-radius: 8px; margin: 20px 0; }\n" +
                "        .benefits h3 { color: #667eea; margin-top: 0; }\n" +
                "        .benefits ul { margin: 10px 0; padding-left: 20px; color: #555; }\n" +
                "        .benefits li { margin: 8px 0; }\n" +
                "        .footer { background-color: #f5f5f5; padding: 20px; text-align: center; font-size: 12px; color: #888; border-top: 1px solid #e0e0e0; }\n" +
                "        .footer a { color: #667eea; text-decoration: none; }\n" +
                "        .divider { height: 1px; background-color: #e0e0e0; margin: 20px 0; }\n" +
                "        .security-note { background-color: #fff3cd; padding: 15px; border-left: 4px solid #ffc107; border-radius: 4px; font-size: 14px; color: #856404; margin: 20px 0; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <!-- Header -->\n" +
                "        <div class=\"header\">\n" +
                "            <div class=\"logo\">🍔</div>\n" +
                "            <h1>¡Bienvenido a Uni-Eats!</h1>\n" +
                "            <p>Tu marketplace de comida universitario</p>\n" +
                "        </div>\n" +
                "\n" +
                "        <!-- Contenido -->\n" +
                "        <div class=\"content\">\n" +
                "            <div class=\"greeting\">\n" +
                "                ¡Hola <strong>" + nombre + "</strong>! 👋\n" +
                "            </div>\n" +
                "\n" +
                "            <p style=\"color: #555; font-size: 15px; line-height: 1.6;\">\n" +
                "                Nos complace confirmarte que tu cuenta ha sido creada exitosamente en Uni-Eats. ¡Ya eres parte de nuestra comunidad!\n" +
                "            </p>\n" +
                "\n" +
                "            <!-- Característica -->\n" +
                "            <div class=\"features\">\n" +
                "                <div class=\"feature-item\">\n" +
                "                    <div class=\"feature-icon\">🍕</div>\n" +
                "                    <div class=\"feature-text\">\n" +
                "                        <strong>Explora Tiendas</strong>\n" +
                "                        Descubre una variedad de tiendas con comida deliciosa a precios de estudiante\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "\n" +
                "                <div class=\"feature-item\">\n" +
                "                    <div class=\"feature-icon\">⚡</div>\n" +
                "                    <div class=\"feature-text\">\n" +
                "                        <strong>Órdenes Rápidas</strong>\n" +
                "                        Pide tu comida favorita con solo unos clics\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "\n" +
                "                <div class=\"feature-item\">\n" +
                "                    <div class=\"feature-icon\">⭐</div>\n" +
                "                    <div class=\"feature-text\">\n" +
                "                        <strong>Gana Puntos</strong>\n" +
                "                        Acumula puntos de lealtad y disfruta de descuentos exclusivos\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "\n" +
                "                <div class=\"feature-item\">\n" +
                "                    <div class=\"feature-icon\">🎁</div>\n" +
                "                    <div class=\"feature-text\">\n" +
                "                        <strong>Cupones Especiales</strong>\n" +
                "                        Acceso a ofertas y promociones solo para nuestros usuarios\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "\n" +
                "            <div class=\"divider\"></div>\n" +
                "\n" +
                "            <!-- Beneficios -->\n" +
                "            <div class=\"benefits\">\n" +
                "                <h3>¿Por qué elegir Uni-Eats?</h3>\n" +
                "                <ul>\n" +
                "                    <li>✓ Comida de calidad a precios accesibles</li>\n" +
                "                    <li>✓ Entrega rápida dentro del campus</li>\n" +
                "                    <li>✓ Múltiples formas de pago</li>\n" +
                "                    <li>✓ Excelente atención al cliente 24/7</li>\n" +
                "                    <li>✓ Programa de lealtad con recompensas</li>\n" +
                "                </ul>\n" +
                "            </div>\n" +
                "\n" +
                "            <!-- Nota de seguridad -->\n" +
                "            <div class=\"security-note\">\n" +
                "                <strong>⚠️ Nota de Seguridad:</strong> Nunca compartimos tu contraseña. Si recibiste correos sospechosos o necesitas ayuda, contacta al equipo de soporte.\n" +
                "            </div>\n" +
                "\n" +
                "            <!-- Botón CTA -->\n" +
                "            <p style=\"text-align: center;\">\n" +
                "                <a href=\"http://localhost:8092/login\" class=\"cta-button\">Comenzar a Pedir Ahora</a>\n" +
                "            </p>\n" +
                "\n" +
                "            <p style=\"color: #999; font-size: 13px; text-align: center; margin-top: 20px;\">\n" +
                "                Si el botón anterior no funciona, copia y pega este enlace en tu navegador:<br>\n" +
                "                <a href=\"http://localhost:8092/login\" style=\"color: #667eea;\">http://localhost:8092/login</a>\n" +
                "            </p>\n" +
                "        </div>\n" +
                "\n" +
                "        <!-- Footer -->\n" +
                "        <div class=\"footer\">\n" +
                "            <p style=\"margin: 0 0 10px 0;\">\n" +
                "                © 2025 Uni-Eats. Todos los derechos reservados.\n" +
                "            </p>\n" +
                "            <p style=\"margin: 0;\">\n" +
                "                ¿Preguntas? <a href=\"http://localhost:8092/contacto\">Contáctanos aquí</a>\n" +
                "            </p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}