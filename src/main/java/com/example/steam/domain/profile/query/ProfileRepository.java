package com.example.steam.domain.profile.query;

import com.example.steam.domain.profile.Profile;
import com.example.steam.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> , ProfileRepositoryCustom{
    Profile findProfileByUser(User user);

    Optional<Profile> findProfileByUser_Id(Long userId);
}
