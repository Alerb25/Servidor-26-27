package com.daw.crudapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.crudapi.model.LogAccion;

public interface LogAccionRepository extends JpaRepository<LogAccion, Long> {

    Page<LogAccion> findAllByOrderByFechaHoraDesc(Pageable pageable);
}
