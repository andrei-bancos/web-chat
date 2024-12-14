package com.andreibancos.webchatbackend.dto;

import java.util.Date;
import java.util.UUID;

public class LastChatsDto {
    private UUID senderUserId;
    private UUID receivedUserId;
    private String firstName;
    private String lastName;
    private String lastMessage;
    private boolean read;
    private Date createdAt;

    public LastChatsDto(
            UUID senderUserId,
            UUID receivedUserId,
            String firstName,
            String lastName,
            String lastMessage,
            boolean read,
            Date createdAt) {
        this.senderUserId = senderUserId;
        this.receivedUserId = receivedUserId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastMessage = lastMessage;
        this.read = read;
        this.createdAt = createdAt;
    }

    public UUID getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(UUID senderUserId) {
        this.senderUserId = senderUserId;
    }

    public UUID getReceivedUserId() {
        return receivedUserId;
    }

    public void setReceivedUserId(UUID receivedUserId) {
        this.receivedUserId = receivedUserId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public boolean getRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
