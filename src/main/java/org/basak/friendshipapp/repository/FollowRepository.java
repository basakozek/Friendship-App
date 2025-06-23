package org.basak.friendshipapp.repository;

import org.basak.friendshipapp.entity.Follow;
import org.basak.friendshipapp.entity.FollowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA, spring bootun yapısı gereği her bir interface için gerekli olan Impl sınıflarını generate ederek
 * onların beanlerini application context içine atar. Gerekli olduğunda da application contextten bu beanleri çekerek
 * kullanır.
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    //	## Method Query
//* Bekleyen tüm takip isteklerini bul
    List<Follow> findAllByStatus(FollowStatus status);
    //followRepository.findAllByStatus(FollowStatus.PENDING)

    //* Belirli bir kullanıcının aldığı takip isteklerini listele
    List<Follow> findAllByFolloweeIdAndStatus(Long followeeId, FollowStatus status);
    //followRepository.findAllByFolloweeIdAndStatus(userId,FollowStatus.PENDING)

    //* Belirli bir kullanıcının gönderdiği takip isteklerini listele
    List<Follow> findAllByFollowerIdAndStatus(Long followerId, FollowStatus status);

    //* İsteği kabul edilmiş takipleri bul
    List<Follow> findAllByStatusOrderByIdDesc(FollowStatus status);

    //* İptal edilmiş takipleri bul - belirli bir kullanıcı için
//  List<Follow> findAllByStatus(FollowStatus status);
//* Bekleyen durumundaki takipleri bul - belirli bir kullanıcı için
//  List<Follow> findAllByFolloweeIdAndStatus(Long followeeId, FollowStatus status);
//* Belirli bir takip durumu kontrolü(bir user bir başka user'ı takip ediyor mu? boolean sonuç dönecek.)
    boolean existsByFollowerIdAndFolloweeIdAndStatus(Long followerId, Long followeeId, FollowStatus status);

    //	## JPQL-Native SQL
//* Bir kullanıcının tüm takipçilerini bul (OK statüsündeki)
    @Query("SELECT f FROM Follow f WHERE f.followeeId=?1 AND f.status='OK'")
    List<Follow> kullanicininTumTakipcileri(Long followeeId);

    @Query("SELECT f.followerId FROM Follow f WHERE f.followeeId=?1 AND f.status='OK'")
    List<Follow> findFollowerIdByFolloweeIdAndStatus(Long followeeId);


    //* Bir kullanıcının takip ettiği tüm kullanıcıları bul (OK statüsündeki)
    @Query("SELECT f FROM Follow f WHERE f.followerId=?1 AND f.status='OK'")
    List<Follow> kullanicininTumTakipEttikleri(Long followerId);

    //* Karşılıklı takip eden kullanıcıları bul (arkadaşlar)
    @Query("SELECT f1.followerId " +
            "FROM Follow f1 " +
            "JOIN Follow f2 ON f1.followerId=f2.followeeId AND f1.followeeId=f2.followerId " +
            "WHERE f1.followeeId=?1 AND f1.status='OK' AND f2.status='OK' ")
    List<Long> getFriends(Long followeeId); //5id li user'ın takip ettikleri ve aynı anda 5 nolu useri takip
    // edenler

    //* Bir kullanıcının takipçi sayısını hesapla
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followeeId=?1 AND f.status='OK'")
    Long takipciSayisi(Long followeeId);

    //* Bir kullanıcının takip ettiği kullanıcı sayısını hesapla
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followerId=?1 AND f.status='OK'")
    Long takipEttigiKullaniciSayisi(Long followerId);

    //* En çok takipçisi olan kullanıcıları bul
    @Query("SELECT f.followeeId,COUNT(f) FROM Follow f WHERE f.status='OK'" +
            " GROUP BY f.followeeId ORDER BY COUNT(f) DESC LIMIT 3")
    List<Object[]> enCokTakipcisiOlanlar();


//* Takip önerileri - (takip ettiğin kişilerin takip ettiği ama senin takip etmediğin kişiler)

    @Query("""
					SELECT DISTINCT f2.followeeId FROM Follow f1
					JOIN Follow f2 ON f1.followeeId=f2.followerId
					WHERE f1.followerId=:followerId
					AND f1.status='OK'
					AND f2.status='OK'
					AND f2.followeeId!=:followerId
					EXCEPT
					SELECT f3.followeeId FROM Follow f3
					WHERE f3.followerId=:followerId AND f3.status='OK'
			""")
    List<Long> getFollowSuggestions(Long followerId);

}

