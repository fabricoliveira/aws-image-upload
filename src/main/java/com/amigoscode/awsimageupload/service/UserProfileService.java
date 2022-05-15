package com.amigoscode.awsimageupload.service;

import com.amigoscode.awsimageupload.datastore.UserProfileDatastore;
import com.amigoscode.awsimageupload.filestore.FileStore;
import com.amigoscode.awsimageupload.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class UserProfileService {

    private final UserProfileDatastore userProfileDatastore;
    private final FileStore fileStore;
    private final String bucketName;

    @Autowired
    public UserProfileService(
            UserProfileDatastore userProfileDatastore,
            FileStore fileStore,
            @Value("${aws.s3.bucket.name}") String bucketName) {
        this.userProfileDatastore = userProfileDatastore;
        this.fileStore = fileStore;
        this.bucketName = bucketName;
    }

    public List<UserProfile> getUserProfiles() {
        return userProfileDatastore.getUserProfiles();
    }

    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
        // todo 1 and 2
        validateFile(file);

        // todo 3. validate if the user exists in our database
        UserProfile user = userProfileDatastore.findById(userProfileId);

        // todo 4. extract metadata from the file
        Map<String, String> metadata = getFileMetadata(file);

        // todo 5. store the image in s3 and update database (userProfileImageLink) with s3 image link
        String path = String.format("%s/%s", bucketName, userProfileId);
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
        try {
            fileStore.save(path, filename, file.getInputStream(), Optional.of(metadata));
            user.setUserProfileImageLink(filename);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public byte[] downloadUserProfileImage(UUID userProfileId) {
        String path = String.format("%s/%s", bucketName, userProfileId);
        String key = userProfileDatastore.findById(userProfileId).getUserProfileImageLink().get();
        return fileStore.download(path, key);
    }

    private Map<String, String> getFileMetadata(MultipartFile file) {
        // 4. extract metadata from the file
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private void validateFile(MultipartFile file) {
        // 1. validate if the file is empty
        if(file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file.");
        }

        // 2. validate if the file is an image
        //if(!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
        if(!file.getContentType().startsWith("image/")) {
            throw new IllegalStateException("File must be an image.");
        }
    }


}
