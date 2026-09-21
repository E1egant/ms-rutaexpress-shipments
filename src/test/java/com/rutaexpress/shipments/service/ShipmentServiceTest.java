package com.rutaexpress.shipments.service;

import com.rutaexpress.contracts.ShipmentStatus;
import com.rutaexpress.contracts.dto.PackageDto;
import com.rutaexpress.contracts.dto.RecipientDto;
import com.rutaexpress.contracts.dto.ShipmentRequest;
import com.rutaexpress.contracts.dto.ShipmentResponse;
import com.rutaexpress.shipments.domain.PackageInfo;
import com.rutaexpress.shipments.domain.Recipient;
import com.rutaexpress.shipments.domain.Shipment;
import com.rutaexpress.shipments.domain.ShipmentRepository;
import com.rutaexpress.shipments.exception.InvalidStateTransitionException;
import com.rutaexpress.shipments.exception.ResourceNotFoundException;
import com.rutaexpress.shipments.messaging.NotificationPublisher;
import com.rutaexpress.shipments.messaging.ShipmentEventPublisher;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    ShipmentRepository repository;
    @Mock
    ShipmentEventPublisher eventPublisher;
    @Mock
    NotificationPublisher notificationPublisher;

    ShipmentService service;

    @BeforeEach
    void setUp() {
        service = new ShipmentService(repository, new ShipmentMapper(), eventPublisher, notificationPublisher);
    }

    @Test
    void createGeneratesTrackingNumberAndPublishes() {
        when(repository.save(any(Shipment.class))).thenAnswer(inv -> {
            Shipment s = inv.getArgument(0);
            s.setId(1L);
            return s;
        });

        ShipmentRequest request = new ShipmentRequest("A", "B", null,
                new RecipientDto("Ana", "123", "a@x.com", "Calle 1"),
                new PackageDto(1.0, 0.5, "caja"));

        ShipmentResponse response = service.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.trackingNumber()).startsWith("RE-");
        assertThat(response.status()).isEqualTo(ShipmentStatus.CREATED);
        verify(eventPublisher).publish(any(Shipment.class));
        verify(notificationPublisher).publish(eq("email"), eq("a@x.com"), anyString(), anyString());
    }

    @Test
    void createRejectsMissingFields() {
        ShipmentRequest request = new ShipmentRequest("", "B", null,
                new RecipientDto("Ana", "123", "a@x.com", "Calle 1"),
                new PackageDto(1.0, 0.5, "caja"));

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateStatusValidTransition() {
        Shipment shipment = shipmentWith(ShipmentStatus.CREATED);
        when(repository.findById(1L)).thenReturn(Optional.of(shipment));
        when(repository.save(any(Shipment.class))).thenAnswer(inv -> inv.getArgument(0));

        ShipmentResponse response = service.updateStatus(1L, ShipmentStatus.ASSIGNED);

        assertThat(response.status()).isEqualTo(ShipmentStatus.ASSIGNED);
        verify(eventPublisher).publish(any(Shipment.class));
    }

    @Test
    void updateStatusInvalidTransitionThrows() {
        Shipment shipment = shipmentWith(ShipmentStatus.CREATED);
        when(repository.findById(1L)).thenReturn(Optional.of(shipment));

        assertThatThrownBy(() -> service.updateStatus(1L, ShipmentStatus.DELIVERED))
                .isInstanceOf(InvalidStateTransitionException.class);
    }

    @Test
    void findByIdNotFoundThrows() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private Shipment shipmentWith(ShipmentStatus status) {
        Shipment shipment = new Shipment();
        shipment.setId(1L);
        shipment.setTrackingNumber("RE-ABC123");
        shipment.setOrigin("A");
        shipment.setDestination("B");
        shipment.setStatus(status);

        Recipient recipient = new Recipient();
        recipient.setName("Ana");
        recipient.setPhone("123");
        recipient.setEmail("a@x.com");
        recipient.setAddress("Calle 1");
        shipment.setRecipient(recipient);

        PackageInfo packageInfo = new PackageInfo();
        packageInfo.setWeightKg(1.0);
        packageInfo.setVolumeM3(0.5);
        packageInfo.setDescription("caja");
        shipment.setPackageInfo(packageInfo);
        return shipment;
    }
}
