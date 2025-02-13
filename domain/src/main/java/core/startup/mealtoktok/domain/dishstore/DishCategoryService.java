package core.startup.mealtoktok.domain.dishstore;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DishCategoryService {

    private final DishCategoryReader dishCategoryReader;
    private final DishCategoryRemover dishCategoryRemover;
    private final DishCategoryUpdater dishCategoryUpdater;
    private final DishCategoryAppender dishCategoryAppender;

    @Transactional
    public void createDishCategory(DishCategoryInfo dishCategoryInfo) {
        dishCategoryAppender.append(dishCategoryInfo);
    }

    public List<DishCategory> readDishCategories() {
        return dishCategoryReader.readAll();
    }

    @Transactional
    public void deleteDishCategory(TargetDishCategory targetDishCategory) {
        DishCategory dishCategory = dishCategoryReader.read(targetDishCategory);
        dishCategoryRemover.remove(dishCategory);
    }

    @Transactional
    public void updateDishCategory(
            TargetDishCategory targetDishCategory, DishCategoryInfo dishCategoryInfo) {
        DishCategory dishCategory = dishCategoryReader.read(targetDishCategory);
        dishCategoryUpdater.update(dishCategory, dishCategoryInfo);
    }
}
