package org.binglinoioij.client.service;


import org.binglinoioij.client.dto.ChatMessage;

/**
 * <p>
 *
 * </p>
 *
 * <b>Creation Time:</b> 2016/11/30
 *
 * @author binglin
 */
public interface HuanxinService {
    boolean register(Integer uid, String password);

    String getUser(Integer uid);

    boolean resetPassword(Integer uid, String newPassword);

    int sentChatMessage(Integer from, Integer to, String content, Integer isGroup);

    int sentChatMessage(ChatMessage chatMessage);
}
