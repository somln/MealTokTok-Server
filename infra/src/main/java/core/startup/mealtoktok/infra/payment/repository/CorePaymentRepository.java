package core.startup.mealtoktok.infra.payment.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import core.startup.mealtoktok.domain.order.OrderId;
import core.startup.mealtoktok.domain.payment.Payment;
import core.startup.mealtoktok.domain.payment.PaymentId;
import core.startup.mealtoktok.domain.payment.PaymentRepository;
import core.startup.mealtoktok.infra.payment.entity.PaymentEntity;
import core.startup.mealtoktok.infra.payment.exception.PaymentNotFoundException;

@Repository
@RequiredArgsConstructor
@Transactional
public class CorePaymentRepository implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(PaymentEntity.from(payment)).toDomain();
    }

    @Override
    public Payment findById(PaymentId paymentId) {
        return getPaymentEntity(paymentId).toDomain();
    }

    @Override
    public Payment findByOrderId(OrderId orderId) {
        return paymentJpaRepository
                .findByOrderId(orderId.getValue())
                .orElseThrow(() -> PaymentNotFoundException.EXCEPTION)
                .toDomain();
    }

    @Override
    public void update(Payment payment) {
        PaymentEntity paymentEntity = getPaymentEntity(payment.getPaymentId());
        paymentEntity.update(payment);
    }

    private PaymentEntity getPaymentEntity(PaymentId paymentId) {
        return paymentJpaRepository
                .findById(paymentId.getValue())
                .orElseThrow(() -> PaymentNotFoundException.EXCEPTION);
    }
}
