package com.example.artapp.service.storage;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import com.example.artapp.service.storage.StorageException;
class FileSystemStorageServiceTest {


    @Test
    void checkType() {
        FileSystemStorageService fileSystemStorageService = new FileSystemStorageService();// Arrange
        String fileName = "image.jpg";
        String response = fileSystemStorageService.checkType(fileName);//Act
        assertEquals("Ok", response);//Assert
    }


    @Test
    void checkSecurity() {
        FileSystemStorageService fileSystemStorageService = new FileSystemStorageService();
        String fileName  = "../";
        Exception exception = assertThrows(StorageException.class, () -> {
            fileSystemStorageService.checkSecurity(fileName);
        });

        String expectedMessage = "Cannot store file with relative path outside current directory";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}