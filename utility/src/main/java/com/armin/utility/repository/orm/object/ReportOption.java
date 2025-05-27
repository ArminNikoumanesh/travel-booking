package com.armin.utility.repository.orm.object;


import com.armin.utility.repository.common.SortOption;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class ReportOption {
    private boolean distinct = false;
    private Integer pageSize;
    private Integer pageNumber; // start from 1
    private boolean export;
    private List<SortOption> sortOptions;

    public ReportOption() {
        this.pageSize = 10;
        this.pageNumber = 1;
    }

    public ReportOption(Integer pageSize, Integer pageNumber) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    public ReportOption(Integer pageSize, Integer pageNumber, List<SortOption> sortOptions) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.sortOptions = sortOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportOption)) return false;
        ReportOption that = (ReportOption) o;
        return distinct == that.distinct &&
                Objects.equals(pageSize, that.pageSize) &&
                Objects.equals(pageNumber, that.pageNumber) &&
                Objects.equals(sortOptions, that.sortOptions) &&
                Objects.equals(export, that.export);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct, pageSize, pageNumber, sortOptions,export);
    }
}
