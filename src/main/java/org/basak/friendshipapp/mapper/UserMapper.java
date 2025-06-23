package org.basak.friendshipapp.mapper;

import org.basak.friendshipapp.dto.request.UserUpdateRequestDto;
import org.basak.friendshipapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class );
    //dto'dan usera dönüştürecek:
    User fromDto(final UserUpdateRequestDto dto);
    void updateUserFromDto(UserUpdateRequestDto dto, @MappingTarget User user);
}