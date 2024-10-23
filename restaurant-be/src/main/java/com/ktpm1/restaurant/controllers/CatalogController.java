package com.ktpm1.restaurant.controllers;

import com.ktpm1.restaurant.dtos.request.CatalogRequest;
import com.ktpm1.restaurant.models.Catalog;
import com.ktpm1.restaurant.services.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalogs")
public class CatalogController {
    @Autowired
    private CatalogService catalogService;

    @PostMapping("/admin/create")
    public ResponseEntity<Catalog> createCatalog(@ModelAttribute CatalogRequest catalogRequest) {
        return ResponseEntity.ok(catalogService.createCatalog(catalogRequest));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Catalog> getCatalog(@PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getCatalog(id));
    }

//    @GetMapping("/all")
//    public ResponseEntity<Iterable<Catalog>> getAllCatalogs() {
//        return ResponseEntity.ok(catalogService.getAllCatalogs());
//    }
    @GetMapping("/all")
    public ResponseEntity<List<Catalog>> getAllCatalogs(@RequestParam(defaultValue = "") String keyword) {
        return ResponseEntity.ok(catalogService.getAlCatalogsByKeyword(keyword));
    }
}
