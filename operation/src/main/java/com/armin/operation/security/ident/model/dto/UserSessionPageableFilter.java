package com.armin.operation.security.ident.model.dto;

import com.armin.utility.repository.common.SortOption;
import com.armin.utility.statics.enums.SortType;

import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

public class UserSessionPageableFilter extends UserSessionFilter {
    @Min(1)
    private Integer page;
    @Min(1)
    private Integer size;
    private List<SortOption> sort;

    public UserSessionPageableFilter() {
        this.page = 1;
        this.size = 50;
        this.sort = new ArrayList<>();
        this.sort.add(new SortOption("id", SortType.DESCENDING));
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<SortOption> getSort() {
        return sort;
    }

    public void setSort(List<SortOption> sort) {
        this.sort = sort;
    }
}
