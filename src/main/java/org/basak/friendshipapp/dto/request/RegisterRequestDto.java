package org.basak.friendshipapp.dto.request;

import jakarta.validation.constraints.*;

public record RegisterRequestDto(@NotNull(message = "Username boş geçilemez")
                                 @Size(min = 3,max = 30,message = "Username en az 3 en fazla 30 karakter olmalıdır.")
                                 String username,
                                 @NotEmpty @Pattern(regexp = "^.*(?=.{8,64})(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=" +
                                         ".*[@#$%^&+=*.]).*$",
                                         message = "Şifre en az 8 karakter, en fazla 64 karakter, 1 Büyük harf 1 " +
                                                 "Küçük harf, 1 Rakam ve 1 Özel karakter olmalıdır.") String password,
                                 @NotEmpty(message = "Repassword boş geçilemez.") String rePassword,
                                 @Email(message = "Lütfen geçerli email giriniz.") String email,
                                 String profilePic) {
}
