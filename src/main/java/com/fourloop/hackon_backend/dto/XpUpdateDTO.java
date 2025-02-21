package com.fourloop.hackon_backend.dto;

public class XpUpdateDTO {
    private int xpAmount;
    private String reason;

    public XpUpdateDTO() {
    }

    public XpUpdateDTO(int xpAmount, String reason) {
        this.xpAmount = xpAmount;
        this.reason = reason;
    }

    public int getXpAmount() {
        return xpAmount;
    }

    public void setXpAmount(int xpAmount) {
        this.xpAmount = xpAmount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
