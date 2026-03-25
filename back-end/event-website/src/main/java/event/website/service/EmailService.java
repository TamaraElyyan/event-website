package event.website.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    // Method to send acceptance email with HTML and Google Signature
    public void sendStudentAcceptanceEmail(String studentEmail, String studentName, String eventName) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String imagePath = "C:/Users/hp/IdeaProjects/event-website/src/main/resources/static/pictures/logo.png";
            FileSystemResource logo = new FileSystemResource(new File(imagePath));
            helper.setTo(studentEmail);
            helper.setSubject("Registration Accepted for " + eventName);

            // HTML email content with Google signature
            String htmlContent = "<html>" +
                    "<body>" +
                    "<p>Dear <strong>" + studentName + "</strong>,</p>" +
                    "<p>Your registration for the Training <strong>" + eventName + "</strong> has been successfully accepted.</p>" +
                    "<p>We look forward to your participation!</p>" +
                    "<br>" +
                    "<p>Best regards,</p>" +
                    "<p><strong>The Yalla Shabab Team</strong></p>" +
                    "<hr>" +
                    "<p><strong>📞 Phone:</strong> 0562 667 777 / 0592 385 684</p>" +
                    "<p><strong>📧 Email:</strong> <a href='mailto:yallashabab96@gmail.com'>yallashabab96@gmail.com</a></p>" +
                    "<p><strong>🌐 Website:</strong> Yalla Shabab Website</p>" +
                    "<p><strong>📍 Address:</strong> Palestine, Ramallah</p>" +
                    "<br>" +
                    "<img src='cid:logoImage' width='200' alt='Yalla Shabab Logo'/>" +
//                    "<img src='http://localhost:8080/static/pictures/logo.png' width='200' alt='Yalla Shabab Logo'/>" +  // Reference Content-ID

                    "</body>" +
                    "</html>";
            helper.setText(htmlContent, true);
            helper.addInline("logoImage", logo);

            javaMailSender.send(message);
        } catch (MessagingException | MailException ex) {
            ex.printStackTrace();
        }
    }
}
