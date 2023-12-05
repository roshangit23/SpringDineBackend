package com.hotelPro.hotelmanagementsystem.service;

import com.hotelPro.hotelmanagementsystem.controller.DTO.FeedbackRequestDTO;
import com.hotelPro.hotelmanagementsystem.controller.DTO.FeedbackResponseDTO;
import com.hotelPro.hotelmanagementsystem.model.Feedback;

import java.util.List;

public interface FeedbackService {
    FeedbackResponseDTO createFeedback(FeedbackRequestDTO feedbackRequestDTO, Long companyId);
    List<Feedback> getFeedbackByCompanyId(Long companyId);
}
