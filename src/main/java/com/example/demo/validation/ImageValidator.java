package com.example.demo.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@AllArgsConstructor
public class ImageValidator {
    public static boolean validateImage(MultipartFile img) {
        if (img == null || img.isEmpty()) {
            return false;
        }
        if(!isImageExtension(img.getName())){
            return false;
        }
        return true;
    }
    private static boolean isImageExtension(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")
                || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("gif");
    }
}
