package com.armin.utility.repository.orm.object;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Setter
@Getter
public class ReportFilter implements Serializable {
    private ReportCondition conditions;
    private ReportOption options;
    private List<HaveCondition> haveCondition;

    public ReportFilter(ReportCondition conditions, ReportOption options, List<HaveCondition> haveCondition) {
        this.conditions = conditions;
        this.options = options;
        this.haveCondition = haveCondition;
    }

    public ReportFilter(ReportCondition conditions, ReportOption options) {
        this.conditions = conditions;
        this.options = options;
    }

    public ReportFilter() {
    }

    public void addHaveCondition(HaveCondition haveCondition) {
        this.haveCondition.add(haveCondition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportFilter)) return false;
        ReportFilter that = (ReportFilter) o;
        return Objects.equals(conditions, that.conditions) &&
                Objects.equals(options, that.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditions, options);
    }
}
