package me.g1tommy.etfportfolioanalyzer.service;

import me.g1tommy.etfportfolioanalyzer.entity.APIResponseEntity;
import me.g1tommy.etfportfolioanalyzer.entity.StockEntity;
import me.g1tommy.etfportfolioanalyzer.entity.ETFEntity;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
public class KRXScrapeService {

    @Value("${service.api.max_work_dt.endpoint}")
    private String maxWorkDtEndpoint;

    @Value("${service.api.stock_query.endpoint}")
    private String stockQueryEndpoint;

    @Value("${service.params.list.bld}")
    private String listBld;

    @Value("${service.params.detail.bld}")
    private String detailBld;

    private String getMaxWorkDt() {
        ResponseEntity<String> response = new RestTemplate().exchange(
                maxWorkDtEndpoint,
                HttpMethod.GET,
                null,
                String.class
        );

        return new JSONObject(response.getBody())
                .getJSONObject("result")
                .getJSONArray("output")
                .getJSONObject(0)
                .getString("max_work_dt");
    }

    public List<ETFEntity> getList() throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        // Payload
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("bld", listBld);
        paramMap.add("trdDd", getMaxWorkDt());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(paramMap, headers);

        // Request
        restTemplate.getInterceptors().add((request, body, execution) -> {
            ClientHttpResponse response = execution.execute(request,body);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response;
        });

        ResponseEntity<APIResponseEntity<List<ETFEntity>>> response = restTemplate.exchange(
                stockQueryEndpoint,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody().getOutput();
    }

    public List<StockEntity> getStockDetail(String code) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        // Payload
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("bld", detailBld);
        paramMap.add("trdDd", getMaxWorkDt());
        paramMap.add("isuCd", "KR7" + code + "000");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(paramMap, headers);

        // Request
        restTemplate.getInterceptors().add((request, body, execution) -> {
            ClientHttpResponse response = execution.execute(request,body);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response;
        });

        ResponseEntity<APIResponseEntity<List<StockEntity>>> response = restTemplate.exchange(
                stockQueryEndpoint,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody().getOutput();
    }
}
