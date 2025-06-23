package org.basak.friendshipapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.basak.friendshipapp.dto.MessageDto;
import org.basak.friendshipapp.dto.request.MessageRequestDto;
import org.basak.friendshipapp.dto.response.BaseResponse;
import org.basak.friendshipapp.dto.response.MessageResponseDto;
import org.basak.friendshipapp.entity.Message;
import org.basak.friendshipapp.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.basak.friendshipapp.constant.EndPoints.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller sınıfı son kullanıcı ile iletişime geçiş için kullanılır. Gelen requestleri handle edeceğimiz sınıftır.
 * Burada web için gerekli erişimler tanımlanmalı ve istekler işlenip response'lar dönülmelidir.
 *
 * MessageController.md erişim: http://localhost:9090/message
 */
@RestController
@RequestMapping(value = MESSAGE)
@Tag(name = "Message API", description = "Mesaj işlemleri için API")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Örnek mesajlar oluşturur
     * http://localhost:9090/message/save
     */
    @Operation(
            summary = "Örnek mesajlar oluştur",
            description = "Sistem içinde test amaçlı kullanılabilecek örnek mesajlar oluşturur"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Mesajlar başarıyla oluşturuldu",
            content = {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}
    )
    @GetMapping("/save")
    public String save() {
        messageService.save(1L, 2L, "Merhaba, nasılsın?", LocalDateTime.now());
        messageService.save(2L, 1L, "İyiyim, teşekkürler. Sen nasılsın?", LocalDateTime.now().plusMinutes(5));
        messageService.save(1L, 3L, "Selam, bugün toplantı var mı?", LocalDateTime.now().plusMinutes(10));
        messageService.save(3L, 1L, "Evet, saat 14:00'te.", LocalDateTime.now().plusMinutes(15));
        messageService.save(2L, 3L, "Proje dosyalarını gönderebilir misin?", LocalDateTime.now().plusMinutes(20));

        return "5 mesaj kaydedildi";
    }

    @PostMapping("/messages")
    public ResponseEntity<BaseResponse<MessageResponseDto>> saveMessage(@RequestBody @Valid MessageRequestDto dto) {
        MessageResponseDto savedMessage =
                messageService.save(dto.senderId(), dto.receiverId(), dto.message(), LocalDateTime.now());
        return ResponseEntity.ok(BaseResponse.<MessageResponseDto>builder()
                .success(true)
                .code(200)
                .message("Mesaj başarıyla gönderildi.")
                .data(savedMessage).build());
    }
    /**
     * Tüm mesajları listeler
     * http://localhost:9090/message/findall
     */
    @GetMapping("/messages")
    public ResponseEntity<BaseResponse<List<MessageResponseDto>>> findAll() {
        return ResponseEntity.ok(BaseResponse.<List<MessageResponseDto>>builder()
                .success(true)
                .code(200)
                .message("Mesajlar başarıyla getirildi.")
                .data(messageService.findAll())
                .build());
    }

    /**
     * Belirli bir kullanıcının gönderdiği mesajları listeler
     * http://localhost:9090/message/findbysender?senderId=1
     */
    @GetMapping("/findbysender")
    public List<Message> findAllBySenderId(Long senderId) {
        return messageService.findAllBySenderId(senderId);
    }

    /**
     * Belirli bir kullanıcının aldığı mesajları listeler
     * http://localhost:9090/message/findbyreceiver?receiverId=1
     */
    @GetMapping("/findbyreceiver")
    @RequestMapping(value = "findbyreceiver",method = RequestMethod.GET)
    public List<Message> findAllByReceiverId(Long receiverId) {
        return messageService.findAllByReceiverId(receiverId);
    }

    /**
     * Son 24 saat içindeki mesajları listeler
     * http://localhost:9090/message/findrecent
     */
    @GetMapping("/findrecent")
    public List<Message> findRecentMessages() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        return messageService.findAllBySendDateAfter(yesterday);
    }
    /**
     * Belirli bir tarihten önce gönderilen mesajları listeler.
     * http://localhost:9090/message/findbefore?date=2023-01-01
     */
    @GetMapping("/findbefore")
    public List<Message> findAllBySendDateBefore(String dateStr) {
        // String'i LocalDateTime'a çevirme
        LocalDateTime date = LocalDateTime.parse(dateStr+ "T23:59:59");
        return messageService.findAllBySendDateBefore(date);
    }
    /**
     * İki tarih arasında gönderilen mesajları listeler
     * http://localhost:9090/message/findbetween?startDate=2023-01-01&endDate=2023-01-31
     */
    @GetMapping("/findbetween")
    public List<Message> findAllBySendDateBetween(String startDate, String endDate) {
        // Başlangıç tarihinin başlangıcı (00:00:00)
        LocalDateTime startDateTime = LocalDateTime.parse(startDate + "T00:00:00");

        // Bitiş tarihinin sonu (23:59:59)
        LocalDateTime endDateTime = LocalDateTime.parse(endDate + "T23:59:59");

        return messageService.findAllBySendDateBetween(startDateTime, endDateTime);
    }
    /**
     * Mesajları gönderilme tarihine göre tersten sıralar
     * http://localhost:9090/message/findallorderbydatedesc
     */
    @GetMapping("/findallorderbydatedesc")
    public List<Message> findAllOrderBySendDateDesc() {
        return messageService.findAllByOrderBySendDateDesc();
    }

    /**
     * Belirli bir metni içeren mesajları listeler
     * http://localhost:9090/message/findbycontaining?text=merhaba
     */
    @GetMapping("/findbycontaining")
    public List<Message> findAllByMessageContainingIgnoreCase(String text) {
        return messageService.findAllByMessageContainingIgnoreCase(text);
    }

    /**
     * Belirli bir metni içermeyen mesajları listeler
     * http://localhost:9090/message/findbynotcontaining?text=toplantı
     */
    @GetMapping("/findbynotcontaining")
    public List<Message> findAllByMessageNotContainingIgnoreCase(String text) {
        return messageService.findAllByMessageNotContainingIgnoreCase(text);
    }

    /**
     * İki kullanıcı arasındaki konuşmayı listeler
     * http://localhost:9090/message/findconversation?user1Id=1&user2Id=2
     */
    @GetMapping("/findconversation")
    public List<Message> findConversation(@RequestParam(defaultValue = "0") Long user1Id, @RequestParam Long user2Id) {
        return messageService.findConversation(user1Id, user2Id);
    }

    /**
     * Her konuşma için en son mesajı listeler
     * http://localhost:9090/message/findlatestperconversation
     */
    @GetMapping("/findlatestperconversation")
    public List<Message> findLatestMessagePerConversation() {
        return messageService.findLatestMessagePerConversation();
    }

    /**
     * Tüm mesajları DTO formatında listeler
     * http://localhost:9090/message/findalldtos
     */
    @GetMapping("/findalldtos")
    public List<MessageDto> findAllMessageDtos() {
        return messageService.findAllMessageDtos();
    }
    /**
     * Bir kullanıcının aldığı mesajları özet olarak listeler
     * http://localhost:9090/message/findsummaries?receiverId=1
     */
    @Operation(summary = "Mesaj özetlerini getir", description = "Bir kullanıcının aldığı mesajların özetlerini listeler")
    @GetMapping("/findsummaries")
    public List<MessageDto> findReceivedMessageSummaries(
            @Parameter(description = "Alıcı kullanıcının ID'si") Long receiverId) {
        return messageService.findReceivedMessageSummaries(receiverId);
    }
    /**
     * Bir mesajı okundu olarak işaretler
     * http://localhost:9090/message/markasread?messageId=1
     */
    @Operation(summary = "Mesajı okundu olarak işaretle", description = "Belirtilen mesajı okundu olarak işaretler")
    @GetMapping("/markasread")
    public Message markAsRead(
            @Parameter(description = "İşaretlenecek mesajın ID'si") Long messageId) {
        return messageService.markAsRead(messageId);
    }

    /**
     * Bir kullanıcının tüm mesajlarını okundu olarak işaretler
     * http://localhost:9090/message/markallasread?receiverId=1
     */
    @Operation(summary = "Tüm mesajları okundu olarak işaretle", description = "Bir kullanıcının tüm mesajlarını okundu olarak işaretler")
    @GetMapping("/markallasread")
    public String markAllAsRead(
            @Parameter(description = "Alıcı kullanıcının ID'si") Long receiverId) {
        messageService.markAllAsRead(receiverId);
        return "Tüm mesajlar okundu olarak işaretlendi";
    }
    /**
     * Son 24 saat içinde gönderilen mesajları sayar
     * http://localhost:9090/message/countlast24hours
     */
    @Operation(summary = "Son 24 saatteki mesaj sayısını getir", description = "Son 24 saat içinde gönderilen mesajların sayısını döndürür")
    @GetMapping("/countlast24hours")
    public Long countMessagesInLast24Hours() {
        return messageService.countMessagesInLast24Hours();
    }
}