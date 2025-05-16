package com.example.steam.domain.notification.event;

import com.example.steam.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendEventNotification {
    private User receiver;
    private User user;
}
