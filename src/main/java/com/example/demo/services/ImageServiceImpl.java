package com.example.demo.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private final String uploadDirectory="src/main/resources/static/images/profilePics";


    @Override
    public String saveImageToStorage( MultipartFile imageFile) {
        String uniqueFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        Path uploadPath=Path.of(uploadDirectory);
        Path filePath=uploadPath.resolve(uniqueFileName);

        if(Files.exists(uploadPath)){
            try {
                Files.createDirectories(uploadPath);
                Files.copy(imageFile.getInputStream(),filePath, StandardCopyOption.REPLACE_EXISTING);
                return uniqueFileName;

            } catch (IOException e) {
                System.out.println("Error creating image directory");
            }
        }
        return null;
    }

    @Override
    public byte[] getImage( String imageName) {
        Path imagePath=Path.of(
                uploadDirectory,imageName);
        if(Files.exists(imagePath)){
            try {
                byte[] imageBytes=Files.readAllBytes(imagePath);
                return imageBytes;
            } catch (IOException e) {
                System.out.println("Image doesn't exist");
            }
        }
        return null;
    }

    @Override
    public String deleteImage(String imageDirectory, String imageName) {
        Path imagePath=Path.of(imageDirectory,imageName);
        if(Files.exists(imagePath)){
            try {
                Files.delete(imagePath);
                return "success";
            } catch (IOException e) {
                System.out.println("Error deleting image");
            }
        }
        return null;
    }
}
