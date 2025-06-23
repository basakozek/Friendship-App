package org.basak.friendshipapp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;



@AllArgsConstructor
@Getter
public enum ErrorType {
    INTERNAL_SERVER_ERROR(500,"Sunucuda beklenmeyen hata", HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_ERROR(400,"Girilen parametreler hatalıdır. Kontrol ediniz.",HttpStatus.BAD_REQUEST),
    FOLLOW_USERID_NOT_FOUND(5001, "userId yanlış girildi",HttpStatus.BAD_REQUEST),
    FOLLOW_USER_CANNOT_FOLLOW_SELF(5002, "Kullanıcı kendini takip edemez", HttpStatus.BAD_REQUEST),
    FOLLOW_ALREADY_FOLLOWED(5003, "Bu kullanıcıyı zaten takip ediyorsunuz", HttpStatus.BAD_REQUEST),
    FOLLOW_ALREADY_SENT_REQUEST(5002, "Bu kullanıcıya daha önce takip isteği gönderdiniz", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatus httpStatus;
}
