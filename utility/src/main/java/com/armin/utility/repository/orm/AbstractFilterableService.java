package com.armin.utility.repository.orm;

import com.armin.utility.repository.orm.service.FilterBase;
import com.armin.utility.repository.orm.service.Filterable;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
public abstract class AbstractFilterableService<T, F extends FilterBase, D extends Dao<T>>
        extends AbstractService<T, D>
        implements Filterable<F> {

    public AbstractFilterableService() {
    }

    public AbstractFilterableService(D dao) {
        super(dao);
    }
}
