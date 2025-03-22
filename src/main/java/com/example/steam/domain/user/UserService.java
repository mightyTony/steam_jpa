package com.example.steam.domain.user;

import com.example.steam.exception.ErrorCode;
import com.example.steam.exception.SteamException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void isExisted(User user) {
        userRepository.findById(user.getId())
                .orElseThrow(()-> new SteamException(ErrorCode.NOT_FOUND_USER_NAME));
    }

    public void isExistedBoolean(Long userId) {
        if(!userRepository.existsById(userId)) {
            throw new SteamException(ErrorCode.NOT_FOUND_USER_NAME);
        }
    }
}
