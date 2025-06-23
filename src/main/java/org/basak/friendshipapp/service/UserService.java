package org.basak.friendshipapp.service;

import jakarta.validation.Valid;
import org.basak.friendshipapp.dto.UserDto;
import org.basak.friendshipapp.dto.request.RegisterRequestDto;
import org.basak.friendshipapp.dto.request.UserUpdateRequestDto;
import org.basak.friendshipapp.dto.response.GetAllUsersResponseDto;
import org.basak.friendshipapp.dto.response.RegisterResponseDto;
import org.basak.friendshipapp.entity.Gender;
import org.basak.friendshipapp.entity.User;
import org.basak.friendshipapp.mapper.UserMapper;
import org.basak.friendshipapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(
            String username,
            String password,
            String email,
            String profilePic,
            Gender gender,
            Integer age) {
        User user = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .profilePic(profilePic)
                .gender(gender)
                .age(age).build();

        return userRepository.save(user);
    }
    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAllFemaleUser() {
        return userRepository.findAllByGender(Gender.FEMALE);
    }

    public List<User> findAllByProfilePicIsNotNull() {
        return userRepository.findAllByProfilePicIsNotNull();
    }

    public List<User> findAllMaleAndGt25() {
        return userRepository
                .findAllByGenderAndAgeGreaterThan(
                        Gender.MALE,
                        25);
    }
    //Anıl

    //Başak
    public List<User> findAllByUsernameStartingWith(String username) {
        return userRepository.findAllByUsernameStartingWith(username);
    }

    public List<User> findAllByUsernameStartsWith(String username) {
        return userRepository.findAllByUsernameStartsWith(username);
    }

    public List<User> findAllByUsernameIsStartingWith(String username) {
        return userRepository.findAllByUsernameIsStartingWith(username);
    }

    public List<User> findAllByUsernameContainingAndAgeBetween(String username, int min, int max) {
        return userRepository.findAllByUsernameContainingAndAgeBetween(username, min, max);
    }

    //Eren
    public List<User> findAllByUsernameIsContaining() {
        return userRepository.findAllByUsernameIsContaining("a");
    }

    public List<User> findAllByUsernameIsNotContaining() {
        return userRepository.findAllByUsernameIsNotContaining("a");
    }

    public List<User> findAllByUsernameContainingIgnoreCase() {
        return userRepository.findAllByUsernameContainingIgnoreCase("A");
    }

    public List<User> findAllByIsActiveTrue() {
        return userRepository.findAllByIsActiveTrue();
    }

    //Mehmet Ali
    public List<User> findAllByUsernameNotContainingIgnoreCase() {
        return userRepository.findAllByUsernameNotContainingIgnoreCase("emre");
    }

    public List<User> findAllByUsernameContaining() {
        return userRepository.findAllByUsernameContaining("e");
    }

    public List<User> findAllByUsernameNotContaining() {
        return userRepository.findAllByUsernameNotContaining("e");
    }

    public List<User> findAllByIsActiveFalse() {
        return userRepository.findAllByIsActiveFalse();
    }

    //Mert
    public List<User> findAllByUsernameContains() {
        return userRepository.findAllByUsernameContains("a");
    }

    public List<User> findAllByUsernameNotContains() {
        return userRepository.findAllByUsernameNotContains("a");
    }

    public List<User> findAllByUsernameEndingWith() {
        return userRepository.findAllByUsernameEndingWith("a");
    }

    public List<User> findAllByIsActive() {
        return userRepository.findAllByIsActive(true);
    }

    //Onur
    public List<User> findAllByOrderByAge() {
        return userRepository.findAllByOrderByAge();
    }

    public List<User> findAllByUsernameEndsWith(String username) {
        return userRepository.findAllByUsernameEndsWith(username);
    }

    public List<User> findAllByUsernameIsEndingWith(String username) {
        return userRepository.findAllByUsernameIsEndingWith(username);
    }

    public List<User> findAllByProfilePicIsNull() {
        return userRepository.findAllByProfilePicIsNull();
    }

    //Yunus
    public User findFirstByAge() {
        return userRepository.findFirstByOrderByAgeDesc();
    }

    public List<User> findUsernameOrderByAge(String username) {
        return userRepository.findAllByUsernameOrderByAgeDesc(username);
    }

    public List<User> findTop3OrderByAgeDesc() {
        return userRepository.findTop3ByOrderByAgeDesc();
    }

    public List<User> findUsersById(List<Long> ids) {
        return userRepository.findAllByIdIn(ids);
    }

    public List<User> banaAdiSuOlanKullanicilariGetir(String ad) {
        return userRepository.banaAdiSuOlanKullanicilariGetir(ad);
    }

    public List<User> yasiDegerdenBuyukOlanlariGetir(int yas) {
        return userRepository.yasiDegerdenBuyukOlanlariGetir(yas);
    }

    public boolean buKullaniciVarMi(String username, String password) {
        return userRepository.buKullaniciVarMi(username, password);
    }

    public List<UserDto> tumKullanicilariGetir() {
        return userRepository.tumKullanicilariGetir();
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public List<User> findAllById(List<Long> longs) {
        return userRepository.findAllById(longs);
    }

    public void createUser(String username, String password, String email, String profilePic) {
        userRepository.save(User.builder()
                .username(username)
                .password(password)
                .email(email)
                .profilePic(profilePic).build());
    }

    public RegisterResponseDto register(RegisterRequestDto dto) {

        User savedUser = userRepository.save(User.builder()
                .username(dto.username())
                .password(dto.password())
                .email(dto.email())
                .profilePic(dto.profilePic()).build());
        return new RegisterResponseDto(savedUser.getUsername(), savedUser.getProfilePic());
    }

    public List<GetAllUsersResponseDto> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public boolean existsById(Long followerId) {
        return userRepository.existsById(followerId);
    }
    public void updateUserProfile(@Valid UserUpdateRequestDto dto) {
        //dto'dan(kaynak) usera(hedef) dönüşüm:
		/*User user = User.builder()
		                .id(dto.id())
		                .username(dto.username())
		                .password(dto.password())
		                .profilePic(dto.profilePic())
		                .email(dto.email())
		                .gender(dto.gender())
		                .phone(dto.phone())
		                .address(dto.address())
		                .age(dto.age())
		                .height(dto.height())
		                .weight(dto.weight())
		                .build();*/
        User user = userRepository.findById(dto.id()).get();
        UserMapper.INSTANCE.updateUserFromDto(dto, user);
        userRepository.save(user); //bu işlem kaydetmez, günceller. çünkü entity içinde id değeri mevcuttur.
    }

}