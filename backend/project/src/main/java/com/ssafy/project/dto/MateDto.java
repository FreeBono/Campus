package com.ssafy.project.dto;

import java.sql.Date;
import java.util.List;

import lombok.Data;

@Data
public class MateDto {
    private int mateNo;
    private String mateTitle;
    private String mateCampsite;
    private int lowestAge;
    private int highestAge;
    private String mateContent;
    private String mateCampstyle;
    private String mateCamptype;
    private Date mateCampstart;
    private Date mateCampend;
    private int userNo;
    private String userNickName;
    private int contentId;
    private String mateStatus;
    private int mateAccompanyNum;


    private Date mateCreateTime;
    private Date mateUpdateTime;

    private String mateImageUrl;
    private String userProfileImage;
    private List<MateListDto> mateList;
    private List<SnsImageDto> imageList;
    private List<MateCampStyleDto> campStyleList;
    private List<MateCampEquipRequiredDto> campEquipRequiredList;

    private String firstImageUrl;
    private String facltNm; // 야영장 명

    // 캠핑장 평점 추가
    // 장비 리스트
    
}
