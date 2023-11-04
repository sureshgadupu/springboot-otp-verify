package dev.fullstackcode.otp.service;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Service;

@Service
public class TotpService {

    private static final int OTP_LENGTH = 6;
    private static final int TIME_STEP = 30;

    public String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return new Base32().encodeToString(bytes);
    }


    public  int generateTotp(String secretKey, int timeOffset) throws NoSuchAlgorithmException,
            InvalidKeyException {
        Instant instant = Instant.now();
        long timeSlice =  (instant.getEpochSecond() / TIME_STEP) + timeOffset;
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(timeSlice);
        byte[] timeBytes = buffer.array();

        byte[] hmacResult = hmacSha1(new Base32().decode(secretKey), timeBytes);

        int offset = hmacResult[hmacResult.length - 1] & 0x0F;
        byte[] codeBytes = Arrays.copyOfRange(hmacResult, offset, offset + 4);
        codeBytes[0] &= 0x7F;

        int code = ByteBuffer.wrap(codeBytes).getInt();
        return code % (int) Math.pow(10, OTP_LENGTH);
    }

    private  byte[] hmacSha1(byte[] key, byte[] timeBytes) throws InvalidKeyException,
            NoSuchAlgorithmException {
        Mac hmac = Mac.getInstance("HmacSHA1");
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        hmac.init(signKey);
        return hmac.doFinal(timeBytes);
    }

    public boolean verify(int inputOtp,String secret) throws NoSuchAlgorithmException, InvalidKeyException {
       return  this.verify(inputOtp,secret, 1, 1);
    }

    public boolean verify(int inputOtp,String secret, int lookAheadOffset,
                          int lookBehindOffset) throws NoSuchAlgorithmException, InvalidKeyException {

        for(int i = lookAheadOffset; i >=  -lookBehindOffset; --i) {
            int generatedOpt = this.generateTotp(secret, i);
            if (generatedOpt == inputOtp) {
                return true;
            }
        }

        return false;

    }
}
