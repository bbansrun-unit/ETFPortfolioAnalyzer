package me.g1tommy.etfportfolioanalyzer.service;

import static me.g1tommy.etfportfolioanalyzer.helper.HttpHelper.commonHeader;
import static me.g1tommy.etfportfolioanalyzer.helper.HttpHelper.restTemplate;

import me.g1tommy.etfportfolioanalyzer.entity.APIResponseEntity;
import me.g1tommy.etfportfolioanalyzer.entity.ETFSearchEntity;
import me.g1tommy.etfportfolioanalyzer.entity.StockEntity;
import me.g1tommy.etfportfolioanalyzer.entity.ETFListEntity;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class KRXScrapeService {

    @Value("${service.api.max_work_dt.endpoint}")
    private String maxWorkDtEndpoint;

    @Value("${service.api.stock_query.endpoint}")
    private String stockQueryEndpoint;

    @Value("${service.api.information.endpoint}")
    private String informationEndpoint;

    @Value("${service.params.list.bld}")
    private String listBld;

    @Value("${service.params.detail.bld}")
    private String detailBld;

    @Value("${service.params.information.rows}")
    private String rows;

    @Value("${service.params.information.start}")
    private String start;

    @Value("${service.params.information.solrIsuType}")
    private String solrIsuType;

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

    private String getInformation(String etfCode) {
        // Payload
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("solrIsuType", solrIsuType);
        paramMap.add("solrKeyword", etfCode);
        paramMap.add("rows", rows);
        paramMap.add("start", start);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(paramMap, commonHeader());

        ResponseEntity<ETFSearchEntity> response = restTemplate().exchange(
                informationEndpoint,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody()
                .getResult().get(0)
                .getIsu_cd().get(0);
    }

    public List<ETFListEntity> getList() {
        // Payload
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("bld", listBld);
        paramMap.add("trdDd", getMaxWorkDt());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(paramMap, commonHeader());

        ResponseEntity<APIResponseEntity<List<ETFListEntity>>> response = restTemplate().exchange(
                stockQueryEndpoint,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody().getOutput();
    }

    public List<StockEntity> getStockDetail(String code) {
        // Payload
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("bld", detailBld);
        paramMap.add("trdDd", getMaxWorkDt());
        paramMap.add("isuCd", getInformation(code));

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(paramMap, commonHeader());

        ResponseEntity<APIResponseEntity<List<StockEntity>>> response = restTemplate().exchange(
                stockQueryEndpoint,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody().getOutput();
    }

}
