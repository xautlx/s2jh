package lab.s2jh.core.dao;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BaseDao<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID>,
        JpaSpecificationExecutor<T> {

}
