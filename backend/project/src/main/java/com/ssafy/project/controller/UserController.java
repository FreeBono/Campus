package com.ssafy.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.project.dto.UserDto;
import com.ssafy.project.dto.UserRateDto;
import com.ssafy.project.dto.UserResultDto;
import com.ssafy.project.service.UserService;

@CrossOrigin(origins = "https://i6e102.p.ssafy.io", allowCredentials = "true", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.OPTIONS })
@RestController // == > @Controller + @ResponseBody ==> 여기는 다 json 으로 넘어간다!! // 값자체를 리턴
public class UserController {

    @Autowired
    UserService userService;

    private static final int SUCCESS = 1;

    // 회원가입
    @PostMapping(value = "/user")
    public ResponseEntity<UserResultDto> register(@RequestBody UserDto userDto) {
        System.out.println(userDto);
        UserResultDto userResultDto = userService.userRegister(userDto);
        if (userResultDto.getResult() == SUCCESS) {
            return new ResponseEntity<UserResultDto>(userResultDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<UserResultDto>(userResultDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // userEmail에 해당하는 user 정보 구하기
    @GetMapping(value = "/user/{userEmail}")
    public ResponseEntity<UserDto> select(@PathVariable String userEmail) {
        System.out.println(1);
        System.out.println(userEmail);
        UserDto userDto = userService.userSelect(userEmail);
        System.out.println(userDto);
        return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
    }

    // user 정보 업데이트
    @PutMapping(value = "/user")
    public ResponseEntity<UserResultDto> update(@RequestBody UserDto userDto) {
        System.out.println(userDto);
        UserResultDto userResultDto = userService.userUpdate(userDto);
        System.out.println(userResultDto);
        if (userResultDto.getResult() == SUCCESS) {
            return new ResponseEntity<UserResultDto>(userResultDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<UserResultDto>(userResultDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 가입 시 user 세부 정보 입력
    @PutMapping(value = "/user/camp")
    public ResponseEntity<UserResultDto> updateCamp(@RequestBody UserDto userDto) {
        UserResultDto userResultDto = userService.userUpdateCamp(userDto);
        if (userResultDto.getResult() == SUCCESS) {
            return new ResponseEntity<UserResultDto>(userResultDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<UserResultDto>(userResultDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 가입 시 mbti
    @PutMapping(value = "/user/mbti")
    public ResponseEntity<UserResultDto> updateMBTI(@RequestBody UserDto userDto) {
        UserResultDto userResultDto = userService.userUpdateMBTI(userDto);
        if (userResultDto.getResult() == SUCCESS) {
            return new ResponseEntity<UserResultDto>(userResultDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<UserResultDto>(userResultDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 이미지 업로드 테스트
    // @PutMapping(value = "/user/image")
    // public String updateImage(@RequestParam("userProfileImage") MultipartFile
    // multipartFile) {
    // System.out.println(multipartFile.getOriginalFilename());
    // String result = userService.userUpdateProfileImage(multipartFile);
    // return result;
    // }

    // 가입 시 이미지 등록
    @PutMapping(value = "/user/image/{userEmail}")
    public ResponseEntity<UserResultDto> updateImage(@PathVariable String userEmail,
            @RequestParam("fileName") MultipartFile multipartFile) {
        System.out.println(multipartFile.getOriginalFilename());
        UserResultDto userResultDto = userService.userUpdateProfileImage(userEmail,
                multipartFile);
        if (userResultDto.getResult() == SUCCESS) {
            return new ResponseEntity<UserResultDto>(userResultDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<UserResultDto>(userResultDto,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // userEmail에 해당하는 user 탈퇴
    @DeleteMapping(value = "/user/{userEmail}")
    public ResponseEntity<UserResultDto> delete(@PathVariable String userEmail) {
        UserResultDto userResultDto = userService.userDelete(userEmail);
        if (userResultDto.getResult() == SUCCESS) {
            return new ResponseEntity<UserResultDto>(userResultDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<UserResultDto>(userResultDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 유저 평점
    @PostMapping(value = "/user/rate")
    public ResponseEntity<UserResultDto> rate(@RequestBody UserRateDto userRateDto) {
        UserResultDto userResultDto = userService.userRate(userRateDto);
        if (userResultDto.getResult() == SUCCESS) {
            return new ResponseEntity<UserResultDto>(userResultDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<UserResultDto>(userResultDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // userEmail에 해당하는 user 정보 구하기
    @GetMapping(value = "/user/duplemail/{userEmail}")
    public ResponseEntity<UserResultDto> duplEmail(@PathVariable String userEmail) {
        UserResultDto userResultDto = userService.userDuplEmail(userEmail);
        return new ResponseEntity<UserResultDto>(userResultDto, HttpStatus.OK);
    }

    // userEmail에 해당하는 user 정보 구하기
    @GetMapping(value = "/user/duplnickname/{userNickname}")
    public ResponseEntity<UserResultDto> duplNickname(@PathVariable String userNickname) {
        UserResultDto userResultDto = userService.userDuplNickname(userNickname);
        return new ResponseEntity<UserResultDto>(userResultDto, HttpStatus.OK);
    }
}
