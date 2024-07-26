package core.startup.mealtoktok.infra.dish.entity;

import core.startup.mealtoktok.domain.dish.Dish;
import core.startup.mealtoktok.domain.dish.DishInfo;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class DishEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long dishId;

    String dishName;

    String imgUrl;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="store_id")
    private DishStoreEntity dishStoreEntity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="category_id")
    private DishCategoryEntity dishCategoryEntity;


    public static DishEntity of(DishStoreEntity dishStoreEntity,
                                DishCategoryEntity dishCategoryEntity,
                                DishInfo dishInfo){
        return DishEntity.builder()
                .dishName(dishInfo.dishName())
                .imgUrl(dishInfo.imgUrl())
                .dishStoreEntity(dishStoreEntity)
                .dishCategoryEntity(dishCategoryEntity)
                .build();
    }

    public Dish toDomain() {
        return Dish.builder()
                .dishId(dishId)
                .dishName(dishName)
                .imgUrl(imgUrl)
                .dishStoreId(dishStoreEntity.getStoreId())
                .dishCategoryId(dishCategoryEntity.getCategoryId())
                .build();
    }

    public void update(DishInfo dishInfo) {
        this.dishName = dishInfo.dishName();
        this.imgUrl = dishInfo.imgUrl();
    }
}
