package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import org.springframework.data.domain.Page;

public interface TerminalDao<T, K> extends GenericDao<T, K> {
    Page<TerminalResponseDto> findTerminalsByMerchant(int id, int page, int pageSize) throws Exception;
}
