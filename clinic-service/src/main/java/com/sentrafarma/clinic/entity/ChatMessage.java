package com.sentrafarma.clinic.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @Column(name = "sender_name")
    private String senderName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String pesan;

    @Column(name = "tipe_pesan", nullable = false)
    private String tipePesan = "TEXT";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ChatMessage() {}

    public ChatMessage(Long id, String sessionId, Long senderId, Long receiverId, String senderName, String pesan, String tipePesan, LocalDateTime createdAt) {
        this.id = id;
        this.sessionId = sessionId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.pesan = pesan;
        this.tipePesan = tipePesan != null ? tipePesan : "TEXT";
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getPesan() { return pesan; }
    public void setPesan(String pesan) { this.pesan = pesan; }
    public String getTipePesan() { return tipePesan; }
    public void setTipePesan(String tipePesan) { this.tipePesan = tipePesan; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static ChatMessageBuilder builder() { return new ChatMessageBuilder(); }

    public static class ChatMessageBuilder {
        private Long id;
        private String sessionId;
        private Long senderId;
        private Long receiverId;
        private String senderName;
        private String pesan;
        private String tipePesan = "TEXT";

        public ChatMessageBuilder id(Long id) { this.id = id; return this; }
        public ChatMessageBuilder sessionId(String sessionId) { this.sessionId = sessionId; return this; }
        public ChatMessageBuilder senderId(Long senderId) { this.senderId = senderId; return this; }
        public ChatMessageBuilder receiverId(Long receiverId) { this.receiverId = receiverId; return this; }
        public ChatMessageBuilder senderName(String senderName) { this.senderName = senderName; return this; }
        public ChatMessageBuilder pesan(String pesan) { this.pesan = pesan; return this; }
        public ChatMessageBuilder tipePesan(String tipePesan) { this.tipePesan = tipePesan; return this; }

        public ChatMessage build() {
            return new ChatMessage(id, sessionId, senderId, receiverId, senderName, pesan, tipePesan, null);
        }
    }
}
