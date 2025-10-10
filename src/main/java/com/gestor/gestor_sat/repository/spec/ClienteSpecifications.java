package com.gestor.gestor_sat.repository.spec;

import com.gestor.gestor_sat.entity.Cliente;
import com.gestor.gestor_sat.entity.enums.TramiteEstado;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ClienteSpecifications {

    public static Specification<Cliente> nombreContains(String nombre) {
        return (root, q, cb) ->
            (nombre == null || nombre.isBlank())
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("nombreCompleto")), "%" + nombre.toLowerCase() + "%");
    }

    public static Specification<Cliente> dpiEquals(String dpi) {
        return (root, q, cb) ->
            (dpi == null || dpi.isBlank())
                ? cb.conjunction()
                : cb.equal(root.get("dpi"), dpi);
    }

    // join a consultaTramites para filtrar por fecha
    public static Specification<Cliente> consultaEntre(LocalDate inicio, LocalDate fin) {
        return (root, q, cb) -> {
            if (inicio == null && fin == null) return cb.conjunction();
            var join = root.join("consultaTramites"); // <-- asegúrate que así se llama en Cliente
            if (inicio != null && fin != null) return cb.between(join.get("fechaTramite"), inicio, fin);
            if (inicio != null) return cb.greaterThanOrEqualTo(join.get("fechaTramite"), inicio);
            return cb.lessThanOrEqualTo(join.get("fechaTramite"), fin);
        };
    }

    public static Specification<Cliente> consultaEstado(String estado) {
        return (root, q, cb) -> {
            if (estado == null || estado.isBlank()) return cb.conjunction();
            var join = root.join("consultaTramites"); // <-- nombre exacto de la colección
            TramiteEstado enumEstado = TramiteEstado.valueOf(estado.toUpperCase());
            return cb.equal(join.get("estado"), enumEstado);
        };
    }
}
