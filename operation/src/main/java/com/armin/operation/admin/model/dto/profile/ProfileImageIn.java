package com.armin.operation.admin.model.dto.profile;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Size;

@Getter
@Setter
public class ProfileImageIn {
    @Size(max = 100)
    private String image;
}