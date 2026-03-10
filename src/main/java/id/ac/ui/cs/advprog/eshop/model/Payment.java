package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import java.util.Map;

@Getter
public class Payment {
    String id;
    Order order;
    String method;
    String status;
    Map<String, String> paymentData;

    // Ekstraksi string literal menjadi konstan
    private static final String VOUCHER = "VOUCHER";
    private static final String COD = "COD";
    private static final String SUCCESS = "SUCCESS";
    private static final String REJECTED = "REJECTED";

    public Payment(String id, Order order, String method, Map<String, String> paymentData) {
        this.id = id;
        this.order = order;
        this.method = method;
        this.paymentData = paymentData;

        if (VOUCHER.equals(method)) {
            String voucherCode = paymentData.get("voucherCode");
            if (voucherCode != null &&
                    voucherCode.length() == 16 &&
                    voucherCode.startsWith("ESHOP") &&
                    countNumerics(voucherCode) == 8) {
                this.status = SUCCESS;
            } else {
                this.status = REJECTED;
            }
        } else if (COD.equals(method)) {
            String address = paymentData.get("address");
            String deliveryFee = paymentData.get("deliveryFee");

            if (address == null || address.trim().isEmpty() ||
                    deliveryFee == null || deliveryFee.trim().isEmpty()) {
                this.status = REJECTED;
            } else {
                this.status = SUCCESS;
            }
        } else {
            this.status = REJECTED;
        }
    }

    public Payment(String id, Order order, String method, String status, Map<String, String> paymentData) {
        this.id = id;
        this.order = order;
        this.method = method;
        this.status = status;
        this.paymentData = paymentData;
    }

    private int countNumerics(String str) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                count++;
            }
        }
        return count;
    }
}