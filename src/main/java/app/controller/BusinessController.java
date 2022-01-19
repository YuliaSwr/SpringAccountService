package app.controller;

import app.model.Payment;
import app.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class BusinessController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/api/acct/payments")
    public Map<String, String> savePayments(@Valid @RequestBody List<Payment> listOfPayments) {
        paymentService.saveAll(listOfPayments);
        return Map.of("status", "Added successfully!");
    }

    @PutMapping("/api/acct/payments")
    public Map<String, String> updatePay(@RequestBody @Valid Payment payment) {
        paymentService.updatePayment(payment);
        return Map.of("status", "Updated successfully!");
    }

    @GetMapping("/api/empl/payment")
    public Object getPayment(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam(name = "period", required = false)
                           @DateTimeFormat(pattern = "MM-yyyy") String period) {

        if (period != null) {
            return paymentService.getPaymentByPeriod(userDetails.getUsername(), period);
        } else {
            return paymentService.getAllPayments(userDetails.getUsername());
        }
    }
}
