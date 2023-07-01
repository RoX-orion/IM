package com.im.lib.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

/**
 * Copyright (c) 2023 Andre Lina. All rights reserved.
 *
 * @author: Andre Lina
 * @date: 2023-03-03
 */

@Component
public class ChannelManager {

    /**
     * 管理全局的channel
     * GlobalEventExecutor.INSTANCE  全局事件监听器
     * 一旦将channel 加入 ChannelGroup 就不要用手动去
     * 管理channel的连接失效后移除操作，他会自己移除
     */
    private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public Channel getChannel(ChannelId channelId) {
        return channels.find(channelId);
    }

    public void setChannel(Channel channel) {
        channels.add(channel);
    }

    public int size() {
        return channels.size();
    }

    public ChannelGroup getChannelGroup() {
        return channels;
    }
}
