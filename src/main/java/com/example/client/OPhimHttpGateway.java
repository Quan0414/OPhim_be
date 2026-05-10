package com.example.client;

import com.example.exception.OPhimException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.config.ConfigMapping;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriBuilder;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@ApplicationScoped
public class OPhimHttpGateway implements OPhimGateway {
    private static final Logger LOG = Logger.getLogger(OPhimHttpGateway.class);
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(20);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final OPhimConfig config;

    @Inject
    public OPhimHttpGateway(ObjectMapper objectMapper, OPhimConfig config) {
        this.objectMapper = objectMapper;
        this.config = config;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    @Override
    public JsonNode get(String path, Map<String, String> queryParams) {
        URI uri = buildUri(path, queryParams);
        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(REQUEST_TIMEOUT)
                .header("accept", "application/json")
                .GET()
                .build();

        try {
            LOG.infov("Calling OPhim upstream: path={0}, query={1}", path, queryParams);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            LOG.infov("OPhim upstream responded: path={0}, status={1}", path, response.statusCode());

            if (response.statusCode() >= 500) {
                LOG.warnv("OPhim upstream server error: path={0}, status={1}", path, response.statusCode());
                throw new OPhimException(502, "OPhim upstream is unavailable");
            }

            JsonNode body = objectMapper.readTree(response.body());
            if (response.statusCode() >= 400) {
                String message = body.path("message").asText("OPhim request failed");
                LOG.warnv("OPhim upstream request failed: path={0}, status={1}, message={2}",
                        path,
                        response.statusCode(),
                        message);
                throw new OPhimException(response.statusCode(), message);
            }

            return body;
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            LOG.errorv(exception, "OPhim upstream request interrupted: path={0}", path);
            throw new OPhimException(503, "OPhim request was interrupted");
        } catch (IOException exception) {
            LOG.errorv(exception, "Cannot connect to OPhim upstream: path={0}", path);
            throw new OPhimException(502, "Cannot connect to OPhim upstream");
        }
    }

    private URI buildUri(String path, Map<String, String> queryParams) {
        UriBuilder builder = UriBuilder.fromUri(config.baseUrl()).path(path);
        queryParams.forEach((key, value) -> {
            if (value != null && !value.isBlank()) {
                builder.queryParam(key, value);
            }
        });
        return builder.build();
    }

    @ConfigMapping(prefix = "ophim")
    public interface OPhimConfig {
        URI baseUrl();
    }
}
