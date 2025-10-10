package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.entity.Cliente;
import com.gestor.gestor_sat.repository.ClienteRepository;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExportacionService {

    private final ClienteRepository clienteRepository;

    private void validarPermisosExportacion() {
        // Ajusta según tu seguridad; ahora todos los endpoints están permitAll.
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new RuntimeException("Usuario no autenticado para exportar");
        }
    }

    // 3.2 & 3.4 CSV
    public byte[] exportarClientesCSV() {
        validarPermisosExportacion(); // 3.6
        List<Cliente> clientes = clienteRepository.findAll();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             OutputStreamWriter osw = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
             CSVWriter writer = new CSVWriter(osw)) {

            writer.writeNext(new String[]{"ID","Nombre","DPI","FechaNacimiento","Telefono","Direccion"});
            for (Cliente c : clientes) {
                writer.writeNext(new String[]{
                        String.valueOf(c.getIdCliente()),
                        c.getNombreCompleto(),
                        c.getDpi(),
                        c.getFechaNacimiento()!=null?c.getFechaNacimiento().toString():"",
                        c.getTelefono(),
                        c.getDireccion()
                });
            }
            writer.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Error generando CSV: ", e);
            throw new RuntimeException("Error generando CSV: " + e.getMessage());
        }
    }

    // 3.3 & 3.5 PDF (PDFBox)
    public byte[] exportarClientesPDF() {
        validarPermisosExportacion(); // 3.6
        List<Cliente> clientes = clienteRepository.findAll();
        try (PDDocument doc = new PDDocument(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);
            PDPageContentStream cs = new PDPageContentStream(doc, page);

            float y = page.getMediaBox().getHeight() - 50;
            cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
            cs.beginText(); cs.newLineAtOffset(50, y); cs.showText("Listado de Clientes"); cs.endText();
            y -= 25;

            cs.setFont(PDType1Font.HELVETICA, 10);
            for (Cliente c : clientes) {
                if (y < 50) { // nueva página
                    cs.close();
                    page = new PDPage(PDRectangle.LETTER);
                    doc.addPage(page);
                    cs = new PDPageContentStream(doc, page);
                    y = page.getMediaBox().getHeight() - 50;
                    cs.setFont(PDType1Font.HELVETICA, 10);
                }
                cs.beginText(); cs.newLineAtOffset(50, y);
                cs.showText(String.format("%d | %s | %s | %s",
                        c.getIdCliente(), c.getNombreCompleto(), c.getDpi(),
                        c.getFechaNacimiento()!=null?c.getFechaNacimiento().toString():""));
                cs.endText();
                y -= 14;
            }
            cs.close();
            doc.save(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Error generando PDF: ", e);
            throw new RuntimeException("Error generando PDF: " + e.getMessage());
        }
    }
}
