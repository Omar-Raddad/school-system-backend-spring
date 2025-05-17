package com.example.demo.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
public class GoogleDriveUploader {

    private static final String APPLICATION_NAME = "SandBag App";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static Drive driveService;

    private static Drive getDriveService() throws IOException, GeneralSecurityException {
        if (driveService != null) {
            return driveService;
        }

        ClassPathResource serviceAccountResource = new ClassPathResource("onyx-nexus-439900-t0-13dc2945f0ed.json");

        GoogleCredential credential = GoogleCredential.fromStream(serviceAccountResource.getInputStream())
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/drive"));

        driveService = new Drive.Builder(new NetHttpTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        return driveService;
    }

    public static String uploadFile(MultipartFile multipartFile) throws IOException, GeneralSecurityException {
        java.io.File file = convertMultipartFileToFile(multipartFile);

        String uploadedFileUrl = uploadFile(file, multipartFile.getContentType());

        file.delete(); // delete temp file

        return uploadedFileUrl;
    }

    private static java.io.File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        java.io.File convFile = new java.io.File(multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }

    private static String uploadFile(java.io.File file, String mimeType) throws IOException, GeneralSecurityException {
        File fileMetadata = new File();
        fileMetadata.setName(file.getName());
        fileMetadata.setParents(Collections.singletonList("1THkxc0ryrSJgNQazKF2SVb14ViXrxLx1"));

        FileContent mediaContent = new FileContent(mimeType, file);

        File uploadedFile = getDriveService().files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();

        return "https://drive.google.com/file/d/" + uploadedFile.getId() + "/view";
    }
}
