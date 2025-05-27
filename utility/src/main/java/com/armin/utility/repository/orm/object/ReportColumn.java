package com.armin.utility.repository.orm.object;

public class ReportColumn {
    private String column;
    private String alias;

    public ReportColumn(String column, String alias) {
        this.column = column;
        this.alias = alias;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
