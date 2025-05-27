package com.armin.utility.repository.odm.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InnerDto<PARENT, CHILD> {
    private PARENT parent;
    private CHILD child;

    public InnerDto(PARENT parent, CHILD child) {
        this.parent = parent;
        this.child = child;
    }
}
