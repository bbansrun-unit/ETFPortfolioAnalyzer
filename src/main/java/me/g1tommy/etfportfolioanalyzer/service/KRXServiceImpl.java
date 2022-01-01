package me.g1tommy.etfportfolioanalyzer.service;

import me.g1tommy.etfportfolioanalyzer.entity.StockEntity;
import me.g1tommy.etfportfolioanalyzer.entity.ETFEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class KRXServiceImpl implements DataService {

    @Autowired
    private KRXScrapeService scrapeService;

    @Override
    public List<ETFEntity> getList() throws IOException {
        return scrapeService.getList();
    }

    @Override
    public List<StockEntity> getStockDetail(String code) throws IOException {
        return scrapeService.getStockDetail(code);
    }

}
