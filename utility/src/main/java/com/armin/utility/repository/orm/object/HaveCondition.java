package com.armin.utility.repository.orm.object;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HaveCondition {
    private String column;
    private Object value;
    private AggregateOperation operation;
    private ColumnAction action;

    public HaveCondition(String column, Object value, AggregateOperation operation) {
        this.column = column;
        this.value = value;
        this.operation = operation;
    }

    public HaveCondition(String column, Object value, AggregateOperation operation, ColumnAction action) {
        this.column = column;
        this.value = value;
        this.operation = operation;
        this.action = action;
    }
}
