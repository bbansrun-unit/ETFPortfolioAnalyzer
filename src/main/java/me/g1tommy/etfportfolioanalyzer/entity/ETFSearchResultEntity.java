package me.g1tommy.etfportfolioanalyzer.entity;

import lombok.Getter;

import java.util.List;

public class ETFSearchResultEntity {
    public List<String> isu_tp_dtl;
    public List<String> mkt_nm;
    public List<String> isu_abbrv;
    public List<String> mkt_id;
    public List<String> prod_id;
    public List<String> isu_tp;
    public List<String> isu_srt_cd;
    public int isu_tp_ord;
    @Getter
    public List<String> isu_cd;
    public String id;
    public String _version_;
}
