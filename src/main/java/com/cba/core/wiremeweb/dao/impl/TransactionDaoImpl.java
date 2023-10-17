package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.TransactionDao;
import com.cba.core.wiremeweb.dto.MerchantCustomerResponseDto;
import com.cba.core.wiremeweb.dto.MerchantResponseDto;
import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import com.cba.core.wiremeweb.dto.TransactionCoreResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.MerchantCustomerMapper;
import com.cba.core.wiremeweb.mapper.MerchantMapper;
import com.cba.core.wiremeweb.mapper.TerminalMapper;
import com.cba.core.wiremeweb.mapper.TransactionCoreMapper;
import com.cba.core.wiremeweb.model.Merchant;
import com.cba.core.wiremeweb.model.MerchantCustomer;
import com.cba.core.wiremeweb.model.Terminal;
import com.cba.core.wiremeweb.model.TransactionCore;
import com.cba.core.wiremeweb.repository.MerchantCustomerRepository;
import com.cba.core.wiremeweb.repository.MerchantRepository;
import com.cba.core.wiremeweb.repository.TerminalRepository;
import com.cba.core.wiremeweb.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class TransactionDaoImpl implements TransactionDao {

    private final TerminalRepository terminalRepository;
    private final MerchantRepository merchantRepository;
    private final MerchantCustomerRepository merchantCustomerRepository;
    private final TransactionRepository transactionRepository;

    @Value("${application.resource.transactions}")
    private String resource;

    @Override
    public List<TerminalResponseDto> findAllTerminals() throws Exception {
        List<Terminal> entityList = terminalRepository.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Terminals found");
        }
        return entityList
                .stream()
                .map(TerminalMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MerchantResponseDto> getAllMerchants() throws Exception {
        List<Merchant> entityList = merchantRepository.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Merchants found");
        }
        return entityList
                .stream()
                .map(MerchantMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MerchantCustomerResponseDto> getAllMerchantCustomers() throws Exception {
        List<MerchantCustomer> entityList = merchantCustomerRepository.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Merchant Customers found");
        }
        return entityList
                .stream()
                .map(MerchantCustomerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TransactionCoreResponseDto> getAllTransactions(String dateFrom, String dateTo, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Page<TransactionCore> entitiesPage = transactionRepository.findByDateTimeBetween(dateFormat.parse(dateFrom), dateFormat.parse(dateTo),
                pageable);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Transactions found");
        }
        return entitiesPage.map(TransactionCoreMapper::toDto);
    }

    @Override
    public Map<String, ArrayList<Map<String, Object>>> getAllTransactionSummary(String dateFrom, String dateTo, String queryBy) throws Exception {

        Map<String, Object> map1 = new HashMap<>();
        map1.put("cardLabel", "visa");
        map1.put("count", 26);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("cardLabel", "master");
        map2.put("count", 3);

        ArrayList<Map<String, Object>> list1 = new ArrayList();
        list1.add(map1);
        list1.add(map2);

        Map<String, ArrayList<Map<String, Object>>> returnMap = new HashMap<>();
        returnMap.put("totalCount", list1);
        returnMap.put("totalAmount", list1);

        return returnMap;
    }

}
