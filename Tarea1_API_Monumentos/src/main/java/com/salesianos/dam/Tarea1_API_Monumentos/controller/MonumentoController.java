package com.salesianos.dam.Tarea1_API_Monumentos;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/monument")
public class MonumentoController {

    private final MonumentoRepository monumentoRepository;

    @GetMapping
    public ResponseEntity<List<Monumento>> getAllMonumentos() {
        List<Monumento> resultado = monumentoRepository.findAll();
        if (resultado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Monumento> getMonumentoById(@PathVariable Long id) {
        return ResponseEntity.of(monumentoRepository.findById(id));
    }

    @PostMapping
    public ResponseEntity<Monumento> addMonumento (@RequestBody Monumento monumento) {
      if (StringUtils.hasText(monumento.getNombre())) {
          return ResponseEntity.status(201)
                  .body(monumentoRepository.save(monumento));
      }
      return ResponseEntity.badRequest().build();
    }

   @PutMapping("/{id}")
    public ResponseEntity<Monumento> updateMonumento(
            @PathVariable Long id,
            @RequestBody Monumento monumento) {
        if (!StringUtils.hasText(monumento.getNombre())) {
            return ResponseEntity.badRequest().build();
        }
      return monumentoRepository.findById(id)
              .map(m -> {
                  m.setCodigoPais(monumento.getCodigoPais());
                  m.setNombrePais(monumento.getNombrePais());
                  m.setCiudad(monumento.getCiudad());
                  m.setLatitud(monumento.getLatitud());
                  m.setLongitud(monumento.getLongitud());
                  m.setNombre(monumento.getNombre());
                  m.setDescripcion(monumento.getDescripcion());
                  m.setUrlFoto(monumento.getUrlFoto());
                  return ResponseEntity.ok(monumentoRepository.save(m));
              })
              .orElse(ResponseEntity.notFound().build());
   }

   @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonumento(@PathVariable Long id) {
        monumentoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
   }
}
