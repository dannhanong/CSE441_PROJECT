package com.ktpm1.restaurant.services.impls;

import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.Table;
import com.ktpm1.restaurant.repositories.TableRepository;
import com.ktpm1.restaurant.services.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableServiceImpl implements TableService {
    @Autowired
    private TableRepository tableRepository;

    @Override
    public Table createTable(Table table) {
        return tableRepository.save(table);
    }

    @Override
    public Table getTableById(Long id) {
        return tableRepository.findById(id).orElse(null);
    }

    @Override
    public Table getTableByTableNumber(String tableNumber) {
        return tableRepository.findByTableNumber(tableNumber);
    }

    @Override
    public Table updateTable(Table table, Long id) {
        return tableRepository.findById(id)
                .map(t -> {
                    t.setTableNumber(table.getTableNumber());
                    t.setCapacity(table.getCapacity());
                    t.setAvailable(table.isAvailable());
                    return tableRepository.save(t);
                })
                .orElse(null);
    }

    @Override
    public List<Table> getAllTable() {
        return tableRepository.findAll();
    }

    @Override
    public ResponseMessage deleteTable(Long id) {
        tableRepository.deleteById(id);
        return ResponseMessage.builder().status(200).message("Xóa bàn thành công").build();
    }

    @Override
    public Table updateTableStatus(Long id) {
        return tableRepository.findById(id)
                .map(t -> {
                    t.setAvailable(!t.isAvailable());
                    return tableRepository.save(t);
                })
                .orElse(null);
    }

    @Override
    public List<Table> getAvailableTables() {
        List<Table> allTables = tableRepository.findAll();
        allTables.removeIf(table -> !table.isAvailable());
        return allTables;
    }
}
