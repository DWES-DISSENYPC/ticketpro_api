package com.ticketpro.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelefonoDTO {
    private String numero;
    private String tipo; // MOVIL, FIJO, TRABAJO...
}

