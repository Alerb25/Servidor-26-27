import java.util.*;

import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        System.out.println("=".repeat(60));

        System.out.println("DEMOSTRACIÓN DE CONCEPTOS DE POO EN JAVA");

        System.out.println("=".repeat(60));
        
          private static void demostrarArrays() {

        System.out.println("\n3. ARRAYS Y OPERACIONES");

        System.out.println("-".repeat(40));

        Puzzle[] arrayPuzzles = new Puzzle[5];

        arrayPuzzles[0] = new Puzzle("Puzzle 1", "Desc", "sol", 10);

        arrayPuzzles[1] = new Puzzle("Puzzle 2", "Desc", "sol", 20);

        arrayPuzzles[2] = new Puzzle("Puzzle 3", "Desc", "sol", 15);

        int[] puntuaciones = {10, 20, 15, 25, 30};

        System.out.println("Array de puntuaciones: " + Arrays.toString(puntuaciones));

        Arrays.sort(puntuaciones);

        System.out.println("Ordenado: " + Arrays.toString(puntuaciones));

        int suma = Arrays.stream(puntuaciones).sum();

        System.out.println("Suma total: " + suma);

        double promedio = Arrays.stream(puntuaciones).average().orElse(0);

        System.out.println("Promedio: " + promedio);

        int indice = Arrays.binarySearch(puntuaciones, 20);

        System.out.println("Índice de 20: " + indice);

        List<Integer> listaPuntos = Arrays.stream(puntuaciones)

                                          .boxed()

                                          .collect(Collectors.toList());

        System.out.println("Lista desde array: " + listaPuntos);

    }


         private static void demostrarOperacionesAvanzadas() {

        System.out.println("\n4. OPERACIONES AVANZADAS CON LISTAS");

        System.out.println("-".repeat(40));

        ScapeRoom escape = crearEscapeRoomCompleto();

        System.out.println("\nBuscando puzzle 'espejo'...");

        Puzzle encontrado = escape.buscarPuzzle("espejo");

        if (encontrado != null) {

            System.out.println("Encontrado: " + encontrado);

        }

        Optional<Puzzle> puzzleOpt = escape.buscarPuzzleStream("morse");

        puzzleOpt.ifPresent(p -> System.out.println("Con Stream: " + p));

        System.out.println("\nOrdenando puzzles por puntuación...");

        escape.ordenarPuzzlesPorPuntos();

        escape.listarPuzzles();

        System.out.println("\nPuzzles entre 15 y 25 puntos:");

        List<Puzzle> puzzlesFiltrados = escape.filtrarPuzzlesPorPuntos(15, 25);

        puzzlesFiltrados.forEach(System.out::println);

        System.out.println("\nTop 3 puzzles más valiosos:");

        List<Puzzle> topPuzzles = escape.obtenerTopPuzzles(3);

        topPuzzles.forEach(System.out::println);

        System.out.println("\nIntercambiando puzzles...");

        escape.intercambiarPuzzles(0, 2);

        System.out.println("\nBarajando puzzles...");

        escape.barajarPuzzles();

    }



    
        public void demostrarSimulacionJuego() {
        }

           private static void demostrarEstructurasDatos() {

        System.out.println("\n6. ESTRUCTURAS DE DATOS COMPLEJAS");

        System.out.println("-".repeat(40));

        ScapeRoom escape = crearEscapeRoomCompleto();

        Map<String, Object> stats = escape.obtenerEstadisticas();

        System.out.println("\nEstadísticas del Escape Room:");

        stats.forEach((clave, valor) ->

            System.out.println("   " + clave + ": " + valor)

        );

        List<Map<String, Object>> infoPuzzles = new ArrayList<>();

        for (Puzzle p : escape.getPuzzles()) {

            Map<String, Object> info = new HashMap<>();

            info.put("id", p.getId());

            info.put("nombre", p.getNombre());

            info.put("resuelto", p.isResuelto());

            info.put("puntos", p.getPuntos());

            infoPuzzles.add(info);

        }

        System.out.println("\nInformación de puzzles en Maps:");

        infoPuzzles.forEach(System.out::println);

        // TreeMap: mantiene las claves ordenadas automáticamente

        TreeMap<Integer, Puzzle> puzzlesPorId = new TreeMap<>();

        for (Puzzle p : escape.getPuzzles()) {

            puzzlesPorId.put(p.getId(), p);

        }

        System.out.println("\nPuzzles ordenados por ID (TreeMap):");

        puzzlesPorId.forEach((id, puzzle) ->

            System.out.println("   ID " + id + ": " + puzzle.getNombre())

        );

        // Set: no admite elementos duplicados

        Set<String> tematicas = new HashSet<>();

        tematicas.add("Terror");

        tematicas.add("Aventura");

        tematicas.add("Ciencia Ficción");

        tematicas.add("Terror"); // No se añade, ya existe

        System.out.println("\nTemáticas únicas (Set): " + tematicas);

    }


         private static void demostrarStreamAPI() {

        System.out.println("\n7. STREAM API Y PROGRAMACIÓN FUNCIONAL");

        System.out.println("-".repeat(40));

        ScapeRoom escape = crearEscapeRoomCompleto();

        escape.getPuzzles().get(0).intentarResolver("1234");

        escape.getPuzzles().get(2).intentarResolver("debajo");

        List<Puzzle> resueltos = escape.getPuzzles().stream()

            .filter(Puzzle::isResuelto)

            .collect(Collectors.toList());

        System.out.println("Puzzles resueltos:");

        resueltos.forEach(p -> System.out.println("   - " + p.getNombre()));

        List<Puzzle> pendientes = escape.getPuzzles().stream()

            .filter(p -> !p.isResuelto())

            .collect(Collectors.toList());

        System.out.println("\nPuzzles pendientes:");

        pendientes.forEach(p -> System.out.println("   - " + p.getNombre()));

        List<String> nombres = escape.getPuzzles().stream()

            .map(Puzzle::getNombre)

            .collect(Collectors.toList());

        System.out.println("\nNombres de puzzles: " + nombres);

        // groupingBy: agrupa en un Map<Boolean, List<Puzzle>> según isResuelto()

        Map<Boolean, List<Puzzle>> porEstado = escape.getPuzzles().stream()

            .collect(Collectors.groupingBy(Puzzle::isResuelto));

        System.out.println("\nAgrupados por estado:");

        System.out.println("Resueltos: " + porEstado.get(true));

        System.out.println("Pendientes: " + porEstado.get(false));

        IntSummaryStatistics statsP = escape.getPuzzles().stream()

            .mapToInt(Puzzle::getPuntos)

            .summaryStatistics();

        System.out.println("\nEstadísticas de puntos:");

        System.out.println("   Total: " + statsP.getSum());

        System.out.println("   Promedio: " + statsP.getAverage());

        System.out.println("   Máximo: " + statsP.getMax());

        System.out.println("   Mínimo: " + statsP.getMin());

    }


 private static void demostrarManejoExcepciones() {

        System.out.println("\n8. MANEJO DE EXCEPCIONES");

        System.out.println("-".repeat(40));

        ScapeRoom escape = new ScapeRoom("Test", "Test");

        try {

            System.out.println("Intentando acceder a índice 10:");

            Puzzle p = escape.getPuzzles().get(10);

        } catch (IndexOutOfBoundsException e) {

            System.err.println("Error capturado: " + e.getMessage());

        }

        try {

            System.out.println("\nIntentando agregar puzzle nulo:");

            escape.agregarPuzzle(null);

        } catch (NullPointerException e) {

            System.err.println("Error: " + e.getMessage());

        }

        System.out.println("\nProgreso con lista vacía: " +

                         escape.calcularProgreso() + "%");

        System.out.println("\nEjemplo de try-with-resources:");

        try (Scanner scanner = new Scanner(System.in)) {

            // El scanner se cierra automáticamente al salir del bloque try

            System.out.println("Scanner creado y será cerrado automáticamente");

        }

    }

 

          private static void demostrarCreacionObjetos() {
            
        System.out.println("\n1. CREACIÓN DE OBJETOS");

        System.out.println("-".repeat(40));

        ScapeRoom escape = new ScapeRoom(

            "El Misterio de la Mansión",

            "Terror",

            4,

            45

        );

        System.out.println("Creado: " + escape.getNombre());

        // Upcasting: una variable de tipo Juego puede apuntar a un ScapeRoom

        Juego juego = escape;

        System.out.println("Polimorfismo - Juego: " + juego.getNombre());

    }

       private static void demostrarListas() {

        System.out.println("\n2. TRABAJANDO CON LISTAS DE OBJETOS");

        System.out.println("-".repeat(40));

        ScapeRoom escape = new ScapeRoom("La Casa Embrujada", "Terror");

        Puzzle puzzle1 = new Puzzle(

            "La Caja Fuerte",

            "¿Cuál es el código de 4 dígitos?",

            "1234",

            15,

            "Es una secuencia muy simple"

        );

        Puzzle puzzle2 = new Puzzle(

            "El Acertijo del Espejo",

            "¿Qué tiene ojos pero no puede ver?",

            "aguja",

            20,

            "Se usa para coser"

        );

        Puzzle puzzle3 = new Puzzle(

            "La Llave Oculta",

            "¿Dónde está la llave? (debajo/encima/dentro)",

            "debajo",

            10

        );

        escape.agregarPuzzle(puzzle1);

        escape.agregarPuzzles(puzzle2, puzzle3);  // Varargs

        List<Puzzle> puzzlesExtra = new ArrayList<>();

        puzzlesExtra.add(new Puzzle("Código Morse", "Descifra: ... --- ...",

                                   "sos", 25, "Señal de auxilio"));

        puzzlesExtra.add(new Puzzle("Sudoku", "Completa el sudoku 3x3",

                                   "123", 30));

        for (Puzzle p : puzzlesExtra) {

            escape.agregarPuzzle(p);

        }

        escape.listarPuzzles();

    
        


  // 2.1 Segundo ScapeRoom con el constructor corto (nombre, temática)
ScapeRoom escape2 = new ScapeRoom("La Cripta", "Aventura");
System.out.println(escape2);

// 2.2 Puzzle con el constructor de 4 argumentos (sin pista)
Puzzle puzzle4 = new Puzzle("El Reloj Parado", "¿A qué hora se detuvo?", "12:00", 15);
escape.agregarPuzzle(puzzle4);

// 2.3 Puntuación máxima del array puntuaciones
int[] puntuaciones = {10, 20, 15, 25, 30};
int maxPuntuacion = Arrays.stream(puntuaciones).max().orElse(0);
System.out.println("Puntuación máxima: " + maxPuntuacion);

// 2.4 Búsqueda de "llave"
Puzzle puzzleLlave = escape.buscarPuzzle("llave");
if (puzzleLlave != null) {
    System.out.println("Encontrado: " + puzzleLlave);
} else {
    System.out.println("No se encontró ningún puzzle con 'llave' en el nombre");
}

// 2.6 HashMap<Integer, String>: id -> nombre
HashMap<Integer, String> idsYNombres = new HashMap<>();
for (Puzzle p : escape.getPuzzles()) {
    idsYNombres.put(p.getId(), p.getNombre());
}
System.out.println("Mapa id -> nombre: " + idsYNombres);

// 2.7 List<Integer> con los puntos de todos los puzzles
List<Integer> puntos = escape.getPuzzles().stream()
                              .map(Puzzle::getPuntos)
                              .collect(Collectors.toList());
System.out.println("Puntos de los puzzles: " + puntos);

    }
}
