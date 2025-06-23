package org.basak.friendshipapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Mesaj verisini taşımak için Data Transfer Object (DTO) sınıfı
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String message;
    private LocalDateTime sendDate;

    /**
     * Özet mesaj constructor'ı (receiverId olmadan)
     */
    public MessageDto(Long id, Long senderId, String message, LocalDateTime sendDate) {
        this.id = id;
        this.senderId = senderId;
        this.message = message;
        this.sendDate = sendDate;
    }
}
