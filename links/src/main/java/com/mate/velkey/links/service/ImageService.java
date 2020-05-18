package com.mate.velkey.links.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Service
public class ImageService {

    private final String bucketName;
    private final AmazonS3 amazonS3;

    public ImageService(@Value("${amazonProperties.s3.bucketName}") String bucketName, AmazonS3 amazonS3) {
        this.bucketName = bucketName;
        this.amazonS3 = amazonS3;
    }

    public String createImage(MultipartFile imageMultipartFile) throws IOException {
        File imageFile = convertMultipartFileToFile(imageMultipartFile);
        String imageFileName = generateFileName(imageMultipartFile);
        uploadImageFile(imageFileName, imageFile);
        imageFile.delete();
        return imageFileName;
    }

    public void deleteImage(String imageFileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, imageFileName));
    }

    private File convertMultipartFileToFile(MultipartFile imageMultipartFile) throws IOException {
        File imageFile = new File(Objects.requireNonNull(imageMultipartFile.getOriginalFilename()));
        FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
        fileOutputStream.write(imageMultipartFile.getBytes());
        fileOutputStream.close();
        return imageFile;
    }

    private String generateFileName(MultipartFile imageMultipartFile) {
        return new Date().getTime()
                + "-"
                + Objects.requireNonNull(imageMultipartFile.getOriginalFilename()).replace(" ", "_");
    }

    private void uploadImageFile(String imageFileName, File imageFile) {
        AccessControlList accessControlList = new AccessControlList();
        accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, imageFileName, imageFile)
                .withAccessControlList(accessControlList);
        amazonS3.putObject(putObjectRequest);
    }
}
