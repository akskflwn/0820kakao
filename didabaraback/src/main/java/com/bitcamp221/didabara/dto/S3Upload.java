package com.bitcamp221.didabara.dto;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bitcamp221.didabara.model.UserInfoEntity;
import com.bitcamp221.didabara.presistence.UserInfoRepository;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@PropertySource(value="application.properties")
public class S3Upload {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Autowired
    UserInfoRepository userInfoRepository;
    private final AmazonS3Client s3Client;




    public String upload(MultipartFile file, String dirName, String id) throws IOException {
        File uploadFile = convert(file).orElseThrow(() -> new IllegalArgumentException("file 전달에 실패했습니다."));


        File dumyFile = convert(file).orElseThrow(() -> new IllegalArgumentException("더미 파일 실패."));

        log.info("//////////////////////");
        String extension = uploadFile.getName().substring(uploadFile.getName().lastIndexOf("."));

        File news= new File(System.getProperty("user.dir") + "/" + UUID.randomUUID() + extension);

        uploadFile.renameTo(news);
        return upload(news, dirName,id);
    }




    public String upload(File uploadFile, String dirName,String id) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageURI = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        String dBPathName = uploadImageURI.substring(0, 63);
        String dbFilename = uploadImageURI.substring(uploadImageURI.lastIndexOf("/") + 1);

        log.info(dbFilename);
        log.info(dBPathName);
        Long userId = Long.valueOf(id);
        UserInfoEntity finduserInfo=userInfoRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("유저 인포 저장실패"));
        finduserInfo.setProfileImageUrl(dBPathName);
        finduserInfo.setFilename(dbFilename);

        userInfoRepository.save(finduserInfo);

        removeNewFile(uploadFile);

        return uploadImageURI;
    }

    private String putS3(File uploadFile, String fileName) {
        s3Client.putObject(new PutObjectRequest(bucket, fileName,uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getUrl(bucket, fileName).toString();
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)){
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    public int deleteFile(String fileName,String id) {
        Long userId= Long.valueOf(id);

        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, "images/"+fileName);

        s3Client.deleteObject(deleteObjectRequest);

        UserInfoEntity findUser = userInfoRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("프로필 이미지 경로가 없습니다"));
        //수정 필요//
        userInfoRepository.delete(findUser);
        //수정 필요//
        return 1;
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일 삭제 완료");
        }
        else {
            log.info("파일 삭제 실패");
        }
    }

}