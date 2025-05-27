package com.armin.infrastructure.generic;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RowInfo {
    private int rowNumber;
    private int cellNumber;

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public void setCellNumber(int cellNumber) {
        this.cellNumber = cellNumber + 1;
    }

    public RowInfo(RowInfo rowInfo) {
        this.rowNumber = rowInfo.rowNumber;
        this.cellNumber = rowInfo.cellNumber;
    }
}
