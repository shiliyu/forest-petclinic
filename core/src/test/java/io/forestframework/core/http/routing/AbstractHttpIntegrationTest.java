package io.forestframework.core.http.routing;

import io.forestframework.core.config.Config;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;

import javax.inject.Inject;
import java.io.IOException;

public class AbstractHttpIntegrationTest {

    protected CloseableHttpClient client;

    @Inject
    @Config("forest.http.port")
    protected Integer port;

    public HttpResponse sendHttpRequest(String method, String path) throws IOException {
        String uri = "http://localhost:" + port + path;
        HttpUriRequest request = RequestBuilder
                .create(method)
                .setUri(uri)
                .setHeader("Accept", String.valueOf(ContentType.APPLICATION_JSON))
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
//                .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
//                .setHeader(HttpHeaders.CONTENT_TYPE, "text/html")
                .build();
        CloseableHttpResponse response = client.execute(request);
        return new HttpResponse(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity()));
    }

    static class HttpResponse {
        private final int statusCode;
        private final String body;

        public HttpResponse(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }

        HttpResponse assert200() {
            return assertStatusCode(200);
        }

        HttpResponse assert404() {
            return assertStatusCode(404);
        }

        HttpResponse assert405() {
            return assertStatusCode(405);
        }

        HttpResponse assert406() {
            return assertStatusCode(406);
        }

        HttpResponse assert500() {
            return assertStatusCode(500);
        }

        HttpResponse assert503() {
            return assertStatusCode(503);
        }

        HttpResponse assertStatusCode(int expectedStatusCode) {
            Assertions.assertEquals(expectedStatusCode, statusCode);
            return this;
        }

        public String getBody() {
            return body;
        }
    }
}
