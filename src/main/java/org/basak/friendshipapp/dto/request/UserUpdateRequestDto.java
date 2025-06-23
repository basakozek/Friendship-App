package org.basak.friendshipapp.dto.request;

import jakarta.validation.constraints.*;
import org.basak.friendshipapp.entity.Gender;


public record UserUpdateRequestDto(
        @NotNull
        Long id,
        @NotNull(message = "Username boş geçilemez")
        @Size(min = 3, max = 30, message = "Username en az 3 en fazla 30 karakter olmalıdır.")
        String username,
        String password,
        String email,
        String profilePic,
        Gender gender,
        String phone,
        String address,
        Integer age,
        Integer height,
        Integer weight) {
}