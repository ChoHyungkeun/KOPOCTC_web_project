package com.example.KOPOCTC_web_project.repository;

import com.example.KOPOCTC_web_project.entity.SeoulDataEntity;
import com.example.KOPOCTC_web_project.entity.ServiceRecommend;
import com.example.KOPOCTC_web_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRecommendRepository extends JpaRepository<ServiceRecommend, Long> {
    boolean existsByUserAndService(User user, SeoulDataEntity service);
}
