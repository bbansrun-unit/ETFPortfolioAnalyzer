package me.g1tommy.etfportfolioanalyzer.entity;

import lombok.Getter;

import java.util.List;

public class ETFSearchEntity {
    public String controller;
    public int count;
    public String dir;
    @Getter
    public List<ETFSearchResultEntity> result;
}
