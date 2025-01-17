package core.startup.mealtoktok.common.domain;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public abstract class BaseId<T> {

    private T value;

    protected BaseId(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseId<?> baseId = (BaseId<?>) o;
        return value.equals(baseId.value);
    }
}
