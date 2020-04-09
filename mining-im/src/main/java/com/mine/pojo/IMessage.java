package com.mine.pojo;


public class IMessage {
    private byte type;
    private int receiveId;
    private String msg;
    private String sessionId;

    public IMessage() {
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(int receiveId) {
        this.receiveId = receiveId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "IMessage{" +
                "type=" + type +
                ", receiveId=" + receiveId +
                ", msg='" + msg + '\'' +
                '}';
    }
}
