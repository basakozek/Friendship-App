package org.basak.friendshipapp.repository;

import org.basak.friendshipapp.dto.UserDto;
import org.basak.friendshipapp.dto.response.GetAllUsersResponseDto;
import org.basak.friendshipapp.entity.Gender;
import org.basak.friendshipapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByGender(Gender gender);
    //profilePic null olmayan userları listeleyiniz.
    List<User> findAllByProfilePicIsNotNull();
    //belirli bir cinsiyette ve belirli bir yaşın üzerindeki userları listeleyen metodu yazın.
    //birden fazla sorgu koşulunuz varsa and veya or kullanmalısınız.
    List<User> findAllByGenderAndAgeGreaterThan(Gender gender, int age);

    // Aşağıdaki sorgudan itibaren SERVICE ve CONTROLLER katmanlarına ekleme yapınız.

    //username göre arama yapıp user dönecek metodu yazınız.
    //SELECT * FROM tbl_user where username = ?
    //bu sorgudan geriye tek bir user dönebilir. veya null dönebilir.
    //User findByUsername(String username);
    Optional<User> findOptionalByUsername(String username); //Tuğberk1

    //integer alana göre : yaşa göre sorgular
    List<User> findAllByAge(int age); //Tuğberk2 // = parametrede verilen yaşa eşit yaşa sahip olanları döner
    List<User> findAllByAgeLessThanEqual(int age);//Tuğberk3 // <= belirtilen yaştan küçük veya eşit olanları döner
    List<User> findAllByAgeGreaterThanEqual(int age);//Anıl1 // >= belirtilen yaştan büyük veya eşit olanları döner
    List<User> findAllByAgeLessThan(int age);//Anıl2  // <
    List<User> findAllByAgeGreaterThan(int age);//Anıl3  // >

    //string alana göre: username
    //aranan ifade ile başlayanlar: text%
    List<User> findAllByUsernameStartingWith(String username);//Başak1 // bununla başlayanlar
    List<User> findAllByUsernameStartsWith(String username);//Başak2
    List<User> findAllByUsernameIsStartingWith(String username);//Başak3
    // % text %
    List<User> findAllByUsernameIsContaining(String username);//Eren1
    List<User> findAllByUsernameIsNotContaining(String username);//Eren2
    List<User> findAllByUsernameContainingIgnoreCase(String username);//Eren3
    List<User> findAllByUsernameNotContainingIgnoreCase(String username); //MehmetAli1
    List<User> findAllByUsernameContaining(String username); //MehmetAli2
    List<User> findAllByUsernameNotContaining(String username); //MehmetAli3
    List<User> findAllByUsernameContains(String username); //Mert1
    List<User> findAllByUsernameNotContains(String username);//Mert2
    // %text
    List<User> findAllByUsernameEndingWith(String username);//Mert3
    List<User> findAllByUsernameEndsWith(String username);//Onur1
    List<User> findAllByUsernameIsEndingWith(String username);//Onur2

    //Order by
    //SELECT * FROM tbl_user ORDER BY age
    List<User> findAllByOrderByAge();//Onur3
    //SELECT * FROM tbl_user ORDER BY age DESC LIMIT 1
    User findFirstByOrderByAgeDesc(); //Yunus1
    //SELECT * FROM tbl_user WHERE username=? ORDER BY age DESC
    List<User> findAllByUsernameOrderByAgeDesc(String username);//Yunus2
    //SELECT * FROM tbl_user ORDER BY age DESC LIMIT 3
    List<User> findTop3ByOrderByAgeDesc();//Yunus3
    //en küçük yaşa sahip kullanıcıyı getir:
    User findTopByOrderByAge(); //Tuğberk4
    //SELECT * FROM tbl_user WHERE age>20 AND age<30
    List<User> findAllByAgeBetween(int min, int max); //Anil4 //[min,max]
    //SELECT * FROM tbl_user WHERE username LIKE '%?%' AND age BETWEEN ? and ?
    List<User> findAllByUsernameContainingAndAgeBetween(String username, int min, int max); //Başak4

    //boolean bir alan için: isActive
    List<User> findAllByIsActiveTrue(); //Eren4 //değeri true olanları döner.
    List<User> findAllByIsActiveFalse(); //MehmetAli4 //değeri false olanları döner.
    List<User> findAllByIsActive(boolean isActive);//Mert4 //değeri parametreye eşit olanları döner.

    //null olan kayıtları getir
    List<User> findAllByProfilePicIsNull(); //Onur4
//	List<User> findAllByProfilePicIsNotNull();

    //in
    //SELECT * FROM tbl_user WHERE id IN(3,5,7)
    List<User> findAllByIdIn(List<Long> ids); //Yunus4 //aranacak id'ler list olarak verilmeli.

    /**
     * JPQL -> Jakarta Persistence Query Language
     * HQL -> Hibernate Query Language
     * Native SQL -> Bildiğiniz Structured Query Language
     */
    //jpql
    @Query("SELECT u FROM User u WHERE u.username= ?1")
    List<User> banaAdiSuOlanKullanicilariGetir(String ad);

    @Query("SELECT u FROM User u WHERE u.age=?1 AND u.email LIKE ?2")
    List<User> banaYasiSuVeMailiSuOlanKullanicilariGetir(int yas,String mail);

    //nativesql
    @Query(nativeQuery = true,value = "SELECT * FROM tbl_user WHERE age>?1 ")
    List<User> yasiDegerdenBuyukOlanlariGetir(int yas);

    //sql sorgusu boolean bir sonuc dönebilir mi?
    //username, password
    // SELECT  FROM tbl_user WHERE username=? AND password=?
    @Query(nativeQuery = true,value = "SELECT COUNT(*)>0 FROM tbl_user WHERE username=?1 AND password=?2")
    boolean buKullaniciVarMi(String username,String password);

    //  dto -> data transfer object, Controller: view ->
    @Query("SELECT new org.basak.friendshipapp.dto.UserDto(u.username,u.profilePic,u.age) FROM User u")
    List<UserDto> tumKullanicilariGetir();

    @Query("SELECT new org.basak.friendshipapp.dto.response.GetAllUsersResponseDto(u.username,u.profilePic,u.gender) FROM User u")
    List<GetAllUsersResponseDto> getAllUsers();


}


