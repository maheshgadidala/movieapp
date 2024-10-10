package org.movieflix.movieapp.Service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public interface FileService {
    //upload file
    String uploadFile(String path, MultipartFile file) throws IOException;
    InputStream getResourceFile(String path,String fileName) throws IOException;





}
