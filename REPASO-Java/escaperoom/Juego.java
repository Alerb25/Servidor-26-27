
public class Juego {

    // Atributos privados (encapsulación)

    private String nombre;

    private int maxJugadores;

    private int duracionMinutos;

    protected int puntuacion;  // protected para que las clases hijas puedan acceder

    private boolean estaActivo;

    public Juego(String nombre, int maxJugadores, int duracionMinutos) {

        this.nombre = nombre;

        this.maxJugadores = maxJugadores;

        this.duracionMinutos = duracionMinutos;

        this.puntuacion = 0;

        this.estaActivo = false;

    }

    // Constructor sobrecargado con valores por defecto

    public Juego(String nombre) {

        this(nombre, 1, 60); // Llama al constructor principal

    }

    // ====== GETTERS ======

    public String getNombre() {

        return nombre;

    }

    public int getMaxJugadores() {

        return maxJugadores;

    }

    public int getDuracionMinutos() {

        return duracionMinutos;

    }

    public int getPuntuacion() {

        return puntuacion;

    }

    public boolean isEstaActivo() {

        return estaActivo;

    }

    // ====== SETTERS ======

    public void setPuntuacion(int puntuacion) {

        if (puntuacion >= 0) {

            this.puntuacion = puntuacion;

        } else {

            System.err.println("Error: La puntuación no puede ser negativa");

        }

    }

    public void setMaxJugadores(int maxJugadores) {

        if (maxJugadores > 0) {

            this.maxJugadores = maxJugadores;

        } else {

            System.err.println("Error: Debe haber al menos 1 jugador");

        }

    }

    // ====== MÉTODOS DE LA CLASE ======

    public void iniciarJuego() {

        this.estaActivo = true;

        this.puntuacion = 0;

        System.out.println("Juego '" + nombre + "' iniciado.");

    }

    public void finalizarJuego() {

        this.estaActivo = false;

        System.out.println("Juego finalizado. Puntuación final: " + puntuacion);

    }

    public void agregarPuntos(int puntos) {

        if (puntos > 0) {

            this.puntuacion += puntos;

            System.out.println("+" + puntos + " puntos. Total: " + this.puntuacion);

        } else {

            System.out.println("Los puntos deben ser positivos");

        }

    }

    //1.1 RESTAR PUNTOS

    public void restarPuntos(int puntos){
        if (puntos > 0){
            
            int resultado = this.puntuacion - puntos;
            if (resultado >= 0 ){
                this.puntuacion = resultado;
            } else {
                this.puntuacion = 0;
            }
           
            System.out.println("-" + puntos + " puntos. Total: " + this.puntuacion);
        }
    }

    @Override

    public String toString() {

        String estado = estaActivo ? "Activo" : "Inactivo";

        return String.format("Juego: %s | Jugadores: %d | Estado: %s",

                           nombre, maxJugadores, estado);

    }

}
