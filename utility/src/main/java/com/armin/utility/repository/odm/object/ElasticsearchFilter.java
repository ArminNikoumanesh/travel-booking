package com.armin.utility.repository.odm.object;

import com.armin.utility.repository.common.ConditionParameters;
import com.armin.utility.repository.common.SortOption;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ElasticsearchFilter {
    private Integer page;
    private Integer size;
    private List<SortOption> sorts;
    private List<ConditionParameters> termConditions;
    private List<ConditionParameters> matchConditions;
    private List<RangeConditionParameters> rangeConditions;
    private List<ConditionParameters> termsConditions;

    public ElasticsearchFilter() {
        this.page = 1;
        this.size = 10;
        this.termConditions = new ArrayList<>();
        this.matchConditions = new ArrayList<>();
        this.rangeConditions = new ArrayList<>();
        this.termsConditions = new ArrayList<>();
        this.sorts = new ArrayList<>();
    }

    public void addRangeCondition(String key, Object from, Object to) {
        this.rangeConditions.add(new RangeConditionParameters(key, from, to));
    }

    public void addTermCondition(String key, Object value) {
        this.termConditions.add(new ConditionParameters(key, value));
    }

    public void addMatchCondition(String key, Object value) {
        this.matchConditions.add(new ConditionParameters(key, value));
    }

    public void addTermsCondition(String key, Object value) {
        this.termsConditions.add(new ConditionParameters(key, value));
    }
}
