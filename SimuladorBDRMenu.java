import java.util.*;

/**
 * SimuladorBDR (Etapa 2)
 * -----------------------------------------------
 * Ya implementa la **opción 1** del menú:
 *   • Crear una nueva relación (solo esquema, sin datos aún).
 *   • Valida que el nombre no exista y que haya exactamente 1 PK.
 *   • Almacena la definición en memoria para usarla más adelante.
 *
 * Opciones 2 y 3 siguen como TODO.
 */
public class SimuladorBDRMenu {

    /* ===== Tipos auxiliares ===== */
    enum TipoDato { INT, CHAR, FLOAT }

    static class Atributo {
        final String nombre;
        final TipoDato tipo;
        final boolean pk;
        Atributo(String n, TipoDato t, boolean pk) { this.nombre = n; this.tipo = t; this.pk = pk; }
        @Override public String toString() { return nombre + ":" + tipo + (pk ? " (PK)" : ""); }
    }

    static class Relacion {
        final String nombre;
        final List<Atributo> attrs;
        Relacion(String n, List<Atributo> a) { this.nombre = n; this.attrs = a; }
        void mostrarEsquema() { attrs.forEach(System.out::println); }
    }

    /* ===== Campos de la clase ===== */
    private final Scanner sc = new Scanner(System.in);
    private final Map<String, Relacion> relaciones = new LinkedHashMap<>();

    public static void main(String[] args) { new SimuladorBDRMenu().run(); }

    /* =================== BUCLE PRINCIPAL =================== */
    private void run() {
        int opcion;
        do {
            menu();
            opcion = leerInt("Opción: ");
            switch (opcion) {
                case 1 -> crearRelacion();
                case 2 -> System.out.println("[TODO] Insertar registros – próxima etapa.");
                case 3 -> mostrarRelacion();
                case 0 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción inválida, intenta de nuevo.");
            }
        } while (opcion != 0);
    }

    /* =================== MENÚ Y ACCIONES =================== */
    private void menu() {
        System.out.println("\n--- SIMULADOR BDR (etapa 2) ---");
        System.out.println("1. Crear nueva relación (esquema)");
        System.out.println("2. Insertar registro      [pendiente]");
        System.out.println("3. Mostrar relación       (esquema)");
        System.out.println("0. Salir");
    }

    /** Opción 1: definir un esquema de relación. */
    private void crearRelacion() {
        String nombre = leer("Nombre de la relación: ");
        if (relaciones.containsKey(nombre)) {
            System.out.println("Ya existe una relación con ese nombre.");
            return;
        }
        int n = leerInt("¿Cuántos atributos?: ");
        if (n <= 0) { System.out.println("Debe tener al menos 1 atributo."); return; }
        List<Atributo> lista = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String nAttr = leer("  Nombre del atributo " + (i + 1) + ": ");
            TipoDato tipo;
            while (true) {
                try {
                    tipo = TipoDato.valueOf(leer("  Tipo (INT/CHAR/FLOAT): ").toUpperCase());
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("  Tipo inválido. Intenta de nuevo.");
                }
            }
            boolean pk = leer("  ¿Es llave primaria? (s/n): ").equalsIgnoreCase("s");
            lista.add(new Atributo(nAttr, tipo, pk));
        }
        long pks = lista.stream().filter(a -> a.pk).count();
        if (pks != 1) {
            System.out.println("Error: debe haber exactamente 1 atributo marcado como PK.");
            return;
        }
        relaciones.put(nombre, new Relacion(nombre, lista));
        System.out.println("Relación '" + nombre + "' creada con éxito.");
    }

    /** Opción 3: listar el esquema de una relación. */
    private void mostrarRelacion() {
        if (relaciones.isEmpty()) { System.out.println("Aún no existen relaciones."); return; }
        String nombre = leer("Nombre de la relación a mostrar: ");
        Relacion r = relaciones.get(nombre);
        if (r == null) {
            System.out.println("No se encontró esa relación.");
            return;
        }
        System.out.println("Esquema de '" + nombre + "':");
        r.mostrarEsquema();
    }

    /* =================== UTILIDADES =================== */
    private String leer(String msg) { System.out.print(msg); return sc.nextLine().trim(); }
    private int leerInt(String msg) {
        try { return Integer.parseInt(leer(msg)); }
        catch (NumberFormatException e) { return -1; }
    }
}
