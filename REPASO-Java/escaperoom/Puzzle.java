public class Puzzle {

    // Contador estático para asignar IDs únicos

    private static int contadorId = 0;

    // Atributos de instancia

    private final int id;  // final porque no cambia después de crearse

    private String nombre;

    private String descripcion;

    private String solucion;

    private int puntos;

    private String pista;

    private boolean resuelto;

    private int intentos;

    public Puzzle(String nombre, String descripcion, String solucion,

                  int puntos, String pista) {

        this.id = ++contadorId;

        this.nombre = nombre;

        this.descripcion = descripcion;

        this.solucion = solucion.toLowerCase().trim(); // Normalizamos la solución

        this.puntos = puntos;

        this.pista = pista;

        this.resuelto = false;

        this.intentos = 0;

    }

    // Constructor sobrecargado sin pista

    public Puzzle(String nombre, String descripcion, String solucion, int puntos) {

        this(nombre, descripcion, solucion, puntos, "");

    }

    // ====== GETTERS ======

    public int getId() {

        return id;

    }

    public String getNombre() {

        return nombre;

    }

    public String getDescripcion() {

        return descripcion;

    }

    public int getPuntos() {

        return puntos;

    }

    public boolean isResuelto() {

        return resuelto;

    }

    public int getIntentos() {

        return intentos;

    }

    public String getPista() {

        return pista;

    }

    // ====== SETTERS ======

    public void setNombre(String nombre) {

        this.nombre = nombre;

    }

    public void setDescripcion(String descripcion) {

        this.descripcion = descripcion;

    }

    // ====== MÉTODOS DE LÓGICA DE NEGOCIO ======

    public boolean intentarResolver(String respuesta) {

        this.intentos++;

        String respuestaNormalizada = respuesta.toLowerCase().trim();

        if (respuestaNormalizada.equals(this.solucion)) {

            this.resuelto = true;

            System.out.println("Correcto. Puzzle '" + nombre + "' resuelto");

            System.out.println("   Ganaste " + puntos + " puntos en " +

                             intentos + " intentos");

            return true;

        } else {

            System.out.println("Respuesta incorrecta. Intento #" + intentos);

            // Mostrar pista después de 3 intentos

            if (intentos >= 3 && !pista.isEmpty()) {

                System.out.println("Pista: " + pista);

            }

            return false;

        }

    }

    public void reiniciar() {

        this.resuelto = false;

        this.intentos = 0;

    }

    public Puzzle clonar() {

        return new Puzzle(nombre, descripcion, solucion, puntos, pista);

    }

    // 1.2 GET TOTAL
    public static int getTotalPuzzlesCreados(){
        
        return contadorId;
    }

    @Override

    public String toString() {

        String estado = resuelto ? "Resuelto" : "Pendiente";

        return String.format("Puzzle #%d: %s [%s] - %d pts",

                           id, nombre, estado, puntos);

    }

    @Override

    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        Puzzle puzzle = (Puzzle) obj;

        return id == puzzle.id;

    }

}
