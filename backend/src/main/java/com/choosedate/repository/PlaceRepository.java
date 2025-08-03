package com.choosedate.repository;

import com.choosedate.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    // 카테고리, 지역, 키워드로 검색
    List<Place> findByCategoryAndRegionContainingAndKeywordContaining(String category, String region, String keyword);

    List<Place> findByRegion(String region);

    List<Place> findByRegionAndCategory(String region, String category);

    // Containing: 부분 일치 (ex. "파스타" → "이탈리안 파스타 하우스" 포함)
    List<Place> findByRegionAndKeywordContaining(String region, String keyword);
}
