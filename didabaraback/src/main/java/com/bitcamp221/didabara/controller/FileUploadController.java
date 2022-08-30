package com.bitcamp221.didabara.controller;

import com.bitcamp221.didabara.dto.S3Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class FileUploadController {

    private final S3Upload s3Upload;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("images") MultipartFile multipartFile, @AuthenticationPrincipal String id) throws IOException {


        return ResponseEntity.ok().body(s3Upload.upload(multipartFile,"images",id));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(String filename,@AuthenticationPrincipal String id){
       int rowCnt=s3Upload.deleteFile(filename,id);
       if(rowCnt==1){
           return ResponseEntity.ok().body("삭제 완료");
       }else{
           return ResponseEntity.badRequest().body("삭제 실패");
       }
    }
}