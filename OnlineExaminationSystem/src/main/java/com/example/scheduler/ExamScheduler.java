package com.example.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.service.ExamAttemptService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExamScheduler {
	
	  private static final Logger log = LoggerFactory.getLogger(ExamScheduler.class);

	    private final ExamAttemptService attemptService;

	    // runs every 1 minute and submits attempts whose exam time is over
	    @Scheduled(fixedRate = 60000)
	    public void autoSubmitExpiredAttempts() {
	    	
	        int count = attemptService.autoSubmitExpiredAttempts();
	        
	        if (count > 0) {
	            log.info("Scheduler auto-submitted {} attempts", count);
	        }
	    }

}
