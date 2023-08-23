package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.TerminalRequestDto;
import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import com.cba.core.wiremeweb.model.Merchant;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.model.Terminal;

public class TerminalMapper {

    public static TerminalResponseDto toDto(Terminal terminal) {
        TerminalResponseDto terminalResponseDto = new TerminalResponseDto();
        terminalResponseDto.setTerminalId(terminal.getTerminalId());
        terminalResponseDto.setDeviceId(terminal.getDeviceId());
        terminalResponseDto.setMerchantId(terminal.getMerchant().getId());
        terminalResponseDto.setStatus(terminal.getStatus().getStatusCode());
        terminalResponseDto.setId(terminal.getId());
        return terminalResponseDto;
    }

    public static Terminal toModel(TerminalRequestDto terminalRequestDto) {
        Terminal terminal = new Terminal();
        terminal.setTerminalId(terminalRequestDto.getTerminalId());
        terminal.setMerchant(new Merchant(terminalRequestDto.getMerchantId()));
        terminal.setStatus(new Status(terminalRequestDto.getStatus()));
        terminal.setDeviceId(terminalRequestDto.getDeviceId());
        return terminal;
    }
}
