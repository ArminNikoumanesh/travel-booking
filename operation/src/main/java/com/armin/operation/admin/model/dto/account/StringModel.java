package com.armin.operation.admin.model.dto.account;

import com.armin.utility.bl.NormalizeEngine;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StringModel {
    private String input;

    public void setInput(String input) {
        this.input = NormalizeEngine.normalizePersianDigits(input);
    }
}
