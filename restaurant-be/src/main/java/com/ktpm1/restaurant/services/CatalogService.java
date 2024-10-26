package com.ktpm1.restaurant.services;

import com.ktpm1.restaurant.dtos.request.CatalogRequest;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.Catalog;

import java.util.List;

public interface CatalogService {
    Catalog createCatalog(CatalogRequest catalogRequest);
    Catalog getCatalog(Long id);
    List<Catalog> getAllCatalogs();
    List<Catalog> getAlCatalogsByKeyword(String keyword);
    ResponseMessage updateCatalog(Long id, CatalogRequest catalogRequest);
}
