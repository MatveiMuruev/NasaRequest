import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        String apiKey = "5wQLGyacpxDE5T2Ar1dr5Prq1br91VgfN1fN18GI";
        String uri = "https://api.nasa.gov/planetary/apod?api_key=";

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(uri + apiKey);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        CloseableHttpResponse response = httpClient.execute(request);

        String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);

        ObjectMapper mapper = new ObjectMapper();
        NasaResponse nasaResponse = mapper.readValue(body, NasaResponse.class);

        request = new HttpGet(nasaResponse.getUrl());
        response = httpClient.execute(request);

        body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);

        String fileName = nasaResponse.getUrl().substring(nasaResponse.getUrl().lastIndexOf('/')+1, nasaResponse.getUrl().lastIndexOf('.')) + ".txt";

        try (FileWriter writer = new FileWriter(fileName)) {

            writer.write(body);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
