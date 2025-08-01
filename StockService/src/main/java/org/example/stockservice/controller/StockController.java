package org.example.stockservice.controller;

import org.example.stockservice.Service.StockService;
import org.example.stockservice.dto.ProductDetailsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/stockcontroller")
public class StockController {

    public StockService stockService;
    public final Logger log = LoggerFactory.getLogger(getClass());
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/getStockByProductId")
    public ResponseEntity<ProductDetailsDto> getStockByProductId(@RequestParam Long productId, @RequestParam Long quantity) {
        ProductDetailsDto productDetailsDto = stockService.getProductDetails(productId, quantity);
        log.info("Retrieved product details for Product ID: {}", productId);
        if (productDetailsDto != null) {
            return ResponseEntity.ok(productDetailsDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
