package lab.s2jh.core.web;

import java.io.Serializable;

import lab.s2jh.core.entity.BaseEntity;

public abstract class BaseController<T extends BaseEntity<ID>, ID extends Serializable> extends
        PersistableController<T, ID> {

}
