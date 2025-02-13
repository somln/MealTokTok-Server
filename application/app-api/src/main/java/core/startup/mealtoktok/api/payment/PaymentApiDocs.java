package core.startup.mealtoktok.api.payment;

import core.startup.mealtoktok.api.global.dto.Response;
import core.startup.mealtoktok.api.payment.dto.PaymentFailReason;
import core.startup.mealtoktok.api.payment.dto.PaymentRequest;
import core.startup.mealtoktok.api.payment.dto.PaymentResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "결제 API")
public interface PaymentApiDocs {

    @Operation(summary = "인증 성공 시")
    Response<PaymentResponse> pay(PaymentRequest request);

    @Operation(summary = "인증 실패 시")
    Response<Void> fail(PaymentFailReason paymentFailReason);
}
