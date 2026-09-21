package com.rutaexpress.shipments.service;

import com.rutaexpress.contracts.ShipmentStatus;
import com.rutaexpress.contracts.dto.ShipmentRequest;
import com.rutaexpress.contracts.dto.ShipmentResponse;
import com.rutaexpress.shipments.domain.Shipment;
import com.rutaexpress.shipments.domain.ShipmentRepository;
import com.rutaexpress.shipments.exception.InvalidStateTransitionException;
import com.rutaexpress.shipments.exception.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShipmentService {

    private final ShipmentRepository repository;
    private final ShipmentMapper mapper;

    public ShipmentService(ShipmentRepository repository, ShipmentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public ShipmentResponse create(ShipmentRequest request) {
        validate(request);
        Shipment shipment = mapper.toEntity(request);
        shipment.setTrackingNumber(generateTrackingNumber());
        return mapper.toResponse(repository.save(shipment));
    }

    @Transactional(readOnly = true)
    public ShipmentResponse findById(Long id) {
        return mapper.toResponse(load(id));
    }

    @Transactional(readOnly = true)
    public List<ShipmentResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional
    public ShipmentResponse updateStatus(Long id, ShipmentStatus target) {
        Shipment shipment = load(id);
        if (!shipment.getStatus().canTransitionTo(target)) {
            throw new InvalidStateTransitionException(shipment.getStatus(), target);
        }
        shipment.setStatus(target);
        return mapper.toResponse(repository.save(shipment));
    }

    private Shipment load(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment", id));
    }

    private void validate(ShipmentRequest request) {
        if (request == null || isBlank(request.origin()) || isBlank(request.destination())
                || request.recipient() == null || request.packageInfo() == null) {
            throw new IllegalArgumentException("origin, destination, recipient y packageInfo son obligatorios");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String generateTrackingNumber() {
        return "RE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
