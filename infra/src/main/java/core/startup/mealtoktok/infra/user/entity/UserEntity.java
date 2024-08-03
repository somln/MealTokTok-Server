package core.startup.mealtoktok.infra.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;

import lombok.*;
import lombok.experimental.SuperBuilder;

import core.startup.mealtoktok.domain.auth.OAuthInfo;
import core.startup.mealtoktok.domain.auth.OAuthProvider;
import core.startup.mealtoktok.domain.user.*;
import core.startup.mealtoktok.infra.jpa.config.BaseTimeEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Getter
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET removed_at = NOW() WHERE user_id = ?")
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Embedded private UserProfileVO userProfile;

    @Enumerated(EnumType.STRING)
    private OAuthProvider provider;

    private String oid;

    private LocalDateTime removedAt;

    @Builder.Default
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<DeliveryAddressEntity> deliveryAddresses = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "device_token", joinColumns = @JoinColumn(name = "user_id"))
    private Set<String> deviceTokens = new HashSet<>();

    public static UserEntity from(TargetUser targetUser) {
        return UserEntity.builder().userId(targetUser.userId()).build();
    }

    public static UserEntity from(User user) {
        return UserEntity.builder()
                .userId(user.getUserId())
                .userRole(user.getUserRole())
                .userProfile(UserProfileVO.from(user.getUserProfile()))
                .provider(user.getOAuthInfo().provider())
                .oid(user.getOAuthInfo().oid())
                .deliveryAddresses(
                        user.getDeliveryAddresses().stream()
                                .map(
                                        deliveryAddress ->
                                                DeliveryAddressEntity.from(
                                                        TargetUser.from(user), deliveryAddress))
                                .toList())
                .deviceTokens(user.getDeviceTokens())
                .createdAt(user.getUserDateTime().createdAt())
                .modifiedAt(user.getUserDateTime().modifiedAt())
                .build();
    }

    public static UserEntity from(
            OAuthInfo oAuthInfo, String deviceToken, UserProfile userProfile) {

        return UserEntity.builder()
                .userProfile(UserProfileVO.from(userProfile))
                .provider(oAuthInfo.provider())
                .oid(oAuthInfo.oid())
                .deviceTokens(Set.of(deviceToken))
                .userRole(UserRole.USER)
                .build();
    }

    public User toDomain() {
        return User.builder()
                .userId(userId)
                .userRole(userRole)
                .deviceTokens(deviceTokens)
                .userProfile(userProfile.toDomain())
                .deliveryAddresses(
                        deliveryAddresses.stream().map(DeliveryAddressEntity::toDomain).toList())
                .oAuthInfo(OAuthInfo.of(provider, oid))
                .userDateTime(UserDateTime.of(createdAt, modifiedAt, removedAt))
                .build();
    }

    public UserEntity update(User user) {
        this.userRole = user.getUserRole();
        this.provider = user.getOAuthInfo().provider();
        this.oid = user.getOAuthInfo().oid();
        this.userProfile = UserProfileVO.from(user.getUserProfile());
        this.deviceTokens = user.getDeviceTokens();
        removeNonExistingDeliveryAddresses(user);
        addNewDeliveryAddresses(user);
        return this;
    }

    private void addNewDeliveryAddresses(User user) {
        this.deliveryAddresses.addAll(
                user.getDeliveryAddresses().stream()
                        .filter(deliveryAddress -> deliveryAddress.deliveryAddressId() == null)
                        .map(deliveryAddress -> DeliveryAddressEntity.from(this, deliveryAddress))
                        .toList());
    }

    private void removeNonExistingDeliveryAddresses(User user) {
        Set<Long> userDeliveryAddressIds =
                user.getDeliveryAddresses().stream()
                        .map(DeliveryAddress::deliveryAddressId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());

        this.deliveryAddresses.removeIf(
                deliveryAddressEntity ->
                        !userDeliveryAddressIds.contains(
                                deliveryAddressEntity.getDeliveryAddressId()));
    }
}
