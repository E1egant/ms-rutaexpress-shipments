package com.rutaexpress.contracts.dto;

import com.rutaexpress.contracts.ShipmentStatus;
import java.time.Instant;

/**
 * Representación pública de un envío.
 */
public record ShipmentResponse(
        Long id,
        String trackingNumber,
        String origin,
        String destination,
        Long courierId,
        ShipmentStatus status,
        RecipientDto recipient,
        PackageDto packageInfo,
        Instant createdAt,
        Instant updatedAt) {
}
