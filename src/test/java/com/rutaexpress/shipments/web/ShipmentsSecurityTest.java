package com.rutaexpress.shipments.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/** Perfil `secure`: exige JWT y rol. Los tokens se simulan con jwt(). */
@SpringBootTest(properties = {"AZURE_TENANT_ID=test-tenant", "AZURE_CLIENT_ID=test-client", "AZURE_API_AUDIENCE=api://test"})
@AutoConfigureMockMvc
@ActiveProfiles("secure")
class ShipmentsSecurityTest {

    @Autowired
    MockMvc mvc;

    @Test
    void sinTokenRetorna401() throws Exception {
        mvc.perform(get("/api/shipments")).andExpect(status().isUnauthorized());
    }

    @Test
    void conRolAdminRetorna200() throws Exception {
        mvc.perform(get("/api/shipments").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_Admin"))))
                .andExpect(status().isOk());
    }

    @Test
    void bodegaNoPuedeCrearEnvios() throws Exception {
        mvc.perform(post("/api/shipments").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_Bodega")))
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void healthEsPublicoAunqueElBrokerNoEsteDisponible() throws Exception {
        // 200 si todo está arriba; 503 si un broker no responde. Lo relevante: no exige token.
        mvc.perform(get("/actuator/health")).andExpect(status().is(org.hamcrest.Matchers.oneOf(200, 503)));
    }
}
