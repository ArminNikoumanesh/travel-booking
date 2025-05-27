package com.armin.database.global.entity;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Getter
@Setter
@Entity
@Table(name = "GLOBAL_PROPERT")
public class GlobalPropertyEntity {

    @EmbeddedId
    private GlobalPropertyId id;

    @Type(JsonType.class)
    @Column(name = "PROPERTY", columnDefinition = "jsonb")
    private JsonNode property;
}
