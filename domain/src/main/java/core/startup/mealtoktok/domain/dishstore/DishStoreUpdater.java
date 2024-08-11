package core.startup.mealtoktok.domain.dishstore;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DishStoreUpdater {

    private final DishStoreValidator dishStoreValidator;
    private final DishStoreRepository dishStoreRepository;

    public void update(TargetDishStore targetDishStore, DishStoreInfo dishStoreInfo) {
        dishStoreValidator.validate(dishStoreInfo.storeName());
        dishStoreRepository.update(targetDishStore, dishStoreInfo);
    }
}
