package com.sentrafarma.clinic.repository;

import com.sentrafarma.clinic.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySessionIdOrderByCreatedAtAsc(String sessionId);

    @Query("SELECT DISTINCT c.sessionId FROM ChatMessage c WHERE c.sessionId LIKE %:docPattern% OR c.receiverId = :doctorId OR c.senderId = :doctorId")
    List<String> findDistinctSessionIdsByDoctor(@Param("doctorId") Long doctorId, @Param("docPattern") String docPattern);
}

