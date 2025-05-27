package com.armin.utility.repository.orm.object;

import java.util.HashMap;
import java.util.Map;

public class ReportQuery {
    private String select;
    private String from;
    private String where;
//    private String group;
//    private String having;
//    private String order;

    private Map<String, Object> whereParameters;
//    private Map<String, Object> havingParameters;

    public ReportQuery(String select, String from) {
        this.select = select;
        this.from = from;

        this.whereParameters = new HashMap<>();
//        this.havingParameters = new HashMap<>();
    }

    public ReportQuery(String select, String from, String where) {
        this.select = select;
        this.from = from;
        this.where = where;

        this.whereParameters = new HashMap<>();
//        this.havingParameters = new HashMap<>();
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

//    public String getGroup() {
//        return group;
//    }
//
//    public void setGroup(String group) {
//        this.group = group;
//    }
//
//    public String getHaving() {
//        return having;
//    }
//
//    public void setHaving(String having) {
//        this.having = having;
//    }
//
//    public String getOrder() {
//        return order;
//    }
//
//    public void setOrder(String order) {
//        this.order = order;
//    }

    public Map<String, Object> getWhereParameters() {
        return whereParameters;
    }

    public void setWhereParameters(Map<String, Object> whereParameters) {
        this.whereParameters = whereParameters;
    }

//    public Map<String, Object> getHavingParameters() {
//        return havingParameters;
//    }
//
//    public void setHavingParameters(Map<String, Object> havingParameters) {
//        this.havingParameters = havingParameters;
//    }
}
