package com.armin.infrastructure.common;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "handlebars_template")
public class HandlebarsTemplateEntity {
    @Id
    @Column(name = "id_pk", scale = 0, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HandlebarsTemplateSeq")
    @SequenceGenerator(name = "HandlebarsTemplateSeq", sequenceName = "handlebars_template_seq", allocationSize = 10)
    private int id;
    @Basic
    @Column(name = "name", unique = true, nullable = false, updatable = false)
    private String name;
    @Basic
    @Column(name = "content", nullable = false)
    private String content;
    @Basic
    @Column(name = "parameters")
    private String parameters;

}


