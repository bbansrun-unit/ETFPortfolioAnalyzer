package me.g1tommy.etfportfolioanalyzer.entity;

import lombok.Getter;

public class APIResponseEntity<T> {
    @Getter
    public T output;
    public String CURRENT_DATETIME;
}
