package com.rutaexpress.shipments.service;

import com.rutaexpress.contracts.ShipmentStatus;
import com.rutaexpress.contracts.dto.PackageDto;
import com.rutaexpress.contracts.dto.RecipientDto;
import com.rutaexpress.contracts.dto.ShipmentRequest;
import com.rutaexpress.contracts.dto.ShipmentResponse;
import com.rutaexpress.shipments.domain.PackageInfo;
import com.rutaexpress.shipments.domain.Recipient;
import com.rutaexpress.shipments.domain.Shipment;
import org.springframework.stereotype.Component;

@Component
public class ShipmentMapper {

    public Shipment toEntity(ShipmentRequest request) {
        Shipment shipment = new Shipment();
        shipment.setOrigin(request.origin());
        shipment.setDestination(request.destination());
        shipment.setCourierId(request.courierId());
        shipment.setStatus(ShipmentStatus.CREATED);
        shipment.setRecipient(toRecipient(request.recipient()));
        shipment.setPackageInfo(toPackageInfo(request.packageInfo()));
        return shipment;
    }

    public ShipmentResponse toResponse(Shipment shipment) {
        return new ShipmentResponse(
                shipment.getId(),
                shipment.getTrackingNumber(),
                shipment.getOrigin(),
                shipment.getDestination(),
                shipment.getCourierId(),
                shipment.getStatus(),
                toRecipientDto(shipment.getRecipient()),
                toPackageDto(shipment.getPackageInfo()),
                shipment.getCreatedAt(),
                shipment.getUpdatedAt());
    }

    private Recipient toRecipient(RecipientDto dto) {
        Recipient recipient = new Recipient();
        recipient.setName(dto.name());
        recipient.setPhone(dto.phone());
        recipient.setEmail(dto.email());
        recipient.setAddress(dto.address());
        return recipient;
    }

    private PackageInfo toPackageInfo(PackageDto dto) {
        PackageInfo info = new PackageInfo();
        info.setWeightKg(dto.weightKg());
        info.setVolumeM3(dto.volumeM3());
        info.setDescription(dto.description());
        return info;
    }

    private RecipientDto toRecipientDto(Recipient recipient) {
        return new RecipientDto(recipient.getName(), recipient.getPhone(),
                recipient.getEmail(), recipient.getAddress());
    }

    private PackageDto toPackageDto(PackageInfo info) {
        return new PackageDto(info.getWeightKg(), info.getVolumeM3(), info.getDescription());
    }
}
