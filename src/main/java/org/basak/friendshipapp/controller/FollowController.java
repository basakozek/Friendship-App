package org.basak.friendshipapp.controller;
import lombok.RequiredArgsConstructor;
import org.basak.friendshipapp.dto.response.BaseResponse;
import org.basak.friendshipapp.dto.response.GetAllUsersResponseDto;
import org.basak.friendshipapp.dto.response.GetMyFollowersResponseDto;
import org.basak.friendshipapp.entity.Follow;
import org.basak.friendshipapp.service.FollowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.basak.friendshipapp.constant.EndPoints.*;
@RestController
@RequestMapping(FOLLOW)
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;
    /**
     * HttpStatus : HttpStatus.OK
     * Body
     * Header
     */
    // http://localhost:9090/follow/request/1/2
    // http://localhost:9090/follow/request?followerId=1&followeeId=2
    @PostMapping("/request/{followerId}/{followeeId}")
    public ResponseEntity<?> sendFollowRequest(@PathVariable Long followerId, @PathVariable Long followeeId){
            Follow follow = followService.sendFollowRequest(followerId, followeeId);
            return ResponseEntity.status(HttpStatus.CREATED).body(follow);
    }

    @GetMapping("/get-my-followers")
    public ResponseEntity<BaseResponse<List<GetMyFollowersResponseDto>>> getMyFollowers(@RequestParam Long userId) {
        List<GetMyFollowersResponseDto> followersList=followService.getMyFollowers(userId);
        return ResponseEntity.ok(BaseResponse.<List<GetMyFollowersResponseDto>>builder()
                .data(followService.getMyFollowers(userId))
                .code(200)
                .message("Takipçi listesi başarıyla getirildi")
                .success(true).build());
    }

}
