package com.daw.crudapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daw.crudapi.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

        //LA principal
    // ---- 2 búsquedas findBy... con varias condiciones ----
    List<Paciente> findByGrupoSanguineoAndNombreCompletoContainingIgnoreCase(String grupoSanguineo, String nombre);

    List<Paciente> findByFechaNacimientoBetweenAndDireccionContainingIgnoreCase(LocalDate desde, LocalDate hasta,
            String direccion);

    // ---- 1 con ordenación ----
    List<Paciente> findAllByOrderByNombreCompletoAsc();

    // ---- 1 de conteo ----
    long countByGrupoSanguineo(String grupoSanguineo);

    // ---- 1 con findTop... ----
    List<Paciente> findTop5ByOrderByFechaNacimientoAsc();

    // ---- Agregación: pacientes por grupo sanguíneo ----
    @Query("select p.grupoSanguineo, count(p) from Paciente p group by p.grupoSanguineo order by count(p) desc")
    List<Object[]> contarPacientesPorGrupo();

    // ---- @Query JPQL con parámetro: pacientes con al menos N citas ----
    @Query("select p from Paciente p where size(p.citas) >= :minCitas order by size(p.citas) desc")
    List<Paciente> findConAlMenosCitas(@Param("minCitas") int minCitas);
}
