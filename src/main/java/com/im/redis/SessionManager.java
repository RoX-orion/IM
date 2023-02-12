package com.im.redis;

import com.im.lib.entity.SessionInfo;
import com.im.lib.storage.JacksonSerialize;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Copyright (c) 2023 Andre Lina. All rights reserved.
 *
 * @author: Andre Lina
 * @date: 2023-02-12
 */

@Component
public class SessionManager {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate<String, SessionInfo> redisTemplate;

    private final String SESSION = "session:";

    public SessionInfo getMsgInfo(BigInteger authKeyId, long sessionId) {
        Object o = stringRedisTemplate
                .opsForHash()
                .get(SESSION + authKeyId.toString(), String.valueOf(sessionId));

        return JacksonSerialize.getObject(String.valueOf(o), SessionInfo.class);
    }

    public void setSessionId(BigInteger authKeyId,long sessionId, SessionInfo sessionInfo) {
//        stringRedisTemplate
//                .opsForList()
//                .rightPush(AUTH_KEY_ID_SESSION_ID + authKeyId.toString(), String.valueOf(sessionId));
        stringRedisTemplate
                .opsForHash()
                .put(SESSION + authKeyId.toString(), String.valueOf(sessionId), Objects.requireNonNull(JacksonSerialize.getObjectString(sessionInfo)));
    }

    public boolean hasSessionId(BigInteger authKeyId, long sessionId) {
        return redisTemplate
                .opsForHash()
                .hasKey(SESSION + authKeyId.toString(), String.valueOf(sessionId));
    }

    public void removeSessionId(BigInteger authKeyId, long sessionId) {
        redisTemplate
                .opsForHash()
                .delete(SESSION + authKeyId.toString(), String.valueOf(sessionId));

//        redisTemplate.opsForZSet().remove(AUTH_KEY_ID_SESSION_ID + authKeyId.toString(), sessionId);
    }

    public void setSessionInfo(String authKeyId, String key, Object value) {
        redisTemplate
                .opsForHash()
                .put(SESSION + authKeyId, key, value);
    }

    public Object getSessionInfo(String authKeyId, String key) {
        return redisTemplate
                .opsForHash()
                .get(SESSION + authKeyId, key);
    }

    public void removeTempSessionInfo(String channelId) {
        String authKeyId = stringRedisTemplate.opsForValue().get(KeyPrefix.CHANNEL_ID_AUTH_KEY_ID + channelId);
        if (!StringUtils.hasLength(authKeyId)) {
            return;
        }
        boolean readyLogin = (boolean) this.getSessionInfo(authKeyId, SessionInfo.READY_LOGIN);
        if (!readyLogin) {
            redisTemplate.delete(SESSION + authKeyId);
            stringRedisTemplate.delete(KeyPrefix.CHANNEL_ID_AUTH_KEY_ID + channelId);
        }
    }
}
