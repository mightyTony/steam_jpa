package com.example.steam.domain.notification.event;

import com.example.steam.domain.profile.Profile;
import com.example.steam.domain.user.User;
import lombok.Getter;

@Getter
public class CommentWriteEvent {
    private final User writer;
    private final Profile profile;

    public CommentWriteEvent(User writer, Profile profile) {
        this.writer = writer;
        this.profile = profile;
    }
}
