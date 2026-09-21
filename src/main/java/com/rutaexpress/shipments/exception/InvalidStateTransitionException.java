package com.rutaexpress.shipments.exception;

import com.rutaexpress.contracts.ShipmentStatus;

public class InvalidStateTransitionException extends RuntimeException {

    public InvalidStateTransitionException(ShipmentStatus from, ShipmentStatus to) {
        super("Transición de estado inválida: " + from + " -> " + to);
    }
}
