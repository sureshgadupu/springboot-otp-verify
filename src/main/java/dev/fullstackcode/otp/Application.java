package dev.fullstackcode.otp;

import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	@Bean
	public QRCodeWriter getQrCodeWriter(){
		return new QRCodeWriter();
	}


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
