package com.armin.utility.repository.common;


import com.armin.utility.statics.enums.SortType;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SortOption implements Diffable<SortOption> {

    private String column;
    private SortType type;

    public SortOption() {
    }

    public SortOption(String column, SortType type) {
        this.column = column;
        this.type = type;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public SortType getType() {
        return type;
    }

    public void setType(SortType type) {
        this.type = type;
    }

    @Override
    public DiffResult<SortOption> diff(SortOption sortOption) {
        return new DiffBuilder(this, sortOption, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("column", this.column, sortOption.getColumn())
                .append("type", this.type, sortOption.getType())
                .build();
    }
}
