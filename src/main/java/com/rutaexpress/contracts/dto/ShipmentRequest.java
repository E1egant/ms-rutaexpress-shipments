package com.rutaexpress.contracts.dto;

/**
 * Payload de creación de un envío.
 */
public record ShipmentRequest(
        String origin,
        String destination,
        Long courierId,
        RecipientDto recipient,
        PackageDto packageInfo) {
}
