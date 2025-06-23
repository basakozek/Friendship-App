package org.basak.friendshipapp.service;

import org.basak.friendshipapp.dto.MessageDto;
import org.basak.friendshipapp.dto.response.MessageResponseDto;
import org.basak.friendshipapp.entity.Message;
import org.basak.friendshipapp.repository.MessageRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;

    public MessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    /**
     * Yeni bir mesaj oluşturur ve kaydeder
     */
    public MessageResponseDto save(Long senderId, Long receiverId, String message, LocalDateTime sendDate) {
        Message newMessage = Message.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .message(message)
                .sendDate(sendDate)
                .build();
        Message savedMessage = messageRepository.save(newMessage);

        return new MessageResponseDto(savedMessage.getSenderId(), savedMessage.getReceiverId(), savedMessage.getMessage());
    }

    public Message sendMessage(Long senderId, Long receiverId, String content) {
        // Kullanıcıların var olup olmadığını kontrol et
        userService.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Gönderen kullanıcı bulunamadı"));

        userService.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Alıcı kullanıcı bulunamadı"));

        // Mesaj içeriğini kontrol et
        if (content == null || content.trim().isEmpty()) {
            throw new RuntimeException("Mesaj içeriği boş olamaz");
        }

        // Yeni mesaj oluştur
        Message message = Message.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .message(content)
                .sendDate(LocalDateTime.now())
                .build();

        // Mesajı kaydet ve döndür
        return messageRepository.save(message);
    }

    public Message findById(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Mesaj bulunamadı"));
    }

    /**
     * Tüm mesajları listeler
     */
    public List<MessageResponseDto> findAll() {
        List<Message> messageList = messageRepository.findAll();
        List<MessageResponseDto> messageResponseDtoList = new ArrayList<>();
        for (Message message : messageList) {
            messageResponseDtoList.add(new MessageResponseDto(message.getSenderId(), message.getReceiverId(), message.getMessage()));
        }
        return messageResponseDtoList;
    }

    /**
     * Belirli bir tarihten sonra gönderilen mesajları getirir
     */
    public List<Message> findAllBySendDateAfter(LocalDateTime date) {
        return messageRepository.findAllBySendDateAfter(date);
    }
    /**
     * Belirli bir tarihten önce gönderilen mesajları getirir
     */
    public List<Message> findAllBySendDateBefore(LocalDateTime date) {
        return messageRepository.findAllBySendDateBefore(date);
    }
    /**
     * İki tarih arasında gönderilen mesajları getirir
     */
    public List<Message> findAllBySendDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return messageRepository.findAllBySendDateBetween(startDate, endDate);
    }

    /**
     * Tüm mesajları gönderilme tarihine göre yeniden eskiye sıralar
     */
    public List<Message> findAllByOrderBySendDateDesc() {
        return messageRepository.findAllByOrderBySendDateDesc();
    }

    /**
     * Belirli bir kullanıcının gönderdiği tüm mesajları getirir
     */
    public List<Message> findAllBySenderId(Long senderId) {
        return messageRepository.findAllBySenderId(senderId);
    }

    /**
     * Belirli bir metni içeren tüm mesajları getirir
     */
    public List<Message> findAllByMessageContainingIgnoreCase(String text) {
        return messageRepository.findAllByMessageContainingIgnoreCase(text);
    }

    /**
     * Belirli bir metni içermeyen tüm mesajları getirir
     */
    public List<Message> findAllByMessageNotContainingIgnoreCase(String text) {
        return messageRepository.findAllByMessageNotContainingIgnoreCase(text);
    }

    /**
     * Her konuşma için en son mesajı getirir
     */
    public List<Message> findLatestMessagePerConversation() {
        return messageRepository.findLatestMessagePerConversation();
    }

    /**
     * Bir kullanıcının okunmamış mesaj sayısını getirir (Message entity'sine 'read' alanı eklenmesi gerekir)
     */
    public Long countUnreadMessagesByReceiverId(Long receiverId) {
        return messageRepository.countUnreadMessagesByReceiverId(receiverId);
    }

    /**
     * Mesajı okundu olarak işaretle (Message entity'sine 'read' alanı eklenmesi gerekir)
     */
    public Message markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Mesaj bulunamadı"));
        message.setRead(true);
        return messageRepository.save(message);
    }

    /**
     * Bir kullanıcının tüm mesajlarını okundu olarak işaretle (Message entity'sine 'read' alanı eklenmesi gerekir)
     */
    public void markAllAsRead(Long receiverId) {
        List<Message> unreadMessages = findAllByReceiverId(receiverId);

        for (Message message : unreadMessages) {

            if (!message.getRead()) {
                message.setRead(true);
                messageRepository.save(message);
            }
        }
    }

    /**
     * Belirli bir kullanıcının aldığı tüm mesajları getirir
     */
    public List<Message> findAllByReceiverId(Long receiverId) {
        return messageRepository.findAllByReceiverId(receiverId);
    }

    /**
     * Mesajı sil
     */
    public void deleteMessage(Long messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new RuntimeException("Mesaj bulunamadı");
        }
        messageRepository.deleteById(messageId);
    }

    /**
     * İki kullanıcı arasındaki tüm konuşmayı sil
     */
    public void deleteConversation(Long user1Id, Long user2Id) {
        List<Message> conversation = findConversation(user1Id, user2Id);
        messageRepository.deleteAll(conversation);
    }

    /**
     * İki kullanıcı arasındaki konuşmayı getirir (tarih sırasına göre)
     */
    public List<Message> findConversation(Long user1Id, Long user2Id) {
        return messageRepository.findAllBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderBySendDateAsc(
                user1Id, user2Id, user1Id, user2Id);
    }

    /**
     * Tüm mesajları DTO formatında getir
     */
    public List<MessageDto> findAllMessageDtos() {
        return messageRepository.findAllMessageDtos();
    }

    /**
     * Belirli bir kullanıcının aldığı mesajları özet olarak getir
     */
    public List<MessageDto> findReceivedMessageSummaries(Long receiverId) {
        return messageRepository.findReceivedMessageSummaries(receiverId);
    }

    /**
     * Son 24 saat içinde gönderilen mesajları say
     */
    public Long countMessagesInLast24Hours() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        return messageRepository.countMessagesInLast24Hours(oneDayAgo);
    }

}
