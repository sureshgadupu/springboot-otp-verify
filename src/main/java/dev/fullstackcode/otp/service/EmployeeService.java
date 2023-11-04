package dev.fullstackcode.otp.service;

import com.google.zxing.WriterException;
import dev.fullstackcode.otp.entity.Employee;
import dev.fullstackcode.otp.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired QRCodeService qrCodeService;

    @Autowired
    TotpService totpService;

    @Autowired
    TotpService2 totpService2;


    public Employee getEmployeeById(Integer id) {
        return employeeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id :" + id));
    }

    public byte[] enableOtp(Integer id) throws WriterException, UnsupportedEncodingException {
      Employee emp =   employeeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id :" + id));
      if(emp.getOtp_secret() != null) {
         throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"TOTP is already enabled for " +
                 "Employee with id :" + id);
      }
        String encodedSecret = totpService.generateSecretKey();
        emp.setOtp_secret(encodedSecret);
        employeeRepository.save(emp);
        String otpUri = String.format("otpauth://totp/%s?secret=%s", URLEncoder.encode(
                        emp.getFirst_name(), StandardCharsets.UTF_8),
                encodedSecret);
        return qrCodeService.generateQRCode(otpUri);

    }

    public boolean verifyOtp(Integer empId, String otp) {
        Employee emp =   employeeRepository.findById(empId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id :" + empId));
        try {
           return  totpService.verify(Integer.parseInt(otp),emp.getOtp_secret());

        } catch (NoSuchAlgorithmException | InvalidKeyException e ) {
            throw new RuntimeException(e);
        }
    }

    public byte[] enableOtp2(Integer id) throws WriterException, UnsupportedEncodingException {
        Employee emp =   employeeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id :" + id));
        if(emp.getOtp_secret() != null) {
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"TOTP is already enabled for " +
                    "Employee with id :" + id);
        }
        String encodedSecret = totpService2.generateSecretKey();
        emp.setOtp_secret(encodedSecret);
        employeeRepository.save(emp);
        return qrCodeService.generateQRCode( totpService2.generateUriForQRCode(encodedSecret,emp.getFirst_name()));

    }

    public boolean verifyOtp2(Integer empId, String otp) {
        Employee emp =   employeeRepository.findById(empId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id :" + empId));
        return  totpService2.verifyTotp(otp,emp.getOtp_secret());

    }

}
