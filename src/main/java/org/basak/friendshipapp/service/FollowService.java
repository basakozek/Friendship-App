package org.basak.friendshipapp.service;

import org.basak.friendshipapp.dto.response.GetAllUsersResponseDto;
import org.basak.friendshipapp.dto.response.GetMyFollowersResponseDto;
import org.basak.friendshipapp.entity.Follow;
import org.basak.friendshipapp.entity.FollowStatus;
import org.basak.friendshipapp.entity.User;
import org.basak.friendshipapp.exception.ErrorType;
import org.basak.friendshipapp.exception.FriendshipException;
import org.basak.friendshipapp.repository.FollowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service temel işlevleri yerine getirmek için kullanılır. Gerekli gördüğü doğrulamalardan geçen bilgileri repository
 * katmanına gönderir. Burada öncelikle repository değişkeni tanımlanmalı ve nesnesi oluşturulmalıdır.
 */
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    /**
     * Dependency Injection ile followRepository Bean'i enjekte ediyoruz. Burada DI'nın 2 farklı yöntemi olduğunu
     * bilmek
     * gerekir. 1. @Autowired anatosyonu ile field üzerine ekleyerek kullanılabilir. Ama önerilmez. 2. Constructor
     * Dependency Injection ile constructor'a parametre geçerek injection yapılabilir. Spring bunu önermektedir. Yalnız
     * circular dependency durumlarında bundan kaçınabiliriz.
     *
     * @param followRepository
     */
    public FollowService(FollowRepository followRepository, UserService userService) {
        this.followRepository = followRepository;
        this.userService = userService;
    }

    public List<Follow> findAll() {
        return followRepository.findAll();
    }

    //    * Takip isteği gönderme  (logic kurgusu gerekli)
    public Follow sendFollowRequest(Long followerId, Long followeeId) {
        if (followerId == null || followeeId == null) {
            return null;
        }

        //userService.findById(followerId).orElseThrow(() -> new RuntimeException("Takip eden user id geçersiz!"));
        //userService.findById(followeeId).orElseThrow(() -> new RuntimeException("Takip edilen user id geçersiz!"));
        if(!userService.existsById(followerId)|| !userService.existsById(followeeId)){
            throw new FriendshipException(ErrorType.FOLLOW_USERID_NOT_FOUND);
        }
        //kendini takip edememeli
        if (followerId.equals(followeeId)) {
            throw new FriendshipException(ErrorType.FOLLOW_USER_CANNOT_FOLLOW_SELF);
        }
        //Eğer takip ediyorsa takip edemez.
        if (followRepository.existsByFollowerIdAndFolloweeIdAndStatus(followerId, followeeId, FollowStatus.OK)) {
            throw new FriendshipException(ErrorType.FOLLOW_ALREADY_FOLLOWED);
        }
        if (followRepository.existsByFollowerIdAndFolloweeIdAndStatus(followerId, followeeId, FollowStatus.PENDING)) {
            throw new FriendshipException(ErrorType.FOLLOW_ALREADY_SENT_REQUEST);
        }

        return saveFollow(followerId, followeeId);
    }

    public Follow saveFollow(
            Long followerId,
            Long followeeId) {
        Follow follow = Follow.builder()
                .followerId(followerId)
                .followeeId(followeeId)
                .status(FollowStatus.PENDING)
                .build();
        return followRepository.save(follow);
    }

    //    * Takip isteğini kabul etme  (logic kurgusu gerekli)
	/*
	Takip isteği var mı? yoksa hata.
	Takip Pendingde mi? değilse hata.
	Bu istek bu userId'ye mi gelmiş? değilse hata...
	 */
    @Transactional
    public Follow acceptFollowRequest(Long followId, Long currentUserId) {
        Follow follow = followRepository.findById(followId)
                .orElseThrow(() -> new IllegalStateException("Takip isteği bulunamadı."));

        if (follow.getStatus() != FollowStatus.PENDING) {
            throw new IllegalStateException("Yalnızca bekleyen istekler kabul edilebilir.");
        }

        if (!follow.getFolloweeId().equals(currentUserId)) {
            throw new IllegalStateException("Bu takip isteğini kabul etme yetkiniz yok.");
        }

        follow.setStatus(FollowStatus.OK);
        User follower = userService.findById(follow.getFollowerId())
                .orElseThrow(() -> new IllegalStateException("Takip eden user bulunamadı."));
        User followee = userService.findById(follow.getFolloweeId())
                .orElseThrow(() -> new IllegalStateException("Takip edilen user bulunamadı."));
        followee.setFollowerCount(followee.getFollowerCount() + 1);
        follower.setFollowingCount(follower.getFollowingCount() + 1);
        Follow savedFollow = followRepository.save(follow);
        userService.save(follower);
        userService.save(followee);
        return savedFollow;
    }
//    * Takip isteğini reddetme  (logic kurgusu gerekli)

    public Follow rejectFollowRequest(Long followId, Long currentUserId) {
        Follow follow = followRepository.findById(followId)
                .orElseThrow(() -> new IllegalStateException("Takip isteği bulunamadı."));

        if (follow.getStatus() != FollowStatus.PENDING) {
            throw new IllegalStateException("Yalnızca bekleyen istekler reddedilebilir.");
        }

        if (!follow.getFolloweeId().equals(currentUserId)) {
            throw new IllegalStateException("Bu takip isteğini reddetme yetkiniz yok.");
        }

        follow.setStatus(FollowStatus.CANCELLED);
        return followRepository.save(follow);
    }


    //    * Takibi iptal etme(takipten çıkma)  (logic kurgusu gerekli)
//	1. Kural istek status OK olmalı, değilse hata
//	2. Kural takipçi ve takip edilen sayısı güncellemeli
    @Transactional
    public Follow unFollow(Long followId, Long currentUserId) {
        Follow follow = followRepository.findById(followId)
                .orElseThrow(() -> new IllegalStateException("Takip isteği bulunamadı."));

        if (follow.getStatus() != FollowStatus.OK) {
            throw new IllegalStateException("Yalnızca takip ettiğiniz user'ı takipten çıkabilirsiniz.");
        }

        if (!follow.getFollowerId().equals(currentUserId)) {
            throw new IllegalStateException("Bu user'ı takipten çıkarma yetkiniz yok.");
        }

        follow.setStatus(FollowStatus.CANCELLED);
        User follower = userService.findById(follow.getFollowerId())
                .orElseThrow(() -> new IllegalStateException("Takip eden user bulunamadı."));
        User followee = userService.findById(follow.getFolloweeId())
                .orElseThrow(() -> new IllegalStateException("Takip edilen user bulunamadı."));
        followee.setFollowerCount(followee.getFollowerCount() - 1);
        follower.setFollowingCount(follower.getFollowingCount() - 1);
        Follow savedFollow = followRepository.save(follow);
        userService.save(follower);
        userService.save(followee);
        return savedFollow;
    }


    //			* Bir kullanıcının takipçilerini getirme  (stream olabilir.)
    public List<User> getFollowers(Long userId) {
        List<Follow> follows = followRepository.kullanicininTumTakipcileri(userId);
        return follows.stream()
                .map(follow -> userService.findById(follow.getFollowerId()).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }


    //    * Bir kullanıcının takip ettiği kullanıcıları getirme (stream olabilir.)
    public List<User> getFollowees(Long userId) {
        List<Follow> follows = followRepository.kullanicininTumTakipEttikleri(userId);
        return follows.stream()
                .map(follow -> userService.findById(follow.getFolloweeId()).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }


    //    * Karşılıklı takipleşen kullanıcıları getirme (arkadaşlar) (direkt repositoryde metod çağrımları)
    public List<User> getFriends(Long userId) {
        List<Long> longs = followRepository.getFriends(userId);
        return userService.findAllById(longs);
    }

    //			* İsteği kabul edilmiş takipleri getirme (direkt repositoryde metod çağrımları)
    public List<Follow> getAcceptedFollows(Long userId) {
        return followRepository.findAllByFolloweeIdAndStatus(userId, FollowStatus.OK);
    }

    //    * İptal edilmiş takipleri getirme (direkt repositoryde metod çağrımları)
    public List<Follow> getRejectedFollows(Long userId) {

        return followRepository.findAllByFolloweeIdAndStatus(userId, FollowStatus.CANCELLED);
    }

    //    * Bekleyen takip isteklerini getirme (direkt repositoryde metod çağrımları)
    public List<Follow> getPendingFollows() {
        return followRepository.findAllByStatus(FollowStatus.PENDING);
    }

    //    * Bir kullanıcının aldığı bekleyen takip isteklerini getirme (direkt repositoryde metod çağrımları)
    public List<Follow> getPendingFollows(Long userId) {
        return followRepository.findAllByFolloweeIdAndStatus(userId, FollowStatus.PENDING);
    }

    //    * Bir kullanıcının takipçi sayısını getirme (direkt repositoryde metod çağrımları)
    public Long getFollowerCount(Long userId) {
        return followRepository.takipciSayisi(userId);
    }

    //    * Bir kullanıcının takip ettiği kullanıcı sayısını getirme (direkt repositoryde metod çağrımları)
    public Long getFolloweeCount(Long userId) {
        return followRepository.takipEttigiKullaniciSayisi(userId);
    }

    //    * En çok takipçisi olan kullanıcıları getirme (direkt repositoryde metod çağrımları)
    public List<Object[]> getMostFollowedUsers() {
        return followRepository.enCokTakipcisiOlanlar();
    }

    //    * Takip önerileri getirme
    public List<User> getFollowSuggestions(Long userId) {
        List<Long> longs = followRepository.getFollowSuggestions(userId);
        return userService.findAllById(longs);
    }

    public List<GetMyFollowersResponseDto> getMyFollowers(Long userId) {
       return null;
    }
}


