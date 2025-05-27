package com.armin.operation.security.admin.dto.role;

import com.armin.utility.bl.NormalizeEngine;
import com.armin.utility.repository.common.SortOption;
import com.armin.utility.statics.enums.SortType;
import com.armin.database.user.statics.RoleType;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class RoleInfoPageableFilter {
    @Min(1)
    private Integer page;
    @Min(1)
    private Integer size;
    private List<SortOption> sort;
    private String keyword;
    private Integer id;
    private RoleType type;

    public RoleInfoPageableFilter() {
        this.page = 1;
        this.size = 50;
        this.sort = new ArrayList<>();
        this.sort.add(new SortOption("id", SortType.DESCENDING));
    }

    public void setKeyword(String keyword) {
        this.keyword = NormalizeEngine.trimAndNormalizePersianString(keyword);
    }
}
