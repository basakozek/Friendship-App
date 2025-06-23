package org.basak.friendshipapp.repository;

import org.basak.friendshipapp.dto.MessageDto;
import org.basak.friendshipapp.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    /**
     * Belirli bir tarihten sonra gönderilen tüm mesajları bulur
     */
    List<Message> findAllBySendDateAfter(LocalDateTime date);

    /**
     * Belirli bir tarihten önce gönderilen tüm mesajları bulur
     */
    List<Message> findAllBySendDateBefore(LocalDateTime date);

    /**
     * İki tarih arasında gönderilen tüm mesajları bulur
     */
    List<Message> findAllBySendDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Tüm mesajları gönderilme tarihine göre tersten sıralar (en yeniden en eskiye)
     */
    List<Message> findAllByOrderBySendDateDesc();
    /**
     * Belirli bir kullanıcının gönderdiği tüm mesajları bulur
     */
    List<Message> findAllBySenderId(Long senderId);

    /**
     * Belirli bir kullanıcının aldığı tüm mesajları bulur
     */
    List<Message> findAllByReceiverId(Long receiverId);

    /**
     * İki kullanıcı arasındaki konuşmayı bulur (birbirlerine gönderdikleri mesajlar)
     */
    List<Message> findAllBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderBySendDateAsc(
            Long user1Id, Long user2Id, Long user1IdAgain, Long user2IdAgain);
    /**
     * Belirli bir metni içeren tüm mesajları bulur (büyük/küçük harf duyarsız)
     */
    List<Message> findAllByMessageContainingIgnoreCase(String text);

    /**
     * Belirli bir metni içermeyen tüm mesajları bulur (büyük/küçük harf duyarsız)
     */
    List<Message> findAllByMessageNotContainingIgnoreCase(String text);

    /**
     * Her konuşma için en son mesajı getirir (native SQL kullanarak)
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM tbl_message m1 WHERE m1.send_date = " +
                    "(SELECT MAX(m2.send_date) FROM tbl_message m2 " +
                    "WHERE (m1.sender_id = m2.sender_id AND m1.receiver_id = m2.receiver_id) " +
                    "OR (m1.sender_id = m2.receiver_id AND m1.receiver_id = m2.sender_id))")
    List<Message> findLatestMessagePerConversation();

    /**
     * Son 24 saat içinde gönderilen mesajları sayar
     */
    @Query("SELECT COUNT(m) FROM Message m WHERE m.sendDate >= :oneDayAgo")
    Long countMessagesInLast24Hours(LocalDateTime oneDayAgo);

    /**
     * Belirli bir kullanıcının okunmamış mesajlarını sayar
     * (Message entity'sine 'read' boolean alanı eklenmesi gerekir)
     */
    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiverId = ?1 AND m.read = false")
    Long countUnreadMessagesByReceiverId(Long receiverId);

    /**
     * Tüm mesajları DTO formatında döndürür
     */
    @Query("SELECT new org.basak.friendshipapp.dto.MessageDto(m.id, m.senderId, m.receiverId, m.message, m.sendDate) FROM" +
            " Message m")
    List<MessageDto> findAllMessageDtos();

    /**
     * Belirli bir kullanıcının aldığı mesajları özet olarak döndürür
     */
    @Query("SELECT new org.basak.friendshipapp.dto.MessageDto(m.id, m.senderId, m.message, m.sendDate) FROM Message m " +
            "WHERE m.receiverId = ?1 ORDER BY m.sendDate DESC")
    List<MessageDto> findReceivedMessageSummaries(Long receiverId);

    /**
     * Belirli bir kullanıcının okunmamış mesajlarını bulur
     */
    List<Message> findAllByReceiverIdAndReadFalse(Long receiverId);
}