package core.startup.mealtoktok.domain.mealdelivery;

import java.time.LocalDateTime;
import java.util.List;

import core.startup.mealtoktok.common.dto.Cursor;
import core.startup.mealtoktok.common.dto.SliceResult;
import core.startup.mealtoktok.domain.order.OrderId;

public interface MealDeliveryRepository {

    void update(MealDelivery mealDelivery);

    List<MealDelivery> saveAll(List<MealDelivery> mealDeliveries);

    List<MealDelivery> findAllByOrderId(OrderId orderId);

    MealDelivery findByOrdererAndDeliveryState(Recipient recipient, DeliveryState deliveryState);

    MealDelivery findByDeliveryStateAndTime(
            Recipient recipient,
            DeliveryState deliveryState,
            LocalDateTime startTime,
            LocalDateTime endTime);

    MealDelivery findById(MealDeliveryId mealDeliveryId);

    SliceResult<MealDelivery> findByCondition(
            Recipient recipient, MealDeliverySearchCond cond, Cursor cursor);

    Integer countByDeliveryState(
            Recipient recipient,
            DeliveryState deliveryState,
            LocalDateTime startTime,
            LocalDateTime endTime);
}
