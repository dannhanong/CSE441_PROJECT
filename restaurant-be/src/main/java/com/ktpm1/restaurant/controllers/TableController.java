package com.ktpm1.restaurant.controllers;

import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.Table;
import com.ktpm1.restaurant.services.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tables")
public class TableController {
    @Autowired
    private TableService tableService;

    @GetMapping("/all")
    public ResponseEntity<List<Table>> getAllTables() {
        return ResponseEntity.ok(tableService.getAllTable());
    }

    @PostMapping("/admin/create")
    public ResponseEntity<Table> createTable(@RequestBody Table table) {
        return ResponseEntity.ok(tableService.createTable(table));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Table> getTable(Long id) {
        return ResponseEntity.ok(tableService.getTableById(id));
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<Table> updateTable(@RequestBody Table table, Long id) {
        return ResponseEntity.ok(tableService.updateTable(table, id));
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteTable(Long id) {
        return ResponseEntity.ok(tableService.deleteTable(id));
    }
}
