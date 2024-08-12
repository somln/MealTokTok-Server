package core.startup.mealtoktok.domain.order;

import java.math.BigDecimal;

public record Money(BigDecimal amount) {

    public static Money from(BigDecimal amount) {
        return new Money(amount);
    }

    public Money add(Money money) {
        return new Money(amount.add(money.amount));
    }

    public Money subtract(Money money) {
        return new Money(amount.subtract(money.amount));
    }

    public Money multiply(int multiplier) {
        return new Money(amount.multiply(BigDecimal.valueOf(multiplier)));
    }

    public Money divide(int divisor) {
        return new Money(amount.divide(BigDecimal.valueOf(divisor)));
    }
}
