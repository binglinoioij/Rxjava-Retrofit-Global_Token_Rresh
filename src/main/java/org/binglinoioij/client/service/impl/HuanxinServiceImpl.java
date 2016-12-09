package org.binglinoioij.client.service.impl;


import org.binglinoioij.client.dto.ChatMessage;
import org.binglinoioij.client.dto.HuanxinRegisterDto;
import org.binglinoioij.client.enums.ChatMessageStatus;
import org.binglinoioij.client.retrofit.HuanxinApiServiceProvider;
import org.binglinoioij.client.retrofit.SimpleSubscriber;
import org.binglinoioij.client.retrofit.apiservice.HuanxinApiService;
import org.binglinoioij.client.service.HuanxinService;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * <p>
 *
 * </p>
 *
 * <b>Creation Time:</b> 2016/11/30
 *
 * @author binglin
 */
public class HuanxinServiceImpl implements HuanxinService {

    private static HuanxinApiService huanxinApiService = HuanxinApiServiceProvider.Builder.createService(HuanxinApiService.class);

    @Override
    public boolean register(Integer uid, String password) {
        SimpleSubscriber<Map> simpleSubscriber = new SimpleSubscriber<>();
        huanxinApiService.register(new HuanxinRegisterDto(uid.toString(), password)).subscribe(simpleSubscriber);
        return !simpleSubscriber.getResponse().containsKey("error");
    }

    @Override
    public String getUser(Integer uid) {
        SimpleSubscriber<String> stringSimpleSubscriber = new SimpleSubscriber<>();
        HuanxinApiServiceProvider.Builder.createService(HuanxinApiService.class).getUserRx(uid).subscribe(stringSimpleSubscriber);
        return stringSimpleSubscriber.getResponse();
    }

    @Override
    public boolean resetPassword(Integer uid, String newPassword) {
        SimpleSubscriber<Map> simpleSubscriber = new SimpleSubscriber<>();
        huanxinApiService.resetPassword(uid, new HashMap<String, Object>() {{
            put("newpassword", newPassword);
        }}).subscribe(simpleSubscriber);
        return !simpleSubscriber.getResponse().containsKey("error");
    }

    @Override
    public int sentChatMessage(Integer from, Integer to, String content, Integer isGroup) {
        SimpleSubscriber<Map> simpleSubscriber = new SimpleSubscriber<>();
        ChatMessage chatMessage = Integer.valueOf(1).equals(isGroup) ?
                ChatMessage.Builder.buildForGroupTxtWithApns(from, to, content, "你有一条新消息")
                : ChatMessage.Builder.buildForSingleTxtWithApns(from, to, content, "你有一条新消息");
        huanxinApiService.sentMesssage(chatMessage).subscribe(simpleSubscriber);
        boolean result = !simpleSubscriber.getResponse().containsKey("error");
        ChatMessageStatus sentStatus = result ? ChatMessageStatus.SEND : ChatMessageStatus.FAIL;
        return result ? chatMessage.getId().intValue() : 0;
    }

    @Override
    public int sentChatMessage(ChatMessage chatMessage) {
        SimpleSubscriber<Map> simpleSubscriber = new SimpleSubscriber<>();
        huanxinApiService.sentMesssage(chatMessage).subscribe(simpleSubscriber);
        boolean result = !simpleSubscriber.getResponse().containsKey("error");
        return result ? chatMessage.getId().intValue() : 0;
    }
}
