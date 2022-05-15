package com.amigoscode.awsimageupload.repository;

import com.amigoscode.awsimageupload.model.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserProfileRepository {

    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

    static {
        USER_PROFILES.add(new UserProfile(UUID.fromString("6522fa30-ad8d-4967-8d54-7017b5ac4f8e"), "XPTO", null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("d6d511e0-b857-4dc5-8eb9-ddc1e0754f42"), "XPTZ", null));
    }

    public List<UserProfile> getUserProfiles() {
        return USER_PROFILES;
    }

    public UserProfile findById(UUID userProfileId) {
        return USER_PROFILES
                .stream()
                .filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found.", userProfileId)));
    }
}
