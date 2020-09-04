package com.ula.faculty.feign;

import com.ula.faculty.dto.request.StoreTakingExamRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.ula.core.api.response.Response;

@FeignClient(value = "ula-exam-service")
public interface ExamFeignClient
{

    @PostMapping("/taking-exam")
    Response<Object> storeTakingExam(@RequestBody StoreTakingExamRequest request, @RequestHeader("Authorization") String token);
}
