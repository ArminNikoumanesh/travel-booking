package com.armin.operation.admin.model.dto.user;

import com.armin.utility.repository.common.SortOption;
import com.armin.utility.statics.enums.SortType;

import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserPageableFilter extends UserFilter {
    @Min(1)
    private Integer page;
    @Min(1)
    private Integer size;
    private List<SortOption> sort;

    public UserPageableFilter() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserPageableFilter that = (UserPageableFilter) o;
        return Objects.equals(page, that.page) &&
                Objects.equals(size, that.size) &&
                Objects.equals(sort, that.sort);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), page, size, sort);
    }
}
