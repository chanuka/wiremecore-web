package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import org.springframework.data.domain.Page;

public interface TerminalService<T, K> extends GenericService<T, K> {
    Page<TerminalResponseDto> findTerminalsByMerchant(int id, int page, int pageSize) throws Exception;
}
