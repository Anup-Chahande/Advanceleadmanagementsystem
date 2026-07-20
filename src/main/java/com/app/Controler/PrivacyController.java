package com.app.Controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrivacyController {

    @GetMapping(value = "/privacy-policy", produces = "text/html")
    public String privacyPolicy() {
        return """
        <html>
        <head><title>Privacy Policy</title></head>
        <body>
            <h1>Lead Management CRM Privacy Policy</h1>

            <p>Lead Management CRM collects information such as your name, phone number,
            email address, city, budget, and property requirements.</p>

            <p>This information is used only to respond to your property inquiry,
            recommend suitable properties, and manage your request through our CRM.</p>

            <p>We do not sell or share your personal information with third parties,
            except where required by law.</p>

            <p>Email: support@leadmanagementcrm.com</p>
        </body>
        </html>
        """;
    }
}