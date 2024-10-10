package org.movieflix.movieapp.Controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.movieflix.movieapp.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("/file/")
public class FileController {

    @Autowired
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Value("${file.upload.path}")
    private String path;

    @PostMapping("/FileUpload")
    public ResponseEntity<String> uploadFile(@RequestPart MultipartFile file) throws IOException {
        String savedFile=fileService.uploadFile(path,file);
        return ResponseEntity.ok("Uploaded Succesfully "+savedFile);
    }
    @GetMapping("/{fileName}")
    public void getFile(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        InputStream resourceFile =fileService.getResourceFile(path, fileName);
        response.setContentType(String.valueOf(MediaType.APPLICATION_PDF));
        StreamUtils.copy(resourceFile, response.getOutputStream());

    }
    }
