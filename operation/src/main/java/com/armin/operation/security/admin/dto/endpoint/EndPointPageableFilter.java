package com.armin.operation.security.admin.dto.endpoint;

import com.armin.utility.repository.common.SortOption;
import com.armin.utility.statics.enums.SortType;

import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;


/**
 * @author imax on 5/16/19
 */
public class EndPointPageableFilter extends EndPointFilter {

    @Min(1)
    private int page;
    @Min(1)
    private int size;
    private List<SortOption> sort;



    public EndPointPageableFilter() {
        this.page=1;
        this.size=50;
        this.sort = new ArrayList<>();
        this.sort.add(new SortOption("id", SortType.DESCENDING));
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<SortOption> getSort() {
        return sort;
    }

    public void setSort(List<SortOption> sort) {
        this.sort = sort;
    }
}
