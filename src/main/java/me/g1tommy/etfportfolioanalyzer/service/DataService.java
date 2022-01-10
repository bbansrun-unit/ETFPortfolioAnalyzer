package me.g1tommy.etfportfolioanalyzer.service;

import me.g1tommy.etfportfolioanalyzer.entity.StockEntity;
import me.g1tommy.etfportfolioanalyzer.entity.ETFListEntity;

import java.io.IOException;
import java.util.List;

public interface DataService {
    List<ETFListEntity> getList() throws IOException;
    List<StockEntity> getStockDetail(String code) throws IOException;
}
