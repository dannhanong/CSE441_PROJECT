package com.ktpm1.restaurant.services.impls;

import com.ktpm1.restaurant.dtos.request.CatalogRequest;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.Catalog;
import com.ktpm1.restaurant.models.Table;
import com.ktpm1.restaurant.repositories.CatalogRepository;
import com.ktpm1.restaurant.repositories.TableRepository;
import com.ktpm1.restaurant.services.CatalogService;
import com.ktpm1.restaurant.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    private CatalogRepository catalogRepository;
    @Autowired
    private TableRepository tableRepository;
    @Autowired
    private FileUploadService fileUploadService;

    @Override
    public Catalog createCatalog(CatalogRequest catalogRequest) {
        Catalog catalog = null;
        try {
            catalog = Catalog.builder()
                    .name(catalogRequest.getName())
                    .description(catalogRequest.getDescription())
                    .image(fileUploadService.uploadFile(catalogRequest.getFile()).getFileCode())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Catalog newCatalog = catalogRepository.save(catalog);
        for (int i = 0; i < catalogRequest.getQuantityOfTables(); i++) {
            Table table = Table.builder()
                    .catalog(newCatalog)
                    .tableNumber(newCatalog.getName() + "-" + (i + 1))
                    .capacity(catalogRequest.getCapacity())
                    .available(true)
                    .build();
            tableRepository.save(table);
        }
        return catalog;
    }

    @Override
    public Catalog getCatalog(Long id) {
        return catalogRepository.findById(id).orElse(null);
    }

    @Override
    public List<Catalog> getAllCatalogs() {
        return catalogRepository.findAll();
    }

    @Override
    public List<Catalog> getAlCatalogsByKeyword(String keyword) {
        return catalogRepository.findByNameContaining(keyword);
    }

    @Override
    public ResponseMessage updateCatalog(Long id, CatalogRequest catalogRequest) {
        Catalog catalog = catalogRepository.findById(id).orElse(null);
        if (catalog == null) {
            return new ResponseMessage(404, "Catalog not found");
        }
        try {
            catalog.setName(catalogRequest.getName());
            catalog.setDescription(catalogRequest.getDescription());
            catalog.setImage(fileUploadService.uploadFile(catalogRequest.getFile()).getFileCode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        catalogRepository.save(catalog);
        return new ResponseMessage(200, "Cập nhật thành công");
    }
}
