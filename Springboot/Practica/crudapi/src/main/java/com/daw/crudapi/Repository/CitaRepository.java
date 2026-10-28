package com.daw.crudapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.daw.crudapi.model.Cita;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    // ---- 2 borrados por condición ----
    @Transactional
    long deleteByEstadoCitaAndFechaBefore(String estadoCita, LocalDate fecha);

    @Transactional
    long deleteByPacienteIsNullAndEstadoCita(String estadoCita);

    // ---- 1 búsqueda con filtros ----
    List<Cita> findByEstadoCitaContainingIgnoreCaseAndFechaBetweenAndCostoEstimadoGreaterThanEqualOrderByFechaDesc(
            String estado, LocalDate desde, LocalDate hasta, Double minCosto);

    // ---- 1 @Query personalizada (con parámetro) ----
    @Query("select c from Cita c where c.estadoCita = :estado "
            + "and c.costoEstimado > (select avg(c2.costoEstimado) from Cita c2) "
            + "order by c.costoEstimado desc")
    List<Cita> findMasCarasQueLaMedia(@Param("estado") String estado);

    // ---- Auxiliares ----
    List<Cita> findByPaciente_IdPacienteOrderByFechaDesc(Long idPaciente);

    long countByPacienteIsNull();

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Cita c set c.paciente = null where c.paciente.idPaciente = :idPaciente")
    int desasociarDePaciente(@Param("idPaciente") Long idPaciente);

    // ---- Estadísticas ----
    @Query("select c.estadoCita, count(c) from Cita c group by c.estadoCita order by count(c) desc")
    List<Object[]> contarPorEstado();

    @Query("select coalesce(avg(c.costoEstimado), 0.0) from Cita c")
    Double costeMedio();

    @Query("select coalesce(sum(c.costoEstimado), 0.0) from Cita c")
    Double costeTotal();

    @Query("select coalesce(avg(c.duracionMinutos), 0.0) from Cita c")
    Double duracionMedia();
}
