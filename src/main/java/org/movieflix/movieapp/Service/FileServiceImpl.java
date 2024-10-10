package org.movieflix.movieapp.Service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        // Get the name of the file
        String fileName = file.getOriginalFilename();

        // Create the directory if it doesn't exist
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Create the full file path
        String filePath = path + File.separator + fileName;

        // Save the file to the specified path
        file.transferTo(new File(filePath));

        // Return the filename
        return fileName;
    }
    @Override
    public InputStream getResourceFile(String path, String fileName) throws IOException {
        String filePath= path + File.separator + fileName;
        return new FileInputStream(filePath);
    }
}
