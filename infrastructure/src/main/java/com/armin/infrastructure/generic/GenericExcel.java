package com.armin.infrastructure.generic;

import org.apache.poi.ss.usermodel.Row;

public class GenericExcel {
    public GenericExcel genericExcel = new GenericExcel();

    private GenericExcel() {
    }

    public static Double getNumericValue(Row row, RowInfo rowInfo, int index) {
        rowInfo.setRowNumber(index);
        return row.getCell(index) != null ? row.getCell(index).getNumericCellValue() : -1;
    }

    public static String getStringValue(Row row, RowInfo rowInfo, int index) {
        rowInfo.setCellNumber(index);
        if (row.getCell(index) != null) {
            switch (row.getCell(index).getCellType()) {
                case STRING:
                    return row.getCell(index).getStringCellValue();
                case NUMERIC:
                    return (Number) row.getCell(index).getNumericCellValue() + "";
            }
        }
        return null;
    }

}
