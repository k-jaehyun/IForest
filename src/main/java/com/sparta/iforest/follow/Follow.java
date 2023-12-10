package com.sparta.iforest.follow;

import com.sparta.iforest.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn (name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn (name = "receiver_id")
    private User receiver;


    public Follow(User receiver, User sender) {
        this.receiver=receiver;
        this.sender=sender;
    }
}
