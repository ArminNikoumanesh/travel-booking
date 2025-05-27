package com.armin.utility.repository.orm.object;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.criteria.JoinType;

@Getter
@Setter
public class ReportCriteriaJoinCondition extends ReportCondition {
    private String name;
    private JoinType joinType;
    private boolean fetch = false;

    public ReportCriteriaJoinCondition(String name, JoinType joinType) {
        super();
        this.name = name;
        this.joinType = joinType;
    }

    public ReportCriteriaJoinCondition(String name, JoinType joinType, boolean fetch) {
        super();
        this.name = name;
        this.joinType = joinType;
        this.fetch = fetch;
    }

}
