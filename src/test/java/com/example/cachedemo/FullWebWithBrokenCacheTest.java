package com.example.cachedemo;

import com.example.cachedemo.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("bad")  // caching broken
public class FullWebWithBrokenCacheTest {
    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void codeWorksEvenIfCachingBroken() {
        String bookId = "5";
        String fullUrl = String.format("http://localhost:%s/book/%s", port, bookId);
        ResponseEntity<Book> response = restTemplate.getForEntity(fullUrl, Book.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        Book book = response.getBody();
        assertThat(book.getId(), is("5"));
    }
}
