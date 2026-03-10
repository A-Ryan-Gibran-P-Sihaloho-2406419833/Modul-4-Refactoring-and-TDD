package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Konstanta untuk menghindari PMD literal string warning
    private static final String REDIRECT_LIST = "redirect:/payment/list";
    private static final String SUCCESS_STATUS = "SUCCESS";
    private static final String REJECTED_STATUS = "REJECTED";

    @GetMapping("/list")
    public String paymentListPage(Model model) {
        List<Payment> payments = paymentService.getAllPayments();
        model.addAttribute("payments", payments);
        return "paymentList";
    }

    @PostMapping("/{id}/success")
    public String updatePaymentStatusSuccess(@PathVariable("id") String paymentId) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment != null) {
            paymentService.setStatus(payment, SUCCESS_STATUS);
        }
        return REDIRECT_LIST;
    }

    @PostMapping("/{id}/reject")
    public String updatePaymentStatusReject(@PathVariable("id") String paymentId) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment != null) {
            paymentService.setStatus(payment, REJECTED_STATUS);
        }
        return REDIRECT_LIST;
    }
}