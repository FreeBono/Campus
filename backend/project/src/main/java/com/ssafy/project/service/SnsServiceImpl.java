package com.ssafy.project.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.ssafy.project.dao.SnsDao;
import com.ssafy.project.dto.SnsDto;
import com.ssafy.project.dto.SnsImageDto;
import com.ssafy.project.dto.SnsParamDto;
import com.ssafy.project.dto.SnsResultDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
public class SnsServiceImpl implements SnsService{
    
    @Autowired
    SnsDao dao;

    private static final int SUCCESS = 1;
    private static final int FAIL = -1;

    private String uploadFile(File file, String fileName) throws IOException { // 파일 업로드
        BlobId blobId = BlobId.of("camp-us-9dace.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        String jsonFile = "camp-us-9dace-firebase-adminsdk-68gfe-d8f1c14592.json";
        ClassPathResource cpr = new ClassPathResource(jsonFile);
        Credentials credentials = GoogleCredentials
                .fromStream(cpr.getInputStream());
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/camp-us-9dace.appspot.com/o/%s?alt=media";
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, "UTF-8"));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException { 
                                                                                                  
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private String getExtension(String fileName) { 
        return fileName.substring(fileName.lastIndexOf("."));
    }

    
    @Override
    @Transactional
    public SnsResultDto snsInsert(SnsDto dto, MultipartHttpServletRequest request) {
        
        SnsResultDto snsResultDto = new SnsResultDto();

        try{

            dao.snsInsert(dto); //dto는 키값
            
            List<MultipartFile> fileList = request.getFiles("file");
            
            for(MultipartFile file : fileList){

                int snsNo = dto.getSnsNo();
                
                String fileName = file.getOriginalFilename();
                fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));

                File fileO = this.convertToFile(file, fileName);
                String TEMP_URL = this.uploadFile(fileO, fileName);
                SnsImageDto snsImageDto = new SnsImageDto();
                snsImageDto.setSnsNo(snsNo);
                snsImageDto.setSnsImageUrl(TEMP_URL);
                fileO.delete();
                if(dao.snsImageInsert(snsImageDto) == SUCCESS){
                    snsResultDto.setResult(SUCCESS);
                }else{
                    snsResultDto.setResult(FAIL);
                }
            }
            snsResultDto.setResult(SUCCESS);

        }catch(Exception e){
            e.printStackTrace();
            snsResultDto.setResult(FAIL);
        }

        return snsResultDto;
    }

    // 리스트를 생성하고 sns와 user를 join한 값들을 list에 순차적으로 저장(count는 sns에 있는 튜플의 개수로 지정)
    @Override
    public SnsResultDto snsList(SnsParamDto snsParamDto) {
        
        SnsResultDto snsResultDto = new SnsResultDto();
        System.out.println("1");

        try{
            System.out.println("2");
            List<SnsDto> list = dao.snsList(snsParamDto);
            //List<>
            int count = dao.snsListTotalCount();
            snsResultDto.setList(list);
            snsResultDto.setCount(count);
            snsResultDto.setResult(SUCCESS);
        
        }catch(Exception e){
            e.printStackTrace();
            snsResultDto.setResult(FAIL);
        }

        return snsResultDto;
    }

    @Override
    public SnsResultDto snsListSearchWord(SnsParamDto snsParamDto) {
        
        SnsResultDto snsResultDto = new SnsResultDto();

        try{
            List<SnsDto> list = dao.snsListSearchWord(snsParamDto);
            int count = dao.snsListSearchWordTotalCount();
            snsResultDto.setList(list);
            snsResultDto.setCount(count);
            snsResultDto.setResult(SUCCESS);
        
        }catch(Exception e){
            e.printStackTrace();
            snsResultDto.setResult(FAIL);
        }

        return snsResultDto;
    }

    @Override
    @Transactional
    public SnsResultDto snsUpdate(SnsDto dto, MultipartHttpServletRequest request) {
        
        SnsResultDto snsResultDto = new SnsResultDto();

        try {
            dao.snsUpdate(dto);

            // 만약 로컬에 이미지를 저장하게되면
            // 로컬에 있는 이미지를 삭제, db에 저장된 이미지 url삭제 
            // 이후 insert하는 방식

            snsResultDto.setResult(SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            snsResultDto.setResult(FAIL);
        }


        return snsResultDto;
    }

    @Override
    @Transactional
    public SnsResultDto snsDelete(int snsNo) {
        
        SnsResultDto snsResultDto = new SnsResultDto();

        try {
            //로컬에 저장된 이미지 삭제
            System.out.println("delete serviceImpl");
            //snsImageDelete(snsNo);
            //댓글 삭제;
            dao.snsDelete(snsNo); // 마지막으로 글 삭제

            snsResultDto.setResult(SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            snsResultDto.setResult(FAIL);
        }

        return snsResultDto;
    }

    @Override
    public SnsResultDto snsDetail(SnsParamDto snsParamDto) {
        SnsResultDto snsResultDto = new SnsResultDto();

        try {

            SnsDto snsDto = dao.snsDetail(snsParamDto);

            // 이미지 리스트 불러와주기
            
            snsResultDto.setDto(snsDto);
            snsResultDto.setResult(SUCCESS);
            
        } catch (Exception e) {
            e.printStackTrace();
            snsResultDto.setResult(FAIL);
        }
        
        return snsResultDto;
    }



    
}