package com.rutaexpress.contracts.dto;

/**
 * Datos del destinatario de un envío.
 */
public record RecipientDto(String name, String phone, String email, String address) {
}
