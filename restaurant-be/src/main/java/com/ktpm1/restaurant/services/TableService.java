package com.ktpm1.restaurant.services;

import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.Table;

import java.util.List;

public interface TableService {
    Table createTable(Table table);
    Table getTableById(Long id);
    Table getTableByTableNumber(String tableNumber);
    Table updateTable(Table table, Long id);
    List<Table> getAllTable();
    ResponseMessage deleteTable(Long id);
    Table updateTableStatus(Long id);
    List<Table> getAvailableTables();
}
