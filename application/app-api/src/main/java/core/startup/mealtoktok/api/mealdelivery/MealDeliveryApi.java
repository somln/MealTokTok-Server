package core.startup.mealtoktok.api.mealdelivery;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import core.startup.mealtoktok.api.mealdelivery.dto.FullDiningResponse;
import core.startup.mealtoktok.api.mealdelivery.dto.MealDeliveryResponse;
import core.startup.mealtoktok.common.annotation.CursorDefault;
import core.startup.mealtoktok.common.dto.Cursor;
import core.startup.mealtoktok.common.dto.Response;
import core.startup.mealtoktok.common.dto.SliceResult;
import core.startup.mealtoktok.domain.mealdelivery.CollectingState;
import core.startup.mealtoktok.domain.mealdelivery.DeliveryState;
import core.startup.mealtoktok.domain.mealdelivery.FullDining;
import core.startup.mealtoktok.domain.mealdelivery.MealDeliverySearchCond;
import core.startup.mealtoktok.domain.mealdelivery.MealDeliveryService;
import core.startup.mealtoktok.domain.mealdelivery.Recipient;
import core.startup.mealtoktok.domain.mealdelivery.TargetFullDining;
import core.startup.mealtoktok.domain.mealdelivery.TargetMealDelivery;
import core.startup.mealtoktok.domain.user.User;

@RequestMapping("/api/v1/meal-deliveries")
@RestController
@RequiredArgsConstructor
public class MealDeliveryApi implements MealDeliveryApiDocs {

    private final MealDeliveryService mealDeliveryService;

    @GetMapping("/delivering")
    public Response<MealDeliveryResponse> deliveringMeal(
            @AuthenticationPrincipal User currentUser) {
        return Response.success(
                MealDeliveryResponse.from(
                        mealDeliveryService.getDeliveringMeal(Recipient.from(currentUser))));
    }

    @GetMapping
    public Response<SliceResult<MealDeliveryResponse>> searchMealDeliveries(
            @AuthenticationPrincipal User currentUser,
            MealDeliverySearchCond cond,
            @CursorDefault Cursor cursor) {
        return Response.success(
                mealDeliveryService
                        .searchMealDeliveries(Recipient.from(currentUser), cond, cursor)
                        .map(MealDeliveryResponse::from));
    }

    @GetMapping("/{mealDeliveryId}")
    public Response<MealDeliveryResponse> mealDelivery(@PathVariable Long mealDeliveryId) {
        return Response.success(
                MealDeliveryResponse.from(
                        mealDeliveryService.getMealDelivery(
                                TargetMealDelivery.from(mealDeliveryId))));
    }

    @GetMapping("/delivered")
    public Response<MealDeliveryResponse> recentDeliveredMeal(
            @AuthenticationPrincipal User currentUser) {
        return Response.success(
                MealDeliveryResponse.from(
                        mealDeliveryService.getRecentDeliveredMeal(Recipient.from(currentUser))));
    }

    @GetMapping("/next-delivery")
    public Response<MealDeliveryResponse> nextDeliveryMeal(@RequestParam Long orderId) {
        return Response.success(
                MealDeliveryResponse.from(mealDeliveryService.getNextDeliveryMeal(orderId)));
    }

    @PatchMapping("/{mealDeliveryId}/{deliveryState}")
    public Response<Void> changeDeliveryState(
            @PathVariable Long mealDeliveryId, @PathVariable DeliveryState deliveryState) {

        switch (deliveryState) {
            case DELIVERY_REQUESTED ->
                    mealDeliveryService.reserveMealDelivery(
                            TargetMealDelivery.from(mealDeliveryId));
            case DELIVERING ->
                    mealDeliveryService.startMealDelivery(TargetMealDelivery.from(mealDeliveryId));
            case DELIVERED ->
                    mealDeliveryService.completeMealDelivery(
                            TargetMealDelivery.from(mealDeliveryId));
        }

        return Response.success("배송 상태 변경 성공");
    }

    @PatchMapping("/full-dinings/{fullDiningId}/{collectingState}")
    public Response<Void> changeCollectingState(
            @PathVariable Long fullDiningId, @PathVariable CollectingState collectingState) {
        mealDeliveryService.changeCollectingState(
                TargetFullDining.from(fullDiningId), collectingState);
        return Response.success("수거 상태 변경 성공");
    }

    @GetMapping("/full-dinings/collect-requested/count")
    public Response<Integer> countCollectRequestContainers(
            @AuthenticationPrincipal User currentUser) {
        return Response.success(
                mealDeliveryService.countCollectRequestContainers(Recipient.from(currentUser)));
    }

    @GetMapping("/full-dinings")
    public Response<List<FullDiningResponse>> fullDinings(
            @AuthenticationPrincipal User currentUser) {
        List<FullDining> fullDinings =
                mealDeliveryService.getFullDinings(Recipient.from(currentUser));
        return Response.success(
                fullDinings.parallelStream().map(FullDiningResponse::from).toList());
    }
}
