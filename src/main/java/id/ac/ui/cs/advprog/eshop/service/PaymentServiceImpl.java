package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    // Ekstraksi string literal menjadi konstan
    private static final String SUCCESS_STATUS = "SUCCESS";
    private static final String REJECTED_STATUS = "REJECTED";
    private static final String FAILED_STATUS = "FAILED";

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = new Payment(UUID.randomUUID().toString(), order, method, paymentData);
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        Payment existingPayment = paymentRepository.findById(payment.getId());

        if (existingPayment != null) {
            Payment newPayment = new Payment(existingPayment.getId(), existingPayment.getOrder(),
                    existingPayment.getMethod(), status, existingPayment.getPaymentData());

            if (SUCCESS_STATUS.equals(status)) {
                newPayment.getOrder().setStatus(SUCCESS_STATUS);
            } else if (REJECTED_STATUS.equals(status)) {
                newPayment.getOrder().setStatus(FAILED_STATUS);
            }

            paymentRepository.save(newPayment);
            return newPayment;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}