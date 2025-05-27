package com.armin.utility.repository.orm.object;

import java.util.List;

public class ReportResult<Result, Aggregation> {
    private Integer countAll;
    private List<Result> result;
    private Aggregation aggregation;

    public ReportResult(Integer countAll, List<Result> result) {
        this.countAll = countAll;
        this.result = result;
        this.aggregation = null;
    }

    public ReportResult(Integer countAll, List<Result> result, Aggregation aggregation) {
        this.countAll = countAll;
        this.result = result;
        this.aggregation = aggregation;
    }

    public Integer getCountAll() {
        return countAll;
    }

    public void setCountAll(Integer countAll) {
        this.countAll = countAll;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public Aggregation getAggregation() {
        return aggregation;
    }

    public void setAggregation(Aggregation aggregation) {
        this.aggregation = aggregation;
    }
}
