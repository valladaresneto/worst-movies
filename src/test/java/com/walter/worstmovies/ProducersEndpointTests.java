package com.walter.worstmovies;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ProducersEndpointTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void shouldReturn200AndTheProducerWithBiggerPeriodBetweenAwards() {
        String resorce = this.testRestTemplate.getRootUri() + "/producers/bigger-period-between-awards";
        ResponseEntity entity = this.testRestTemplate.getForEntity(resorce, List.class);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List producers = (ArrayList) entity.getBody();
        then(producers.size()).isEqualTo(1);
        then(((Map) producers.get(0)).get("name")).isEqualTo("Matthew Vaughn");
    }

    @Test
    void shouldReturn200AndTheProducerWithShorterPeriodBetweenAwards() {
        String resorce = this.testRestTemplate.getRootUri() + "/producers/shorter-period-between-awards";
        ResponseEntity entity = this.testRestTemplate.getForEntity(resorce, List.class);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List producers = (ArrayList) entity.getBody();
        then(producers.size()).isEqualTo(1);
        then(((Map) producers.get(0)).get("name")).isEqualTo("Joel Silver");
    }
}
