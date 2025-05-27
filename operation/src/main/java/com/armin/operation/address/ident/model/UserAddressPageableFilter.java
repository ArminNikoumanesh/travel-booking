package com.armin.operation.address.ident.model;

import com.armin.utility.repository.common.SortOption;
import com.armin.utility.statics.enums.SortType;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther : Armin.Nik
 * @date : 20.11.22
 */
@Getter
@Setter
public class UserAddressPageableFilter extends UserAddressFilter {
    @Min(1)
    private int page;
    @Min(1)
    private int size;
    private List<SortOption> sort;

    public UserAddressPageableFilter() {
        this.page = 1;
        this.size = 50;
        this.sort = new ArrayList<>();
        this.sort.add(new SortOption("id", SortType.DESCENDING));
    }
}
