package com.daw.crudapi;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.daw.crudapi.model.Cita;
import com.daw.crudapi.model.Paciente;
import com.daw.crudapi.repository.CitaRepository;
import com.daw.crudapi.repository.PacienteRepository;

import lombok.RequiredArgsConstructor;

/** Datos de ejemplo: solo se cargan si la base de datos está vacía. */
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final PacienteRepository pacientes;
    private final CitaRepository citas;

    @Override
    public void run(String... args) {
        if (pacientes.count() > 0) {
            return;
        }
        Paciente ana = paciente("Ana López García", LocalDate.of(1985, 3, 12), "C/ Mayor 5, Fuengirola", "A+");
        Paciente luis = paciente("Luis Martín Ruiz", LocalDate.of(1972, 11, 2), "Av. Mijas 22, Fuengirola", "0+");
        Paciente marta = paciente("Marta Sánchez Gil", LocalDate.of(1999, 7, 25), "C/ Larga 8, Málaga", "B-");
        Paciente pedro = paciente("Pedro Díaz Mora", LocalDate.of(1960, 1, 30), "C/ Real 14, Mijas", "A+");
        Paciente lucia = paciente("Lucía Romero Vega", LocalDate.of(2005, 9, 9), "Paseo Marítimo 3, Benalmádena", "AB+");

        cita("Revisión anual", "Completada", 30, 60.0, LocalDate.of(2026, 9, 10), ana);
        cita("Dolor de espalda", "Pendiente", 45, 85.5, LocalDate.of(2026, 11, 15), ana);
        cita("Análisis de sangre", "Confirmada", 20, 40.0, LocalDate.of(2026, 11, 3), luis);
        cita("Control de tensión", "Cancelada", 15, 25.0, LocalDate.of(2026, 8, 1), luis);
        cita("Vacunación", "Completada", 10, 30.0, LocalDate.of(2026, 10, 1), marta);
        cita("Consulta dermatología", "Pendiente", 40, 120.0, LocalDate.of(2026, 12, 2), pedro);
        cita("Revisión cardiología", "Cancelada", 50, 150.0, LocalDate.of(2026, 7, 14), pedro);
        cita("Cita sin asignar", "Pendiente", 30, 55.0, LocalDate.of(2026, 12, 20), null);
        cita("Cita cancelada sin asignar", "Cancelada", 30, 55.0, LocalDate.of(2026, 6, 20), null);
        cita("Revisión ortodoncia", "Confirmada", 35, 95.0, LocalDate.of(2026, 11, 28), lucia);
    }

    private Paciente paciente(String nombre, LocalDate nac, String dir, String grupo) {
        Paciente p = new Paciente();
        p.setNombreCompleto(nombre);
        p.setFechaNacimiento(nac);
        p.setDireccion(dir);
        p.setGrupoSanguineo(grupo);
        return pacientes.save(p);
    }

    private void cita(String motivo, String estado, int min, double coste, LocalDate fecha, Paciente p) {
        Cita c = new Cita();
        c.setMotivoCita(motivo);
        c.setEstadoCita(estado);
        c.setObservaciones("");
        c.setDuracionMinutos(min);
        c.setCostoEstimado(coste);
        c.setFecha(fecha);
        c.setPaciente(p);
        citas.save(c);
    }
}
