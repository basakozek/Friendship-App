package org.basak.friendshipapp.dto.response;

import org.basak.friendshipapp.entity.Gender;

public record GetAllUsersResponseDto(String username, String profilePic, Gender gender) {
}
