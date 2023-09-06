package com.samsung.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.samsung.dto.CurrencyDTO;
import com.samsung.dto.DocumentDTO;
import com.samsung.dto.QuotationDTO;
import com.samsung.service.exception.RequestUnsuccessfulException;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class SamsungAPIService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.getDefault());

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

    public List<DocumentDTO> listDocuments(String documentNumber, String currencyCode, String documentDateFrom,
                                           String documentDateTo) {
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

        return _filterDocuments(response, documentNumber, currencyCode, documentDateFrom, documentDateTo);
    }

    private List<DocumentDTO> _filterDocuments(HttpResponse<String> response, String documentNumber, String currencyCode,
                                               String documentDateFrom, String documentDateTo) throws JSONException {
        AtomicReference<List<DocumentDTO>> documents = new AtomicReference<>();
        documents.set(new ArrayList<>());
        JSONArray jsonArray = new JSONArray(response.body());
        jsonArray.forEach(arrayItem -> {
            JSONObject jsonObject = new JSONObject(arrayItem.toString());
            DocumentDTO documentDTO = new DocumentDTO(jsonObject);
            documents.get().add(documentDTO);
        });

        if (!documentNumber.isBlank()) documents.set(documents.get().stream().filter(documentDTO -> documentDTO.getDocumentNumber().contains(documentNumber))
                .collect(Collectors.toList()));;

        if (!currencyCode.isBlank()) documents.set(documents.get().stream().filter(documentDTO -> documentDTO.getCurrencyCode().equals(currencyCode))
                .collect(Collectors.toList()));

        List<DocumentDTO> documentsToReturn = new ArrayList<>();
        if (!documentDateFrom.isBlank() && !documentDateTo.isBlank()) {
            LocalDate dateFrom = LocalDate.parse(documentDateFrom, FORMATTER);
            LocalDate dateTo = LocalDate.parse(documentDateTo, FORMATTER);
            documents.get().forEach(documentDTO -> {
                LocalDate date = LocalDate.parse(documentDTO.getDocumentDate(), FORMATTER);
                if ((date.isEqual(dateFrom) || date.isAfter(dateFrom)) &&
                        (date.isEqual(dateTo) || date.isBefore(dateTo))) documentsToReturn.add(documentDTO);
            });
        } else documentsToReturn.addAll(documents.get());
        return documentsToReturn;
    }
}
