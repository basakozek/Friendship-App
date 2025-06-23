package org.basak.friendshipapp.controller;

import jakarta.validation.Valid;
import org.basak.friendshipapp.dto.UserDto;
import org.basak.friendshipapp.dto.request.RegisterRequestDto;
import org.basak.friendshipapp.dto.request.UserUpdateRequestDto;
import org.basak.friendshipapp.dto.response.BaseResponse;
import org.basak.friendshipapp.dto.response.GetAllUsersResponseDto;
import org.basak.friendshipapp.dto.response.RegisterResponseDto;
import org.basak.friendshipapp.entity.Gender;
import org.basak.friendshipapp.entity.User;
import org.basak.friendshipapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.basak.friendshipapp.constant.EndPoints.*;

@RestController
@RequestMapping(USER)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * http://localhost:9090/user/save
     *
     * @return
     */
    @GetMapping(SAVE)
    public String save() {
        userService.save("Arda", "123", "arda@gmail.com", "arda.jpg", Gender.MALE, 20);
        userService.save("Emre", "123", "emre@gmail.com", "emre.jpg", Gender.MALE, 26);
        userService.save("Can", "123", "can@gmail.com", null, Gender.MALE, 32);
        userService.save("Cenk", "123", "cenk@gmail.com", "cenk.jpg", Gender.MALE, 40);
        userService.save("Su", "123", "su@gmail.com", "su.jpg", Gender.FEMALE, 35);
        return "5 users Saved";
    }

    /**
     * http://localhost:9090/user/findall
     *
     * @return
     */
    @GetMapping(FINDALL)
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/find-all-female")
    public List<User> findAllFemaleUser() {
        return userService.findAllFemaleUser();
    }

    @GetMapping("/findAllByProfilePicIsNotNull")
    public List<User> findAllByProfilePicIsNotNull() {
        return userService.findAllByProfilePicIsNotNull();
    }

    @GetMapping("/Find-all-male-25")
    public List<User> findAllMaleAndGt25() {
        return userService.findAllMaleAndGt25();
    }

    /**
     * başak
     *
     * @param username
     * @return
     */
    @GetMapping("/findAllByUsernameStartingWith")
    public List<User> findAllByUsernameStartingWith(String username) {
        return userService.findAllByUsernameStartingWith(username);
    }

    @GetMapping("/findAllByUsernameStartsWith")
    public List<User> findAllByUsernameStartsWith(String username) {
        return userService.findAllByUsernameStartsWith(username);
    }

    @GetMapping("/findAllByUsernameIsStartingWith")
    public List<User> findAllByUsernameIsStartingWith(String username) {
        return userService.findAllByUsernameIsStartingWith(username);
    }

    @GetMapping("/findAllByUsernameContainingAndAgeBetween")
    public List<User> findAllByUsernameContainingAndAgeBetween(String username, int min, int max) {
        return userService.findAllByUsernameContainingAndAgeBetween(username, min, max);
    }

    //Eren
    @GetMapping("findAllByUsernameIsContaining")
    public List<User> findAllByUsernameIsContaining() {
        return userService.findAllByUsernameIsContaining();
    }

    @GetMapping("findAllByUsernameIsNotContaining")
    public List<User> findAllByUsernameIsNotContaining() {
        return userService.findAllByUsernameIsNotContaining();
    }

    @GetMapping("findAllByUsernameContainingIgnoreCase")
    public List<User> findAllByUsernameContainingIgnoreCase() {
        return userService.findAllByUsernameContainingIgnoreCase();
    }

    @GetMapping("findAllByIsActiveTrue")
    public List<User> findAllByIsActiveTrue() {
        return userService.findAllByIsActiveTrue();
    }

    //Mehmet Ali
    @GetMapping("/findAllByUsernameNotContainingIgnoreCase")
    public List<User> findAllByUsernameNotContainingIgnoreCase() {
        return userService.findAllByUsernameNotContainingIgnoreCase();
    }

    @GetMapping("/findAllByUsernameContaining")
    public List<User> findAllByUsernameContaining() {
        return userService.findAllByUsernameContaining();
    }

    @GetMapping("/findAllByUsernameNotContaining")
    public List<User> findAllByUsernameNotContaining() {
        return userService.findAllByUsernameNotContaining();
    }

    @GetMapping("/findAllByIsActiveFalse")
    public List<User> findAllByIsActiveFalse() {
        return userService.findAllByIsActiveFalse();
    }

    //Mert
    @GetMapping("/search")
    public List<User> findAllByUsernameContains() {
        return userService.findAllByUsernameContains();
    }

    @GetMapping("/search-not")
    public List<User> findAllByUsernameNotContains() {
        return userService.findAllByUsernameNotContains();
    }

    @GetMapping("/search-ending")
    public List<User> findAllByUsernameEndingWith() {
        return userService.findAllByUsernameEndingWith();
    }

    @GetMapping("/search-active")
    public List<User> findAllByIsActive() {
        return userService.findAllByIsActive();
    }

    //Onur
    @GetMapping("/findAllByOrderByAge")
    public List<User> findAllByOrderByAge() {
        return userService.findAllByOrderByAge();
    }

    @GetMapping("/findAllByUsernameEndsWith")
    public List<User> findAllByUsernameEndsWith() {
        return userService.findAllByUsernameEndsWith("an");
    }

    @GetMapping("/findAllByUsernameIsEndingWith")
    public List<User> findAllByUsernameIsEndingWith() {
        return userService.findAllByUsernameIsEndingWith("a");
    }

    @GetMapping("/findAllByProfilePicIsNull")
    public List<User> findAllByProfilePicIsNull() {
        return userService.findAllByProfilePicIsNull();
    }


    //Yunus
    @GetMapping("/find-first-age")
    public User findFirstAge() {
        return userService.findFirstByAge();
    }


    @GetMapping("/find-username-order-by-age")
    public List<User> findUsernameOrderByAge(String username) {
        return userService.findUsernameOrderByAge(username);
    }

    @GetMapping("/find-top3-order-by-age")
    public List<User> findTop3OrderByAgeDesc() {
        return userService.findTop3OrderByAgeDesc();
    }

    @GetMapping("/find-all-id-in")
    public List<User> findUsersById(List<Long> ids) {
        return userService.findUsersById(ids);
    }

    @GetMapping("/banaAdiSuOlanKullanicilariGetir")
    public List<User> banaAdiSuOlanKullanicilariGetir(String ad) {
        return userService.banaAdiSuOlanKullanicilariGetir(ad);
    }

    @GetMapping("/yasiDegerdenBuyukOlanlariGetir")
    public List<User> yasiDegerdenBuyukOlanlariGetir(int yas) {
        return userService.yasiDegerdenBuyukOlanlariGetir(yas);
    }

    @GetMapping("/buKullaniciVarMi")
    public boolean buKullaniciVarMi(String username, String password) {
        return userService.buKullaniciVarMi(username, password);
    }

    @GetMapping("/tumKullanicilariGetir")
    public List<UserDto> tumKullanicilariGetir() {
        return userService.tumKullanicilariGetir();
    }

    @PostMapping("/login")
    public void doLogin(String username, String password) {
        System.out.println(username + " " + password);
    }

    //create-user endpoint'ine istek atınca bir user oluşturacak metod yazınız.
    //register.html sayfasını da tasarlayın.
    //bu metod dışarıdan username,password,email,profilePic alanlarını almalı.
    @PostMapping("/create-user")
    public void createUser(String username, String password, String email, String profilePic) {
        userService.createUser(username, password, email, profilePic);
    }

    //tüm userları dto şeklinde dönecek bir get-all-users adında endpoint yazınız.
    //dto'da username, profilePic, gender değerlerini dönelim.
    @GetMapping("/get-all-users")
    public ResponseEntity<List<GetAllUsersResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/get-all-users-base-response")
    public ResponseEntity<BaseResponse<List<GetAllUsersResponseDto>>> getAllUsersBaseResponse() {
        return ResponseEntity.ok(
                BaseResponse.<List<GetAllUsersResponseDto>>builder()
                        .success(true)
                        .code(200)
                        .message("Kullanıcı listesi başarılı bir şekilde getirildi.")
                        .data(userService.getAllUsers())
                        .build()
        );
    }
    @PostMapping(REGISTER)
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto dto) {
        if (!dto.password().equals(dto.rePassword()))
            return ResponseEntity.badRequest().body(null);
        return ResponseEntity.ok(userService.register(dto));
    }

    @PatchMapping("/update")
    public ResponseEntity<BaseResponse<Boolean>> update(@RequestBody @Valid UserUpdateRequestDto dto) {
        // Yeni istek: boş gelen değerler güncelenmemeli, eski değer kalmalı.
        userService.updateUserProfile(dto);
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(true)
                .code(200)
                .data(true)
                .message("Güncelleme Başarılı")
                .build());
    }


}