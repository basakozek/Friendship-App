package org.basak.friendshipapp.dto.response;

public record MessageResponseDto(Long senderId, Long receiverId, String message) {

}
