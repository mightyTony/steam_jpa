package com.example.steam.domain.profile.friendship.query;

import com.example.steam.domain.profile.friendship.FriendStatus;
import com.example.steam.domain.profile.friendship.Friendship;
import com.example.steam.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long>, FriendshipRepositoryCustom {
    List<Friendship> findFriendshipsByReceiverAndStatus(User receiver, FriendStatus status);
}
