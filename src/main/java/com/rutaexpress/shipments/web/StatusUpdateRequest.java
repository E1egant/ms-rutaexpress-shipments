package com.rutaexpress.shipments.web;

import com.rutaexpress.contracts.ShipmentStatus;

public record StatusUpdateRequest(ShipmentStatus status) {
}
