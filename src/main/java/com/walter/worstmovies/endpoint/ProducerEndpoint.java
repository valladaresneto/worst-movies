package com.walter.worstmovies.endpoint;

import com.walter.worstmovies.service.ProducerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("producers")
public class ProducerEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerEndpoint.class);

    @Autowired
    private ProducerService producerService;

    @GetMapping("/bigger-period-between-awards")
    public ResponseEntity biggerPeriodBetweenAwards() {
        try {
            return ResponseEntity.ok(producerService.biggerPeriodBetweenAwards());
        } catch (Exception e) {
            LOGGER.error("Error", e);
            return ResponseEntity.status(500).body("Error on find producer with bigger period between awards.");
        }
    }

    @GetMapping("/shorter-period-between-awards")
    public ResponseEntity shorterPeriodBetweenAwards() {
        try {
            return ResponseEntity.ok(producerService.shorterPeriodBetweenAwards());
        } catch (Exception e) {
            LOGGER.error("Error", e);
            return ResponseEntity.status(500).body("Error on find producer with shorter period between awards.");
        }
    }

}
