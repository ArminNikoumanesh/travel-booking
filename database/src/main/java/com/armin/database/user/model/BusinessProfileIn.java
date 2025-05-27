package com.armin.database.user.model;

import com.armin.database.user.statics.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class BusinessProfileIn {
    private String firstName;
    private String lastName;
    private String fatherName;
    private Gender gender;
    private String nationalId;
    private LocalDate birthDate;
    private boolean hasBusiness;

    public BusinessProfileIn(String firstName, String lastName, String fatherName, Gender gender, String nationalId, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
        this.gender = gender;
        this.nationalId = nationalId;
        this.birthDate = birthDate;
        this.hasBusiness = true;
    }
}
