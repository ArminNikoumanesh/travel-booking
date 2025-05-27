package com.armin.messaging.inappmessage.ident.model;

import com.armin.utility.repository.common.SortOption;
import com.armin.utility.statics.enums.SortType;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 20.04.24
 */
@Getter
@Setter
public class InAppMessagePageableFilter extends InAppMessageFilter {
    @Min(0)
    private Integer size;
    @Min(0)
    private Integer page;
    private List<SortOption> sort;

    public InAppMessagePageableFilter() {
        this.size = 50;
        this.page = 1;
        this.sort = new ArrayList<>();
        this.sort.add(new SortOption("id", SortType.DESCENDING));
    }
}
