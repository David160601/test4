package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@AllArgsConstructor
public class ImageValidator implements ConstraintValidator<ImageValidation, MultipartFile> {
    public boolean isValid(MultipartFile img, ConstraintValidatorContext cxt) {
        if (img == null || img.isEmpty()) {
            return false;
        }
        if (!isImageExtension(img.getOriginalFilename())) {
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
