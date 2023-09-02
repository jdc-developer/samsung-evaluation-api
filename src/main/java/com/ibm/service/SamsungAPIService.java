package com.ibm.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.ibm.dto.CurrencyDTO;
import com.ibm.dto.DocumentDTO;
import com.ibm.dto.QuotationDTO;
import com.ibm.service.exception.RequestUnsuccessfulException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

@Service
public class SamsungAPIService {

    @Value("${samsung.base.url}")
    private String samsungBaseURL;

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final Gson GSON = new GsonBuilder()
            .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            .create();

    public List<CurrencyDTO> listCurrencies() {
        final String path = "/sds-devs-evaluation/evaluation/currency";

        HttpRequest request = HttpRequest.newBuilder()
                .GET().uri(URI.create(samsungBaseURL + path))
                .setHeader("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = null;
        try {
            response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RequestUnsuccessfulException();
        }

        if (response.statusCode() != HttpStatus.SC_OK && response.statusCode() != HttpStatus.SC_CREATED)
            throw new RequestUnsuccessfulException();

        List<CurrencyDTO> currencies = GSON.fromJson(response.body(), List.class);
        return currencies;
    }

    public List<QuotationDTO> listQuotations() {
        final String path = "/sds-devs-evaluation/evaluation/quotation";

        HttpRequest request = HttpRequest.newBuilder()
                .GET().uri(URI.create(samsungBaseURL + path))
                .setHeader("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = null;
        try {
            response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RequestUnsuccessfulException();
        }

        if (response.statusCode() != HttpStatus.SC_OK && response.statusCode() != HttpStatus.SC_CREATED)
            throw new RequestUnsuccessfulException();

        List<QuotationDTO> quotations = GSON.fromJson(response.body(), List.class);
        return quotations;
    }

    public List<DocumentDTO> listDocuments() {
        final String path = "/sds-devs-evaluation/evaluation/docs";

        HttpRequest request = HttpRequest.newBuilder()
                .GET().uri(URI.create(samsungBaseURL + path))
                .setHeader("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = null;
        try {
            response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RequestUnsuccessfulException();
        }

        if (response.statusCode() != HttpStatus.SC_OK && response.statusCode() != HttpStatus.SC_CREATED)
            throw new RequestUnsuccessfulException();

        List<DocumentDTO> documents = GSON.fromJson(response.body(), List.class);
        return documents;
    }
}
