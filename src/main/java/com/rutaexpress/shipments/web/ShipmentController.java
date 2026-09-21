package com.rutaexpress.shipments.web;

import com.rutaexpress.contracts.ApiPaths;
import com.rutaexpress.contracts.dto.ShipmentRequest;
import com.rutaexpress.contracts.dto.ShipmentResponse;
import com.rutaexpress.shipments.service.ShipmentService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.SHIPMENTS)
public class ShipmentController {

    private final ShipmentService service;

    public ShipmentController(ShipmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<ShipmentResponse> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ShipmentResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShipmentResponse create(@RequestBody ShipmentRequest request) {
        return service.create(request);
    }

    @PatchMapping("/{id}/status")
    public ShipmentResponse updateStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest body) {
        return service.updateStatus(id, body.status());
    }
}
