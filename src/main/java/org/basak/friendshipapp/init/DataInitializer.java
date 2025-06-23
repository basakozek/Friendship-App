package org.basak.friendshipapp.init;

import jakarta.annotation.PostConstruct;
import org.basak.friendshipapp.entity.*;
import org.basak.friendshipapp.repository.FollowRepository;
import org.basak.friendshipapp.repository.MessageRepository;
import org.basak.friendshipapp.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.basak.friendshipapp.constant.ConsoleColors.*;

/**
 * DataInitializer sınıfı, uygulama başlatıldığında örnek veriler oluşturur. Bu sınıf, development ortamında
 * veritabanına başlangıç verileri eklemek için kullanılır.
 * <p>
 * Özellikler: - Örnek kullanıcılar ve mesajlar oluşturur - Konsol üzerinde renkli ve animasyonlu çıktılar gösterir -
 * Sadece dev ve default profillerde çalışır - Veritabanında zaten veri varsa, yeni veri eklemeyi atlar
 *
 * @author BariSD
 * @version 1.0
 */
@Component
@Profile({"dev", "default"})
//@Profile("test")
public class DataInitializer {


    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final FollowRepository followRepository;

    /**
     * Constructor injection ile repository'leri enjekte eder
     *
     * @param userRepository    Kullanıcı işlemleri için repository
     * @param messageRepository Mesaj işlemleri için repository
     */
    public DataInitializer(UserRepository userRepository, MessageRepository messageRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.followRepository = followRepository;
    }

    /**
     * Uygulama başladığında çalışan veri başlatma metodu. Örnek kullanıcılar ve mesajlar oluşturur.
     */
    @PostConstruct
    public void initializeData() {
        try {
            // Veritabanında veri varsa, yeni veri eklemeyi atla
            if (userRepository.count() > 0) {
                printColoredText(YELLOW, "Veritabanında zaten veri var. Örnek veri ekleme atlandı.");
                return;
            }

            // Başlangıç logosu - ASCII art
            printLogo();

            // Animasyonlu başlangıç
            printAnimatedText(CYAN, "Friendship App veritabanı hazırlanıyor...");
            simulateLoading();

            printColoredText(PURPLE, "\n⭐ Örnek veriler veritabanına ekleniyor... ⭐\n");
            Thread.sleep(500);

            // Kullanıcıları oluştur ve ekle
            createAndSaveUsers();

            // Kapanış animasyonu
            Thread.sleep(500);
            printColoredText(PURPLE, "\n🎉 Örnek veriler başarıyla yüklendi! 🎉");

            // Kapanış logosu
            printCompletionBanner();

        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Veri yükleme işlemi kesintiye uğradı: " + e.getMessage());
        }
        catch (Exception e) {
            System.err.println("Veri yükleme sırasında bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Renkli metin yazdırır
     *
     * @param color Metin rengi
     * @param text  Yazdırılacak metin
     */
    private void printColoredText(String color, String text) {
        System.out.println(color + text + COLOR_RESET);
    }

    /**
     * ASCII Logo yazdırır
     */
    private void printLogo() {
        try {
            String[] logo = {
                    BLUE + "  ______    _               _     _     _           " + COLOR_RESET,
                    BLUE + " |  ____|  (_)             | |   | |   (_)          " + COLOR_RESET,
                    BLUE + " | |__ _ __ _  ___ _ __  __| |___| |__  _ _ __      " + COLOR_RESET,
                    BLUE + " |  __| '__| |/ _ \\ '_ \\/ _` / __| '_ \\| | '_ \\ " + COLOR_RESET,
                    CYAN + " | |  | |  | |  __/ | | | (_| \\__ \\ | | | | |_) | " + COLOR_RESET,
                    CYAN + " |_|  |_|  |_|\\___|_| |_|\\__,_|___/_| |_|_| .__/  " + COLOR_RESET,
                    CYAN + "                                          | |       " + COLOR_RESET,
                    CYAN + "                                          |_|       " + COLOR_RESET,
                    YELLOW + "           ☆ ☆ ☆ APP INITIALIZER ☆ ☆ ☆          " + COLOR_RESET
            };

            for (String line : logo) {
                System.out.println(line);
                Thread.sleep(100);
            }
            System.out.println();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Animasyonlu metin yazdırır
     *
     * @param color Metin rengi
     * @param text  Yazdırılacak metin
     */
    private void printAnimatedText(String color, String text) {
        try {
            for (char c : text.toCharArray()) {
                System.out.print(color + c + COLOR_RESET);
                Thread.sleep(50);
            }
            System.out.println();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Animasyonlu yükleme göstergesi
     */
    private void simulateLoading() {
        try {
            String[] animationFrames = {"|", "/", "-", "\\"};
            for (int i = 0; i < 20; i++) {
                String frame = animationFrames[i % animationFrames.length];
                System.out.print("\r" + YELLOW + "[" + frame + "] Veritabanı hazırlanıyor..." + COLOR_RESET);
                Thread.sleep(100);
            }
            System.out.println();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Örnek kullanıcıları oluşturur, kaydeder ve ilgili mesajları ekler
     *
     * @throws InterruptedException Thread.sleep() çağrısından kaynaklanabilir
     */
    private void createAndSaveUsers() throws InterruptedException {
        User user1 = User.builder()
                .username("ahmet")
                .password("123456")
                .email("ahmet@example.com")
                .profilePic("ahmet.jpg")
                .gender(Gender.MALE)
                .phone("5551112233")
                .address("İstanbul, Türkiye")
                .age(28)
                .height(178)
                .weight(75)
                .followerCount(0)
                .followingCount(0)
                .isActive(true)
                .build();

        User user2 = User.builder()
                .username("ayse")
                .password("123456")
                .email("ayse@example.com")
                .profilePic("ayse.jpg")
                .gender(Gender.FEMALE)
                .phone("5552223344")
                .address("Ankara, Türkiye")
                .age(25)
                .height(165)
                .weight(55)
                .followerCount(0)
                .followingCount(0)
                .isActive(true)
                .build();

        User user3 = User.builder()
                .username("mehmet")
                .password("123456")
                .email("mehmet@example.com")
                .profilePic("mehmet.jpg")
                .gender(Gender.MALE)
                .phone("5553334455")
                .address("İzmir, Türkiye")
                .age(32)
                .height(182)
                .weight(80)
                .followerCount(0)
                .followingCount(0)
                .isActive(true)
                .build();

        User user4 = User.builder()
                .username("zeynep")
                .password("123456")
                .email("zeynep@example.com")
                .profilePic("zeynep.jpg")
                .gender(Gender.FEMALE)
                .phone("5554445566")
                .address("Bursa, Türkiye")
                .age(30)
                .height(170)
                .weight(60)
                .followerCount(0)
                .followingCount(0)
                .isActive(true)
                .build();

        User user5 = User.builder()
                .username("ali")
                .password("123456")
                .email("ali@example.com")
                .profilePic("ali.jpg")
                .gender(Gender.MALE)
                .phone("5555556677")
                .address("Antalya, Türkiye")
                .age(35)
                .height(175)
                .weight(78)
                .followerCount(0)
                .followingCount(0)
                .isActive(true)
                .build();

        List<User> users = userRepository.saveAll(Arrays.asList(user1, user2, user3, user4, user5));

        // Kullanıcı ekleme animasyonu
        printProgressBar("Kullanıcılar ekleniyor", 100);
        printColoredText(GREEN, "✓ 3 kullanıcı veritabanına başarıyla eklendi!");

        // Kullanıcı detaylarını yazdır
        printColoredText(BLUE, "\n📋 Kullanıcı Detayları:");
        for (User user : users) {
            printColoredText(CYAN,
                    "  🧑 " + user.getUsername() + " (ID: " + user.getId() + ") - " + user.getGender() + ", " + user.getAge() + " yaş");
            Thread.sleep(300);
        }

        // Kullanıcılar kaydedildikten sonra ID'lerini alabiliriz
        User savedUser1 = users.get(0);
        User savedUser2 = users.get(1);
        User savedUser3 = users.get(2);

        // Örnek mesajlar oluştur ve kaydet
        createAndSaveMessages(savedUser1, savedUser2, savedUser3);
        createAndSaveFollows(users);
    }

    private void createAndSaveFollows(List<User> users) {
        printColoredText(BLUE, "\n👥 Takip ilişkileri oluşturuluyor...");

        // Kullanıcı ID'lerini al
        Long user1Id = users.get(0).getId(); // ahmet
        Long user2Id = users.get(1).getId(); // ayse
        Long user3Id = users.get(2).getId(); // mehmet
        Long user4Id = users.get(3).getId(); // zeynep
        Long user5Id = users.get(4).getId(); // ali

        // Takip ilişkileri oluştur (farklı durumlar için)

        // 1. Karşılıklı takipleşme (
        Follow follow1 = Follow.builder()
                .followerId(user1Id) // ahmet
                .followeeId(user2Id) // ayse'yi takip ediyor
                .status(FollowStatus.OK)
                .build();

        Follow follow2 = Follow.builder()
                .followerId(user2Id) // ayse
                .followeeId(user1Id) // ahmet'i takip ediyor
                .status(FollowStatus.OK)
                .build();

        // 2. Karşılıklı takipleşme (OK-OK)
        Follow follow3 = Follow.builder()
                .followerId(user1Id) // ahmet
                .followeeId(user3Id) // mehmet'i takip ediyor
                .status(FollowStatus.OK)
                .build();

        Follow follow4 = Follow.builder()
                .followerId(user3Id) // mehmet
                .followeeId(user1Id) // ahmet'i takip ediyor
                .status(FollowStatus.OK)
                .build();

        // 3. Tek yönlü takip (OK)
        Follow follow5 = Follow.builder()
                .followerId(user4Id) // zeynep
                .followeeId(user1Id) // ahmet'i takip ediyor
                .status(FollowStatus.OK)
                .build();

        // 4. Bekleyen takip isteği (PENDING)
        Follow follow6 = Follow.builder()
                .followerId(user4Id) // zeynep
                .followeeId(user2Id) // ayse'yi takip etmek istiyor
                .status(FollowStatus.PENDING)
                .build();

        // 5. İptal edilmiş takip (CANCELED)
        Follow follow7 = Follow.builder()
                .followerId(user3Id) // mehmet
                .followeeId(user4Id) // zeynep'i takip etmeyi denemiş ama iptal edilmiş
                .status(FollowStatus.CANCELLED)
                .build();

        // 6. İkinci dereceden bağlantı (Takip önerisi için)
        Follow follow8 = Follow.builder()
                .followerId(user3Id) // mehmet
                .followeeId(user5Id) // ali'yi takip ediyor
                .status(FollowStatus.OK)
                .build();

        // 7. İkinci dereceden bağlantı (Takip önerisi için)
        Follow follow9 = Follow.builder()
                .followerId(user2Id) // ayse
                .followeeId(user4Id) // zeynep'i takip ediyor
                .status(FollowStatus.OK)
                .build();

        // 8. İkinci dereceden bağlantı (Takip önerisi için)
        Follow follow10 = Follow.builder()
                .followerId(user2Id) // ayse
                .followeeId(user5Id) // ali'yi takip ediyor
                .status(FollowStatus.OK)
                .build();

        // Takip ilişkilerini veritabanına kaydet
        List<Follow> follows = followRepository.saveAll(Arrays.asList(
                follow1, follow2, follow3, follow4, follow5,
                follow6, follow7, follow8, follow9, follow10
        ));
        printColoredText(BLUE, "\n👥 Takip İlişkileri Detayları:");

        for (Follow follow : follows) {
            String followerName = users.stream()
                    .filter(u -> u.getId().equals(follow.getFollowerId()))
                    .findFirst()
                    .map(User::getUsername)
                    .orElse("Bilinmeyen");

            String followeeName = users.stream()
                    .filter(u -> u.getId().equals(follow.getFolloweeId()))
                    .findFirst()
                    .map(User::getUsername)
                    .orElse("Bilinmeyen");
            String statusColor = follow.getStatus() == FollowStatus.OK ? GREEN :
                    (follow.getStatus() == FollowStatus.PENDING ? YELLOW : RED);

            printColoredText(CYAN, "  👤 " + followerName + " → " + followeeName +
                    ": " + statusColor + follow.getStatus() + COLOR_RESET);
        }
    }


    /**
     * Kapanış banner'ını yazdırır
     */
    private void printCompletionBanner() {
        try {
            System.out.println();
            String[] banner = {
                    GREEN_BOLD + "  ███████╗██╗███╗   ██╗██╗███████╗██╗  ██╗███████╗██████╗  " + COLOR_RESET,
                    GREEN_BOLD + "  ██╔════╝██║████╗  ██║██║██╔════╝██║  ██║██╔════╝██╔══██╗ " + COLOR_RESET,
                    GREEN_BOLD + "  █████╗  ██║██╔██╗ ██║██║███████╗███████║█████╗  ██║  ██║ " + COLOR_RESET,
                    GREEN_BOLD + "  ██╔══╝  ██║██║╚██╗██║██║╚════██║██╔══██║██╔══╝  ██║  ██║ " + COLOR_RESET,
                    GREEN_BOLD + "  ██║     ██║██║ ╚████║██║███████║██║  ██║███████╗██████╔╝ " + COLOR_RESET,
                    GREEN_BOLD + "  ╚═╝     ╚═╝╚═╝  ╚═══╝╚═╝╚══════╝╚═╝  ╚═╝╚══════╝╚═════╝  " + COLOR_RESET,
                    YELLOW_BOLD + "        Uygulama başlatıldı! Swagger: /swagger-ui.html        " + COLOR_RESET
            };

            for (String line : banner) {
                System.out.println(line);
                Thread.sleep(100);
            }
            System.out.println();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * İlerleme çubuğu yazdırır
     *
     * @param task       Görev açıklaması
     * @param totalSteps Toplam adım sayısı
     */
    private void printProgressBar(String task, int totalSteps) {
        try {
            System.out.print(YELLOW + task + ": " + COLOR_RESET);

            for (int i = 0; i <= totalSteps; i += 5) {
                int percentage = Math.min(100, i);
                int completed = percentage / 2;

                StringBuilder progressBar = new StringBuilder("[");
                for (int j = 0; j < 50; j++) {
                    if (j < completed) {
                        progressBar.append("=");
                    }
                    else if (j == completed) {
                        progressBar.append(">");
                    }
                    else {
                        progressBar.append(" ");
                    }
                }
                progressBar.append("] ");
                progressBar.append(percentage).append("%");

                System.out.print("\r" + YELLOW + task + ": " + COLOR_RESET + progressBar);
                Thread.sleep(20);
            }
            System.out.println();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Kullanıcılar arasında örnek mesajlar oluşturur ve kaydeder
     *
     * @param user1 Birinci kullanıcı
     * @param user2 İkinci kullanıcı
     * @param user3 Üçüncü kullanıcı
     * @throws InterruptedException Thread.sleep() çağrısından kaynaklanabilir
     */
    private void createAndSaveMessages(User user1, User user2, User user3) throws InterruptedException {

        LocalDateTime now = LocalDateTime.now();

        Message message1 = Message.builder()
                .senderId(user1.getId())
                .receiverId(user2.getId())
                .message("Merhaba Ayşe, nasılsın?")
                .sendDate(now.minusDays(1))
                .read(true)
                .build();

        Message message2 = Message.builder()
                .senderId(user2.getId())
                .receiverId(user1.getId())
                .message("İyiyim Ahmet, teşekkür ederim. Sen nasılsın?")
                .sendDate(now.minusDays(1).plusHours(1))
                .read(true)
                .build();

        Message message3 = Message.builder()
                .senderId(user1.getId())
                .receiverId(user2.getId())
                .message("Ben de iyiyim, sağ ol. Bugün hava çok güzel.")
                .sendDate(now.minusDays(1).plusHours(2))
                .read(true)
                .build();

        Message message4 = Message.builder()
                .senderId(user2.getId())
                .receiverId(user1.getId())
                .message("Evet, sonunda güzel bir hava.")
                .sendDate(now.minusDays(1).plusHours(3))
                .read(true)
                .build();

        Message message5 = Message.builder()
                .senderId(user1.getId())
                .receiverId(user3.getId())
                .message("Selam Mehmet, yarın toplantı saat kaçta?")
                .sendDate(now.minusHours(6))
                .read(true)
                .build();

        Message message6 = Message.builder()
                .senderId(user3.getId())
                .receiverId(user1.getId())
                .message("Merhaba Ahmet, toplantı yarın saat 10:00'da.")
                .sendDate(now.minusHours(5))
                .read(true)
                .build();

        Message message7 = Message.builder()
                .senderId(user2.getId())
                .receiverId(user3.getId())
                .message("Merhaba Mehmet, proje dosyalarını gönderebilir misin?")
                .sendDate(now.minusHours(4))
                .read(true)
                .build();

        Message message8 = Message.builder()
                .senderId(user3.getId())
                .receiverId(user2.getId())
                .message("Tabii Ayşe, birazdan göndereceğim.")
                .sendDate(now.minusHours(3))
                .read(true)
                .build();

        Message message9 = Message.builder()
                .senderId(user1.getId())
                .receiverId(user2.getId())
                .message("Ayşe, akşam yemeğe çıkalım mı?")
                .sendDate(now.minusHours(2))
                .read(false)
                .build();

        Message message10 = Message.builder()
                .senderId(user3.getId())
                .receiverId(user1.getId())
                .message("Ahmet, toplantı için sunumu hazırladın mı?")
                .sendDate(now.minusHours(1))
                .read(false)
                .build();


        List<Message> messages = messageRepository.saveAll(Arrays.asList(
                message1, message2, message3, message4, message5,
                message6, message7, message8, message9, message10
        ));


        Thread.sleep(500);
        printProgressBar("Mesajlar ekleniyor", 100);
        printColoredText(GREEN, " ✓ 10 mesaj veritabanına başarıyla eklendi!");


        printColoredText(BLUE, " \n💬 Mesaj Örnekleri:");
        for (int i = 0; i < Math.min(3, messages.size()); i++) {
            Message msg = messages.get(i);
            String senderName = msg.getSenderId().equals(user1.getId()) ? user1.getUsername() :
                    (msg.getSenderId().equals(user2.getId()) ? user2.getUsername() : user3.getUsername());

            String receiverName = msg.getReceiverId().equals(user1.getId()) ? user1.getUsername() :
                    (msg.getReceiverId().equals(user2.getId()) ? user2.getUsername() : user3.getUsername());

            printColoredText(CYAN, "  📩 " + senderName + " → " + receiverName + ": \"" + msg.getMessage() + "\"");
            Thread.sleep(300);
        }
        printColoredText(CYAN, "  ... ve " + (messages.size() - 3) + " mesaj daha.");

    }
}