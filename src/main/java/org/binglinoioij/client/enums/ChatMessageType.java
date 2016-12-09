package org.binglinoioij.client.enums;

/**
 * <p>
 *
 * </p>
 *
 * <b>Creation Time:</b> 2016/11/25
 *
 * @author binglin
 */
public enum ChatMessageType {
    TXT;

    public enum MessageCategory {
        CHAT("chat"),

        SYSTEM("system");

        MessageCategory(String name) {
            this.name = name;
        }

        private String name;

        public String getName() {
            return name;
        }
    }

    public enum MessageType {
        TXT("txt"),
        IMG("img"),
        VIDEO("video"),
        VOICE("voice");

        MessageType(String name) {
            this.name = name;
        }

        private String name;

        public String getName() {
            return name;
        }
    }
}
