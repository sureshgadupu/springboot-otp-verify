package dev.fullstackcode.otp.controller;


import com.google.zxing.WriterException;
import dev.fullstackcode.otp.entity.Employee;
import dev.fullstackcode.otp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

   public  record OtpVerifyRequest(String otp){};
    @Autowired
    EmployeeService employeeService;

    @GetMapping("{id}")
    public Employee getEmployee(@PathVariable Integer id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping(value = "{empId}/enableOtp",produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] enableOtp(@PathVariable(value="empId") Integer id) throws WriterException, UnsupportedEncodingException {
        return employeeService.enableOtp(id);
    }

    @PostMapping(value = "{empId}/verifyOtp")
    public boolean verifyOtp(@PathVariable(value="empId") Integer empId,
                             @RequestBody OtpVerifyRequest otpRequest)  {
        return employeeService.verifyOtp(empId,otpRequest.otp);
    }

    @PostMapping(value = "{empId}/verifyOtp2")
    public boolean verifyOtp2(@PathVariable(value="empId") Integer empId,
                             @RequestBody OtpVerifyRequest otpRequest)  {
        return employeeService.verifyOtp2(empId,otpRequest.otp);
    }

    @GetMapping(value = "{empId}/enableOtp2",produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] enableOtp2(@PathVariable(value="empId") Integer id) throws WriterException,
            UnsupportedEncodingException {
        return employeeService.enableOtp2(id);
    }
}
