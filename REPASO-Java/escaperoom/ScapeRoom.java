import java.util.*;

import java.util.stream.Collectors;

public class ScapeRoom extends Juego {

    private String tematica;

    private List<Puzzle> puzzles;

    private int tiempoRestante;

    private String nivelDificultad;

    public ScapeRoom(String nombre, String tematica, int maxJugadores,

                     int duracionMinutos) {

        super(nombre, maxJugadores, duracionMinutos);

        this.tematica = tematica;

        this.puzzles = new ArrayList<>();

        this.tiempoRestante = duracionMinutos;

        this.nivelDificultad = "Media";

    }

    // Constructor con valores por defecto

    public ScapeRoom(String nombre, String tematica) {

        this(nombre, tematica, 6, 60);

    }

    public String getTematica() {

        return tematica;

    }

    public void setTematica(String tematica) {

        this.tematica = tematica;

    }

    // Encapsulación defensiva: se devuelve una copia para que quien reciba

    // la lista no pueda modificar la lista interna del objeto

    public List<Puzzle> getPuzzles() {

        return new ArrayList<>(puzzles);

    }

    public int getNumPuzzles() {

        return puzzles.size();

    }

    //Funcion de cuenta con filtro de funcion booleana

    public long getPuzzlesResueltos() {

        return puzzles.stream()

                      .filter(Puzzle::isResuelto)

                      //equivalente .filter(p -> p.isResuelto())

                      .count();

    }

    public String getNivelDificultad() {

        return nivelDificultad;

    }

    public void setNivelDificultad(String nivelDificultad) {

        this.nivelDificultad = nivelDificultad;

    }

    // 1.3 Metodo GET PUZZLES PENDIENTES
    public void getPuzzlesPendientes(){
          return puzzles.stream().filter(p -> !isResuelto()).count();

    }

    public void agregarPuzzle(Puzzle puzzle) {

        if (puzzle != null) {

            puzzles.add(puzzle);

            System.out.println("Puzzle '" + puzzle.getNombre() +

                             "' agregado al escape room");

        } else {

            System.err.println("Error: No se puede agregar un puzzle nulo");

        }

    }

    // Varargs: permite pasar cero, uno o varios puzzles separados por comas

    public void agregarPuzzles(Puzzle... nuevosPuzzles) {

        for (Puzzle puzzle : nuevosPuzzles) {

            agregarPuzzle(puzzle);

        }

    }

    public boolean eliminarPuzzle(int puzzleId) {

        boolean eliminado = puzzles.removeIf(p -> p.getId() == puzzleId);

        if (eliminado) {

            System.out.println("Puzzle con ID " + puzzleId + " eliminado");

        } else {

            System.out.println("No se encontró puzzle con ID " + puzzleId);

        }

        return eliminado;

    }

    public Puzzle obtenerPuzzlePorIndice(int indice) {

        try {

            return puzzles.get(indice);

        } catch (IndexOutOfBoundsException e) {

            System.err.println("Error: No existe puzzle en la posición " + indice);

            return null;

        }

    }

    // Método auxiliar para mostrar todos los puzzles por consola

    public void listarPuzzles() {

        puzzles.forEach(System.out::println);

    }


    // 1.4 EXISTEPUZZLE()
    public void existePuzzle(int puzzleId){
        puzzles.stream().contains(puzzleId).anyMatch();
    }

   public Puzzle buscarPuzzle(String nombre) {

        for (Puzzle puzzle : puzzles) {

            if (puzzle.getNombre().toLowerCase().contains(nombre.toLowerCase())) {

                return puzzle;

            }

        }

        return null;

    }

    public Optional<Puzzle> buscarPuzzleStream(String nombre) {

        return puzzles.stream()

                      .filter(p -> p.getNombre().toLowerCase()

                                   .contains(nombre.toLowerCase()))

                      .findFirst();

    }

    //1.5 contar PUZZLES CON NOMBRE
    public long contarPuzzlesconNombre(String texto){
        return puzzles.stream().contains(texto.toLowerCase()).count();
    }

 public void ordenarPuzzlesPorPuntos() {

        puzzles.sort((p1, p2) -> Integer.compare(p2.getPuntos(), p1.getPuntos()));

        System.out.println("Puzzles ordenados por puntuación");

    }

    public List<Puzzle> filtrarPuzzlesPorPuntos(int minPuntos, int maxPuntos) {

        return puzzles.stream()

                      .filter(p -> p.getPuntos() >= minPuntos &&

                                  p.getPuntos() <= maxPuntos)

                      .collect(Collectors.toList());

    }

    public List<Puzzle> obtenerTopPuzzles(int n) {

        return puzzles.stream()

                      .sorted((p1, p2) -> Integer.compare(p2.getPuntos(),

                                                          p1.getPuntos()))

                      .limit(n)

                      .collect(Collectors.toList());

    }

    //1.6 Obtener Puzzle Menos Valioso
    public List<Puzzle> obtenerPuzzlesMenosValiosos(int n){
       return puzzles.stream()

                      .sorted((p2, p1) -> Integer.compare(p2.getPuntos(),

                                                          p1.getPuntos()))

                      .limit(n)

                      .collect(Collectors.toList());

}

@Override

    public void iniciarJuego() {

        super.iniciarJuego();  // Llamamos al método del padre

        this.tiempoRestante = getDuracionMinutos();

        puzzles.forEach(Puzzle::reiniciar);

        System.out.println("Escape Room '" + getNombre() + "' iniciado");

        System.out.println("Tienes " + tiempoRestante + " minutos para escapar");

        System.out.println("Resuelve " + getNumPuzzles() + " puzzles para completarlo");

    }

    public double calcularProgreso() {

        if (puzzles.isEmpty()) {

            return 0.0;

        }

        return (getPuzzlesResueltos() * 100.0) / getNumPuzzles();

    }

    public boolean estaCompletado() {

        return puzzles.stream().allMatch(Puzzle::isResuelto);

    }

// 1.7 Puzzles pendientes
public boolean hayPuzzlesPendientes() {
    return puzzles.stream().anyMatch(p -> !p.isResuelto());
}

public boolean hayNPuzzlesPendientes(int n) {
    long pendientes = puzzles.stream()
                              .filter(p -> !p.isResuelto())
                              .count();
    return pendientes == n;
}

// 1.8 Mover puzzle al principio
public void moverPuzzleAlPrincipio(int indice) {
    try {
        Collections.swap(puzzles, indice, 0);
        System.out.println("Puzzle movido a la posición inicial");
    } catch (IndexOutOfBoundsException e) {
        System.err.println("Error: no existe puzzle en la posición " + indice);
    }
}
}
