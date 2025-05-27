package com.armin.utility.repository.odm.model.entity;

import lombok.Getter;

@Getter
public enum OperationAction {
    CREATE(0),
    UPDATE(1),
    DELETE(2),
    PATCH(3),
    UPDATE_ADDRESS(4),
    CANCEL(5),
    UNDO_CANCEL(6),
    UNDONE(7),
    VERIFY(8),
    UPSERT(9),
    CONFIRM_SUPPLY_ORDER(10);
    private int value;

    OperationAction(int value) {
        this.value = value;
    }
}
