package com.learnings.intern_service.integration_tests;

import com.learnings.intern_service.entity.Intern;
import com.learnings.intern_service.repository.InternRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class InternsIntegrationTest {

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    private InternRepository internRepository;

    @Test
    void createAndGetIntern_throughHttp() {
        Intern create = new Intern();
        create.setUserId(1L);
        create.setMentorUserIds(Set.of(500L));

        ResponseEntity<Intern> postResp = restTemplate.postForEntity("/api/interns", create, Intern.class);
        assertThat(postResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        Intern saved = postResp.getBody();
        assertThat(saved).isNotNull();
        Long id = saved.getId();

        ResponseEntity<Intern> getResp = restTemplate.getForEntity("/api/interns/" + id, Intern.class);
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertNotNull(getResp.getBody());
        assertThat(getResp.getBody().getId()).isEqualTo(id);
    }
}

