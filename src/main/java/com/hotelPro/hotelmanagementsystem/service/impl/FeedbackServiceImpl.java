package com.hotelPro.hotelmanagementsystem.service.impl;

import com.hotelPro.hotelmanagementsystem.controller.DTO.FeedbackRequestDTO;
import com.hotelPro.hotelmanagementsystem.controller.DTO.FeedbackResponseDTO;
import com.hotelPro.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotelPro.hotelmanagementsystem.model.Company;
import com.hotelPro.hotelmanagementsystem.model.Feedback;
import com.hotelPro.hotelmanagementsystem.repository.CompanyRepository;
import com.hotelPro.hotelmanagementsystem.repository.FeedbackRepository;
import com.hotelPro.hotelmanagementsystem.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public FeedbackResponseDTO createFeedback(FeedbackRequestDTO feedbackRequestDTO, Long companyId) {
        // Fetch the company using the provided companyId
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));

        // Create a new Feedback entity
        Feedback feedback = new Feedback();
        feedback.setContent(feedbackRequestDTO.getContent());
        feedback.setCompany(company);

        // Save the Feedback entity to the database
        Feedback savedFeedback = feedbackRepository.save(feedback);

        // Create and return a new FeedbackResponseDTO instance
        return new FeedbackResponseDTO(savedFeedback);
    }


    @Override
    public List<Feedback> getFeedbackByCompanyId(Long companyId) {
        return feedbackRepository.findByCompanyId(companyId);
    }
}

