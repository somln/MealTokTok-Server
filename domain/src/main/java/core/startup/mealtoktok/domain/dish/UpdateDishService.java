package core.startup.mealtoktok.domain.dish;

import core.startup.mealtoktok.domain.DishStore.DishStore;
import core.startup.mealtoktok.domain.DishStore.DishStoreReader;
import core.startup.mealtoktok.domain.DishStore.TargetDishStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateDishService {

    private final DishReader dishReader;
    private final DishStoreReader dishStoreReader;
    private final DishUpdater dishUpdater;

    public void updateDish(TargetDish targetDish, DishInfo dishInfo) {
        Dish dish = dishReader.read(targetDish);
        DishStore dishStore = dishStoreReader.read(TargetDishStore.from(dish.getDishStoreId()));
        dishReader.checkNoDuplicateDishName(dishStore, dishInfo.dishName());
        dishUpdater.update(dish, dishInfo);
    }
}
