package com.armin.messaging.template.repository.dto;

import com.armin.utility.repository.common.SortOption;
import com.armin.utility.statics.enums.SortType;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class HandlebarsTemplatePageableFilter extends HandlebarsTemplateFilter {
    @Min(1)
    private int page;
    @Min(1)
    private int size;
    private List<SortOption> sort;

    public HandlebarsTemplatePageableFilter() {
        this.page=1;
        this.size=50;
        this.sort = new ArrayList<>();
        this.sort.add(new SortOption("id", SortType.DESCENDING));
    }
}
