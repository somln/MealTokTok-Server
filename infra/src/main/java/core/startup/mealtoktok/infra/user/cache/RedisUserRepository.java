package core.startup.mealtoktok.infra.user.cache;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import core.startup.mealtoktok.domain.user.User;
import core.startup.mealtoktok.domain.user.UserCacheRepository;
import core.startup.mealtoktok.domain.user.UserId;
import core.startup.mealtoktok.infra.user.config.RedisConfig;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisUserRepository implements UserCacheRepository {

    private final RedisTemplate<String, User> redisTemplate;

    public void cache(User user) {
        String key = getKey(user.getUserId().getValue());
        log.info("Set UserEntity from {} : {}", key, user.getUserProfile().getNickname());
        redisTemplate.opsForValue().setIfAbsent(key, user, RedisConfig.USER_CACHE_TTL);
    }

    public Optional<User> getUser(UserId userId) {
        String key = getKey(userId.getValue());
        Optional<User> user = Optional.ofNullable(redisTemplate.opsForValue().get(key));
        user.ifPresentOrElse(
                u ->
                        log.info(
                                "Get User from Cache - {} : {}",
                                key,
                                u.getUserProfile().getNickname()),
                () -> log.info("No User Cache - {}", key));
        return user;
    }

    public void deleteUser(User user) {
        String key = getKey(user.getUserId().getValue());
        redisTemplate.delete(key);
        log.info("유저 캐싱 폐기 완료 - {}", key);
    }

    public String getKey(Long userId) {
        return "USER:" + userId;
    }
}
