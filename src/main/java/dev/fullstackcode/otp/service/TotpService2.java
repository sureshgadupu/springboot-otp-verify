package dev.fullstackcode.otp.service;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import dev.uni_hamburg.security.otp.TimeDriftTotp;
import dev.uni_hamburg.security.otp.api.Base32;
import org.springframework.stereotype.Service;

@Service
public class TotpService2 {

    public String generateSecretKey() {
        return Base32.random();
    }

    public String generateUriForQRCode(String secretKey,String name) {
        TimeDriftTotp totp = new TimeDriftTotp(secretKey);
        return totp.uri(name);
    }

    public String generateTotp(String secretKey) {
        TimeDriftTotp totp = new TimeDriftTotp(secretKey);
        totp.uri("suresh");
        return totp.now();
    }

    public boolean verifyTotp( String totp,String secretKey) {
        TimeDriftTotp validator = new TimeDriftTotp(secretKey,1,1);
        return validator.verify(totp);
    }
}
