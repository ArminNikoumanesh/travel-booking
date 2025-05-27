package com.armin.utility.repository.orm.object;

import com.armin.utility.repository.common.ConditionParameters;
import lombok.Getter;
import lombok.Setter;

import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class ReportCondition {
    private List<ConditionParameters> controlFlag;

    private List<String> nullCondition;
    private List<String> notNullCondition;

    private List<ConditionParameters> equalCondition;
    private List<ConditionParameters> notEqualCondition;

    private List<ConditionParameters> likeCondition;
    private List<ConditionParameters> caseInsensitiveLikeCondition;
    private List<ConditionParameters> inCondition;

    private List<ConditionParameters> minNumberCondition;
    private List<ConditionParameters> maxNumberCondition;

    private List<ConditionParameters> minDateCondition;
    private List<ConditionParameters> maxDateCondition;

    private List<ConditionParameters> minTimeCondition;
    private List<ConditionParameters> maxTimeCondition;

    private ReportCondition orCondition;
    private List<ReportCriteriaJoinCondition> joinCondition;
    private List<ConditionParameters> notInCondition;

    private List<ConditionParameters> minSqlCondition;
    private List<ConditionParameters> maxSqlCondition;
    private List<HaveCondition> haveCondition;
    private List<ReportCondition> orConditions;
    private List<ReportCondition> andConditions;

    public ReportCondition() {
        this.controlFlag = new ArrayList<>();
        this.nullCondition = new ArrayList<>();
        this.notNullCondition = new ArrayList<>();
        this.equalCondition = new ArrayList<>();
        this.notEqualCondition = new ArrayList<>();
        this.likeCondition = new ArrayList<>();
        this.caseInsensitiveLikeCondition = new ArrayList<>();
        this.inCondition = new ArrayList<>();
        this.notInCondition = new ArrayList<>();
        this.minNumberCondition = new ArrayList<>();
        this.maxNumberCondition = new ArrayList<>();
        this.minDateCondition = new ArrayList<>();
        this.maxDateCondition = new ArrayList<>();
        this.minTimeCondition = new ArrayList<>();
        this.maxTimeCondition = new ArrayList<>();
        this.joinCondition = new ArrayList<>();
        this.minSqlCondition = new ArrayList<>();
        this.maxSqlCondition = new ArrayList<>();
        this.haveCondition = new ArrayList<>();
        this.orConditions = new ArrayList<>();
        this.andConditions = new ArrayList<>();
    }

    public void addControlFlag(String key, Boolean value) {
        this.controlFlag.add(new ConditionParameters(key, value));
    }

    public void addNullCondition(String value) {
        this.nullCondition.add(value);
    }

    public void addNotNullCondition(String value) {
        this.notNullCondition.add(value);
    }

    public void addEqualCondition(String key, Object value) {
        this.equalCondition.add(new ConditionParameters(key, value));
    }

    public void addNotEqualCondition(String key, Object value) {
        this.notEqualCondition.add(new ConditionParameters(key, value));
    }

    public void addLikeCondition(String key, String value) {
        this.likeCondition.add(new ConditionParameters(key, value));
    }

    public void addCaseInsensitiveLikeCondition(String key, String value) {
        this.caseInsensitiveLikeCondition.add(new ConditionParameters(key, value));
    }

    public void addInCondition(String key, List<?> value) {
        this.inCondition.add(new ConditionParameters(key, value));
    }

    public void addNotInCondition(String key, List<?> value) {
        this.notInCondition.add(new ConditionParameters(key, value));
    }

    public void addMinNumberCondition(String key, Number value) {
        this.minNumberCondition.add(new ConditionParameters(key, value));
    }

    public void addMaxNumberCondition(String key, Number value) {
        this.maxNumberCondition.add(new ConditionParameters(key, value));
    }

    public void addMinTimeCondition(String key, Temporal value) {
        this.minTimeCondition.add(new ConditionParameters(key, value));
    }

    public void addMaxTimeCondition(String key, Temporal value) {
        this.maxTimeCondition.add(new ConditionParameters(key, value));
    }

    public void addMinZonedTimeCondition(String key, Temporal value) {
        this.minTimeCondition.add(new ConditionParameters(key, value));
    }

    public void addMaxZonedTimeCondition(String key, Temporal value) {
        this.maxTimeCondition.add(new ConditionParameters(key, value));
    }

    public void addJoinCondition(ReportCriteriaJoinCondition value) {
        this.joinCondition.add(value);
    }

    public void addMinSqlCondition(String key, Object value) {
        this.minSqlCondition.add(new ConditionParameters(key, value));
    }

    public void addMaxSqlCondition(String key, Object value) {
        this.maxSqlCondition.add(new ConditionParameters(key, value));
    }

    public void addHaveCondition(String column, Object value, AggregateOperation operation, ColumnAction action) {
        this.haveCondition.add(new HaveCondition(column, value, operation, action));
    }

    public void addOrCondition(ReportCondition reportCondition) {
        this.orConditions.add(reportCondition);
    }

    public void addAndCondition(ReportCondition reportCondition) {
        this.andConditions.add(reportCondition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportCondition)) return false;
        ReportCondition that = (ReportCondition) o;
        return Objects.equals(controlFlag, that.controlFlag) &&
                Objects.equals(nullCondition, that.nullCondition) &&
                Objects.equals(notNullCondition, that.notNullCondition) &&
                Objects.equals(equalCondition, that.equalCondition) &&
                Objects.equals(notEqualCondition, that.notEqualCondition) &&
                Objects.equals(likeCondition, that.likeCondition) &&
                Objects.equals(caseInsensitiveLikeCondition, that.caseInsensitiveLikeCondition) &&
                Objects.equals(inCondition, that.inCondition) &&
                Objects.equals(minNumberCondition, that.minNumberCondition) &&
                Objects.equals(maxNumberCondition, that.maxNumberCondition) &&
                Objects.equals(minDateCondition, that.minDateCondition) &&
                Objects.equals(maxDateCondition, that.maxDateCondition) &&
                Objects.equals(minTimeCondition, that.minTimeCondition) &&
                Objects.equals(maxTimeCondition, that.maxTimeCondition) &&
                Objects.equals(orCondition, that.orCondition) &&
                Objects.equals(joinCondition, that.joinCondition) &&
                Objects.equals(notInCondition, that.notInCondition) &&
                Objects.equals(minSqlCondition, that.minSqlCondition) &&
                Objects.equals(maxSqlCondition, that.maxSqlCondition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(controlFlag, nullCondition, notNullCondition, equalCondition, notEqualCondition, likeCondition, caseInsensitiveLikeCondition, inCondition, minNumberCondition, maxNumberCondition, minDateCondition, maxDateCondition, minTimeCondition, maxTimeCondition, orCondition, joinCondition, notInCondition, minSqlCondition, maxSqlCondition);
    }
}
