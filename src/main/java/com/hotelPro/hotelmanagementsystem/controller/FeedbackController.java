package com.hotelPro.hotelmanagementsystem.controller;

import com.hotelPro.hotelmanagementsystem.controller.DTO.FeedbackRequestDTO;
import com.hotelPro.hotelmanagementsystem.controller.DTO.FeedbackResponseDTO;
import com.hotelPro.hotelmanagementsystem.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/createFeedback/{companyId}")
    public ResponseEntity<ApiResponse<FeedbackResponseDTO>> createFeedback(@PathVariable Long companyId, @Valid @RequestBody FeedbackRequestDTO feedbackRequestDTO) {
        FeedbackResponseDTO createdFeedback = feedbackService.createFeedback(feedbackRequestDTO, companyId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED.value(), createdFeedback));
    }

    @GetMapping("/getFeedbackByCompanyId/{companyId}")
    public ResponseEntity<ApiResponse<List<FeedbackResponseDTO>>> getFeedbackByCompanyId(@PathVariable Long companyId) {
        List<FeedbackResponseDTO> feedbacks = feedbackService.getFeedbackByCompanyId(companyId)
                .stream()
                .map(FeedbackResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), feedbacks));
    }
}
