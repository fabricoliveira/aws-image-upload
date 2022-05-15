package com.amigoscode.awsimageupload.datastore;

import com.amigoscode.awsimageupload.model.UserProfile;
import com.amigoscode.awsimageupload.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserProfileDatastore {

    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserProfileDatastore(UserProfileRepository userProfileDatastore) {
        this.userProfileRepository = userProfileDatastore;
    }

    public UserProfile findById(UUID userProfileId) {
        return userProfileRepository.findById(userProfileId);
    }

    public List<UserProfile> getUserProfiles() {
        return userProfileRepository.getUserProfiles();
    }
}
