package org.basak.friendshipapp.dto.request;
import jakarta.validation.constraints.*;

public record MessageRequestDto(
        @NotNull(message = "Gönderen ID boş olamaz.")
        Long senderId,

        @NotNull(message = "Alıcı ID boş olamaz.")
        Long receiverId,

        @NotBlank(message = "Mesaj içeriği boş olamaz.")
        @Size(min=10, max = 500,message = "Mesaj içeriği min:10 max:500 karakter olmalıdır.")
        String message
) { }
