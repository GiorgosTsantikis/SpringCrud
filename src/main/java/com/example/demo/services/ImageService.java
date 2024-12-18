package com.example.demo.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    public String saveImageToStorage( MultipartFile imageFile);

    public byte[] getImage(String imageName);

    public String deleteImage(String imageDirectory,String imageName);
}
