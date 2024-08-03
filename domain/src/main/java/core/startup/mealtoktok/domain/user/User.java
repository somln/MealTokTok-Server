package core.startup.mealtoktok.domain.user;

import java.util.List;
import java.util.Set;

import lombok.*;

import core.startup.mealtoktok.domain.auth.OAuthInfo;
import core.startup.mealtoktok.domain.user.exception.AlreadyExistAddressException;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private Long userId;
    private UserProfile userProfile;
    private OAuthInfo oAuthInfo;
    private UserRole userRole;
    private Set<String> deviceTokens;
    private UserDateTime userDateTime;
    private List<DeliveryAddress> deliveryAddresses;

    public void update(UserProfile userProfile, String deviceToken) {
        this.userProfile.update(userProfile);
        this.deviceTokens.add(deviceToken);
    }

    public void addDeliveryAddress(DeliveryAddress deliveryAddress) {
        if (this.deliveryAddresses.stream()
                .anyMatch(
                        awc ->
                                awc.addressWithCoordinate()
                                        .coordinate()
                                        .equals(
                                                deliveryAddress
                                                        .addressWithCoordinate()
                                                        .coordinate()))) {
            throw AlreadyExistAddressException.EXCEPTION;
        }

        this.deliveryAddresses.add(deliveryAddress);
    }

    public void removeDeliveryAddress(TargetDeliveryAddress targetDeliveryAddress) {
        this.deliveryAddresses.removeIf(
                deliveryAddress ->
                        deliveryAddress
                                .deliveryAddressId()
                                .equals(targetDeliveryAddress.deliveryAddressId()));
    }

    public void updateEmail(String email) {
        this.userProfile.updateEmail(email);
    }

    public void updateNickname(String nickname) {
        this.userProfile.updateNickname(nickname);
    }

    public boolean isRemoved() {
        return userDateTime.removedAt() != null;
    }
}
