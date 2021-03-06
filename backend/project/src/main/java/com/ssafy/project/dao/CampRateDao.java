package com.ssafy.project.dao;

import java.util.List;

import com.ssafy.project.dto.CampRateDto;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CampRateDao {
    // 캠핑장 평점 리스트
    public List<CampRateDto> campRateList(String contentId);

    // 캠핑장 평점 리스트 개수
    public int campRateListTotalCount(String contentId);

    // 캠핑장 평점 상세 정보
    public CampRateDto campRateDetail(@Param("campRateNo") int campRateNo);

    // 캠핑장 평점 추가
    public int campRateInsert(CampRateDto campRateDto);

    // 캠핑장 평점 수정
    public int campRateUpdate(CampRateDto campRateDto);

    // 캠핑장 평점 삭제
    public int campRateDelete(int campRateNo);

    // 최신 campRateNo 가져오기
    public int getCampRateNo();

}
