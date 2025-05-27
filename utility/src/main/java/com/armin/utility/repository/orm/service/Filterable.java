package com.armin.utility.repository.orm.service;

import com.armin.utility.repository.orm.object.ReportFilter;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
public interface Filterable<T extends FilterBase> {

    ReportFilter filter(T filter);
}
