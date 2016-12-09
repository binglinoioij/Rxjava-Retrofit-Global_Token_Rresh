package org.binglinoioij.client.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import org.binglinoioij.client.enums.ChatMessageStatus;
import org.binglinoioij.client.enums.ChatMessageType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * <p>
 *
 * </p>
 *
 * <b>Creation Time:</b> 2016/11/25
 *
 * @author binglin
 */
public class ChatMessage implements Serializable {

    private Long id;

    @JSONField(serialize = false)
    private Date createTime;

    @JSONField(serialize = false)
    private Date modifyTime;

    private HuanxinMsg msg;

    private Map<String, Object> ext;

    private String target_type;

    private List<String> target;

    private String from;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTarget_type() {
        return target_type;
    }

    public void setTarget_type(String target_type) {
        this.target_type = target_type;
    }

    public List<String> getTarget() {
        return target;
    }

    public void setTarget(List<String> target) {
        this.target = target;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Map<String, Object> getExt() {
        return ext;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;

    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public HuanxinMsg getMsg() {
        return msg;
    }

    public void setMsg(HuanxinMsg msg) {
        this.msg = msg;
    }

    public void setExtApns(String title) {
        this.ext = new HashMap<>();
        Map<String, Object> apnsMap = new HashMap<>();
        apnsMap.put("em_push_title", title);
        this.ext.put("em_apns_ext", apnsMap);
    }

    public void notSetExtApns() {
        this.ext = new HashMap<>();
        Map<String, Object> apnsMap = new HashMap<>();
        apnsMap.put("em_ignore_notification", true);
        this.ext.put("em_apns_ext", apnsMap);
    }

    public static class HuanxinMsg {
        private String msg;

        private String type = ChatMessageType.TXT.name().toLowerCase();

        public HuanxinMsg() {
        }

        public HuanxinMsg(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class BaseMessageContent {
        private Long msgId;

        private Integer fromUid;

        private Integer toUid;

        private Date createTime;

        private String category;

        private Properties msg;

        public BaseMessageContent() {
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Properties getMsg() {
            return msg;
        }

        public void setMsg(Properties msg) {
            this.msg = msg;
        }

        public Long getMsgId() {
            return msgId;
        }

        public void setMsgId(Long msgId) {
            this.msgId = msgId;
        }

        public Integer getFromUid() {
            return fromUid;
        }

        public void setFromUid(Integer fromUid) {
            this.fromUid = fromUid;
        }

        public Integer getToUid() {
            return toUid;
        }

        public void setToUid(Integer toUid) {
            this.toUid = toUid;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

    }

    public static class Builder {

        public static ChatMessage buildForSingleTxtWithApns(Integer fromUid, Integer toUid, String content, String ApnsTitle) {
            BaseMessageContent baseTxtMessage = buildChatTxtMessage(fromUid, toUid, content);
            return buildChatMessageFromBaseMessageContentWithApns(baseTxtMessage, ApnsTitle, false);
        }

        public static ChatMessage buildForGroupTxtWithApns(Integer fromUid, Integer groupId, String content, String ApnsTitle) {
            BaseMessageContent baseTxtMessage = buildChatTxtMessage(fromUid, groupId, content);
            return buildChatMessageFromBaseMessageContentWithApns(baseTxtMessage, ApnsTitle, true);
        }

        /**
         * 从BaseMessageContent中生成与环信相关的消息结构，就是生成出msg字段以外的其他字段
         */
        public static ChatMessage buildChatMessageFromBaseMessageContentWithApns(BaseMessageContent baseMessageContent, String apnsTitle, boolean group) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setId(baseMessageContent.getMsgId());
            chatMessage.setCreateTime(new Date());
            chatMessage.setModifyTime(new Date());
            chatMessage.setMsg(new HuanxinMsg(JSON.toJSONString(baseMessageContent)));
            chatMessage.setExtApns(apnsTitle);

            chatMessage.setTarget_type(group ? "chatgroups" : "users");
            chatMessage.setTarget(new ArrayList<String>() {{
                add(baseMessageContent.getToUid().toString());
            }});
            chatMessage.setFrom(baseMessageContent.getFromUid().toString());
            chatMessage.setStatus(ChatMessageStatus.NEW.name());
            return chatMessage;
        }

        /**
         * 生成聊天的业务msg字段例如
         * "msg": {
         * "createTime": 1480664172,
         * "fromUid": 12,
         * "msg": {
         * "msg": "tttttt"
         * },
         * "msgId": 2000,
         * "toUid": 13,
         * "type": {
         * "category": "chat",
         * "msgType": "txt"
         * }
         * }
         */
        public static BaseMessageContent buildChatTxtMessage(Integer from, Integer to, String content) {
            Long msgId = 2000L;//实际应用应该是自动生成的id
            BaseMessageContent txtMessageContent = new BaseMessageContent();
            txtMessageContent.setMsgId(msgId);
            txtMessageContent.setToUid(to);
            txtMessageContent.setFromUid(from);
            txtMessageContent.setMsg(new Properties() {{
                put("msg", content);
                put("msgType", ChatMessageType.MessageType.TXT.getName());
            }});
            txtMessageContent.setCategory(ChatMessageType.MessageCategory.CHAT.getName());
            //客户端时间只需要到秒
            txtMessageContent.setCreateTime(new Date(Math.floorDiv(new Date().getTime(), 1000)));
            return txtMessageContent;
        }

    }

}
