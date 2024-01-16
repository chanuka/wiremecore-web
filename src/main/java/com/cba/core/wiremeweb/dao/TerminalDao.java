package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.Terminal;
import org.springframework.data.domain.Page;

public interface TerminalDao<T> extends GenericDao<T> {
    Page<Terminal> findTerminalsByMerchant(int id, int page, int pageSize) throws Exception;
}
