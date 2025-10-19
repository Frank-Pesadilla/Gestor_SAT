package com.gestor.gestor_sat.controller;

import com.gestor.gestor_sat.service.ExportacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exportacion")
@RequiredArgsConstructor
@Slf4j
public class ExportacionController {

    private final ExportacionService exportacionService;

    // 3.7 CSV
    @GetMapping("/clientes/csv")
    public ResponseEntity<byte[]> exportarCSV() {
        log.info("GET /api/exportacion/clientes/csv - Exportando CSV");
        byte[] body = exportacionService.exportarClientesCSV();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clientes.csv");
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    // 3.8 PDF
    @GetMapping("/clientes/pdf")
    public ResponseEntity<byte[]> exportarPDF() {
        log.info("GET /api/exportacion/clientes/pdf - Exportando PDF");
        byte[] body = exportacionService.exportarClientesPDF();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clientes.pdf"); // 3.9
        headers.setContentType(MediaType.APPLICATION_PDF);
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }
}
