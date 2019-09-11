package application.adapter.data;

import application.entities.Payment;

public class PaymentAdapter {
    private Payment payment;

    public PaymentAdapter() {
    }

    public PaymentAdapter(Payment payment) {
        this.payment = payment;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
