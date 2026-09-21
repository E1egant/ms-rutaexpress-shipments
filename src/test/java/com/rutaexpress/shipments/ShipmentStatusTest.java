package com.rutaexpress.shipments;

import com.rutaexpress.contracts.ShipmentStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentStatusTest {

    @Test
    void validTransitions() {
        assertThat(ShipmentStatus.CREATED.canTransitionTo(ShipmentStatus.ASSIGNED)).isTrue();
        assertThat(ShipmentStatus.ASSIGNED.canTransitionTo(ShipmentStatus.PICKED_UP)).isTrue();
        assertThat(ShipmentStatus.PICKED_UP.canTransitionTo(ShipmentStatus.IN_TRANSIT)).isTrue();
        assertThat(ShipmentStatus.IN_TRANSIT.canTransitionTo(ShipmentStatus.DELIVERED)).isTrue();
    }

    @Test
    void cancelAndFailTransitions() {
        assertThat(ShipmentStatus.CREATED.canTransitionTo(ShipmentStatus.CANCELLED)).isTrue();
        assertThat(ShipmentStatus.ASSIGNED.canTransitionTo(ShipmentStatus.CANCELLED)).isTrue();
        assertThat(ShipmentStatus.PICKED_UP.canTransitionTo(ShipmentStatus.FAILED)).isTrue();
        assertThat(ShipmentStatus.IN_TRANSIT.canTransitionTo(ShipmentStatus.FAILED)).isTrue();
    }

    @Test
    void invalidTransitions() {
        assertThat(ShipmentStatus.CREATED.canTransitionTo(ShipmentStatus.DELIVERED)).isFalse();
        assertThat(ShipmentStatus.ASSIGNED.canTransitionTo(ShipmentStatus.CREATED)).isFalse();
        assertThat(ShipmentStatus.PICKED_UP.canTransitionTo(ShipmentStatus.CREATED)).isFalse();
        assertThat(ShipmentStatus.DELIVERED.canTransitionTo(ShipmentStatus.IN_TRANSIT)).isFalse();
    }

    @Test
    void noSelfTransition() {
        assertThat(ShipmentStatus.CREATED.canTransitionTo(ShipmentStatus.CREATED)).isFalse();
    }

    @Test
    void terminalStates() {
        assertThat(ShipmentStatus.DELIVERED.isTerminal()).isTrue();
        assertThat(ShipmentStatus.CANCELLED.isTerminal()).isTrue();
        assertThat(ShipmentStatus.FAILED.isTerminal()).isTrue();
        assertThat(ShipmentStatus.CREATED.isTerminal()).isFalse();
    }
}
