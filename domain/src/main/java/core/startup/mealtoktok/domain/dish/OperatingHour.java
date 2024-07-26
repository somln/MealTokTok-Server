package core.startup.mealtoktok.domain.dish;

import java.time.LocalTime;

public record OperatingHour(
        LocalTime openTime,
        LocalTime closeTime
) {
    public static OperatingHour of(LocalTime openTime, LocalTime closeTime){
        return new OperatingHour(openTime, closeTime);
    }
}
