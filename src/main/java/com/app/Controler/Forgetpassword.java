package com.app.Controler;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Dto.Resetpasswordto;
import com.app.Entity.Passwordotpreset;
import com.app.Entity.USER;
import com.app.Repositry.Forgetpassworrepo;
import com.app.Repositry.Userreop;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/forget")
public class Forgetpassword {

	@Autowired
	JavaMailSender mailsender;

	@Autowired
	Forgetpassworrepo otpRepo;

	@Autowired
	Userreop userrepo;

	@Autowired
	PasswordEncoder encoder;

	@PostMapping("/otp")
	public String sendotplink(@RequestBody Passwordotpreset request) {

		String email = request.getEmail();
		Optional<USER> user = userrepo.findByEmail(email);

		if (user.isEmpty()) {

			return "User not found";
		}

		String otp = String.valueOf(new Random().nextInt(900000) + 100000);
		Passwordotpreset obj = otpRepo.findByEmail(email);

		if (obj == null) {
			obj = new Passwordotpreset();
		}
		obj.setEmail(email);
		obj.setOtp(otp);
		obj.setExpiryTime(LocalDateTime.now().plusMinutes(10));

		otpRepo.save(obj);

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(email);
		msg.setSubject("Password reset  Otp");
		msg.setText("your otp is " + otp);
		mailsender.send(msg);
		return "otp send";

	}

	@PostMapping("/resetpassword")
	public String resetpassword(@Valid @RequestBody Resetpasswordto dto, BindingResult result) {

		if (result.hasErrors()) {
			return result.getFieldError().getDefaultMessage();
		}

		Passwordotpreset otp = otpRepo.findByEmail(dto.getEmail());

		if (!dto.getOtp().equals(otp.getOtp())) {

			return "wrong otp";
		}

		else if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {

			return "otp expired";
		}

		USER user = userrepo.findByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
		user.setPassword(encoder.encode(dto.getNewPassword()));
		userrepo.save(user);
		otpRepo.delete(otp);

		return "password updated sucesfully";

	}

}
