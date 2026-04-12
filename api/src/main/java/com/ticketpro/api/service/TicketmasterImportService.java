package com.ticketpro.api.service; // Ajusta a tu paquete real

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketpro.api.model.Evento;
import com.ticketpro.api.model.Sala;
import com.ticketpro.api.model.Sesion;
import com.ticketpro.api.model.Ubicacion;
import com.ticketpro.api.repository.EventoRepository;
import com.ticketpro.api.repository.SalaRepository;
import com.ticketpro.api.repository.SesionRepository;
import com.ticketpro.api.repository.UbicacionRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class TicketmasterImportService {

    @Value("${ticketmaster.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    private final EventoRepository eventoRepository;
    private final UbicacionRepository ubicacionRepository;
    private final SalaRepository salaRepository;
    private final SesionRepository sesionRepository;

    public TicketmasterImportService(
            EventoRepository eventoRepository,
            UbicacionRepository ubicacionRepository,
            SalaRepository salaRepository,
            SesionRepository sesionRepository) {

        this.webClient = WebClient.builder()
                .baseUrl("https://app.ticketmaster.com/discovery/v2")
                .codecs(c -> c.defaultCodecs().maxInMemorySize(5 * 1024 * 1024)) // 5 MB
                .build();

        this.objectMapper = new ObjectMapper();

        this.eventoRepository = eventoRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.salaRepository = salaRepository;
        this.sesionRepository = sesionRepository;
    }

public void importarEventosEspana() {
    System.out.println(">>> Importando SOLO conciertos...");
    importarSoloConciertos();
    System.out.println(">>> Importación finalizada.");
}


   private void importarSoloConciertos() {

    // Segmento oficial de música
    String segmentId = "KZFzniwnSyZfZ7v7nJ";

    String url = "/events.json?countryCode=ES&size=200&segmentId=" 
                 + segmentId + "&apikey=" + apiKey;

    try {
        String responseStr = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("URL usada: " + url);

        JsonNode root = objectMapper.readTree(responseStr);
        JsonNode events = root.path("_embedded").path("events");

        if (!events.isArray()) {
            System.out.println("⚠ No hay conciertos en España");
            return;
        }

        for (JsonNode node : events) {
            procesarConcierto(node);
        }

    } catch (Exception e) {
        System.err.println("❌ Error importando conciertos: " + e.getMessage());
    }
}


private String obtenerCategoriaConcierto(JsonNode node) {

    JsonNode classif = node.path("classifications").path(0);

    String sub = classif.path("subGenre").path("name").asText("");
    String genre = classif.path("genre").path("name").asText("");
    String segment = classif.path("segment").path("name").asText("");

    if (!sub.isEmpty()) return sub;
    if (!genre.isEmpty()) return genre;
    if (!segment.isEmpty()) return segment;

    return "Conciertos";
}


    private void procesarConcierto(JsonNode node) {

    String tmId = node.path("id").asText();

    if (tmId.isEmpty() || eventoRepository.existsByTicketmasterId(tmId)) {
        return;
    }

    // UBICACIÓN
    JsonNode venueNode = node.path("_embedded").path("venues").path(0);
    String nombreVenue = venueNode.path("name").asText("Recinto Desconocido");
    String ciudad = venueNode.path("city").path("name").asText("Ciudad");

    Ubicacion ubi = ubicacionRepository.findByNombreAndCiudad(nombreVenue, ciudad)
            .orElseGet(() -> {
                Ubicacion u = new Ubicacion();
                u.setNombre(nombreVenue);
                u.setCiudad(ciudad);
                u.setProvincia(venueNode.path("state").path("name").asText(ciudad));
                u.setCp(venueNode.path("postalCode").asText("00000"));
                return ubicacionRepository.save(u);
            });

    // SALA
    Sala sala = salaRepository.findByNombreAndUbicacion("Sala Principal - " + nombreVenue, ubi)
            .orElseGet(() -> {
                Sala s = new Sala();
                s.setNombre("Sala Principal - " + nombreVenue);
                s.setCapacidad(500);
                s.setUbicacion(ubi);
                return salaRepository.save(s);
            });

    // EVENTO
    Evento evento = new Evento();
    evento.setTicketmasterId(tmId);
    evento.setTitulo(node.path("name").asText("Sin título"));

    // CATEGORÍA = subGenre / genre / segment
    String categoria = obtenerCategoriaConcierto(node);
    evento.setCategoria(categoria);

    // DESCRIPCIÓN
    String desc = node.path("info").asText("");
    if (desc.isEmpty()) desc = node.path("description").asText("");
    if (desc.isEmpty()) desc = "No te pierdas " + evento.getTitulo() + " en " + nombreVenue + ".";
    if (desc.length() > 1000) desc = desc.substring(0, 997) + "...";

    evento.setDescripcion(desc);

    // IMAGEN
    String img = node.path("images").path(0).path("url").asText("https://via.placeholder.com/500");
    evento.setImagenUrl(img);

    // FECHA
    String fechaStr = node.path("dates").path("start").path("localDate").asText();
    evento.setFecha(fechaStr.isEmpty() ? LocalDate.now() : LocalDate.parse(fechaStr));

    evento.setEstado("ACTIVO");

    evento = eventoRepository.save(evento);

    // SESIÓN
    Sesion sesion = new Sesion();
    sesion.setEvento(evento);
    sesion.setSala(sala);
    sesion.setPrecioBase(new BigDecimal("25.00"));
    sesion.setFechaHora(evento.getFecha().atTime(20, 0));
    sesion.setEstado(0);

    sesionRepository.save(sesion);

    System.out.println("✔ Importado concierto: " + evento.getTitulo() + " (" + categoria + ")");
}

}
