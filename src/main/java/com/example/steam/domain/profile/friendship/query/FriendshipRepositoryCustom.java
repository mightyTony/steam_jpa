package com.example.steam.domain.profile.friendship.query;

import com.example.steam.domain.profile.friendship.dto.FriendshipReadResponse;
import com.example.steam.domain.profile.friendship.dto.FriendshipSearchResponse;
import com.example.steam.domain.profile.friendship.dto.FriendshipShortViewResponse;
import com.example.steam.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepositoryCustom {
    boolean isFriend(Long myId, Long receiverId);

    Optional<List<FriendshipReadResponse>> findFriendships(Long userId);

    List<FriendshipSearchResponse> getMyFriendshipRequest(User user);

    FriendshipShortViewResponse getMyFriendsListShort(Long userId);
}
