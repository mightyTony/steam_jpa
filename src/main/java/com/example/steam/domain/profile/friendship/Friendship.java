package com.example.steam.domain.profile.friendship;

import com.example.steam.domain.user.User;
import com.example.steam.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "friendship", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sender_id", "receiver_id"})}
)
public class Friendship extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    private FriendStatus status;

    @Builder
    public Friendship(User sender, User receiver, FriendStatus status) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }

    public void accepted() {
        this.status = FriendStatus.ACCEPTED;
    }

}
