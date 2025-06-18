import java.util.*;

/** Simulador simple de base de datos en consola */
public class SimuladorBaseDeDatos {

    // ----- Recursos globales -----
    static Scanner entrada = new Scanner(System.in);
    static Map<String, Tabla> tablas = new LinkedHashMap<>();

    /*------------------------------------------------------------
      TABLA DE ARRANQUE: "alumnos" con 10 registros de ejemplo
    ------------------------------------------------------------*/
    static {
        Tabla t = new Tabla("alumnos");

        t.agregarAtributo("boleta", "int", true); // PK
        t.agregarAtributo("nombre", "char", false);
        t.agregarAtributo("edad", "int", false);
        t.agregarAtributo("salon", "char", false);
        t.agregarAtributo("esBecado", "char", false); // 'S' o 'N'

        String[][] datos = {
                { "20230001", "Ana Ruiz", "19", "A1", "S" },
                { "20230002", "Luis García", "20", "A1", "N" },
                { "20230010", "Tomás Vargas", "22", "E2", "N" }
        };
        for (String[] fila : datos) {
            Map<String, String> reg = new LinkedHashMap<>();
            reg.put("boleta", fila[0]);
            reg.put("nombre", fila[1]);
            reg.put("edad", fila[2]);
            reg.put("salon", fila[3]);
            reg.put("esBecado", fila[4]);
            t.registros.add(reg);
        }
        tablas.put(t.nombreTabla, t);
        /* --------- cursos (nueva) --------- */
        Tabla c = new Tabla("cursos");
        c.agregarAtributo("idCurso", "int", true); // PK
        c.agregarAtributo("nombre", "char", false);
        c.agregarAtributo("creditos", "int", false);

        String[][] datosCursos = {
                { "101", "Matemáticas I", "6" },
                { "102", "Programación", "8" },
                { "104", "Física", "7" }
        };
        for (String[] fila : datosCursos) {
            Map<String, String> reg = new LinkedHashMap<>();
            reg.put("idCurso", fila[0]);
            reg.put("nombre", fila[1]);
            reg.put("creditos", fila[2]);
            c.registros.add(reg);
        }
        tablas.put(c.nombreTabla, c); // <<--- segunda tabla
              /* --------- salones --------- */
        Tabla s = new Tabla("salones");
        s.agregarAtributo("salon",     "char", true);   // PK
        s.agregarAtributo("edificio",  "char", false);
        s.agregarAtributo("capacidad", "int",  false);

        String[][] datosSalones = {
            {"A1", "Edif. A", "35"},
            {"D1", "Edif. D", "25"},
            {"E2", "Edif. E", "32"}
        };
        for (String[] f : datosSalones) {
            Map<String,String> r = new LinkedHashMap<>();
            r.put("salon",     f[0]);
            r.put("edificio",  f[1]);
            r.put("capacidad", f[2]);
            s.registros.add(r);
        }
        tablas.put(s.nombreTabla, s);
    }

    // ----- Clases internas -----
    static class Atributo {
        String nombreAtributo;
        String tipoDato;
        boolean esLlavePrimaria;

        Atributo(String n, String t, boolean pk) {
            nombreAtributo = n;
            tipoDato = t;
            esLlavePrimaria = pk;
        }

        public String toString() {
            return nombreAtributo + " (" + tipoDato + ")" + (esLlavePrimaria ? " (PK)" : "");
        }
    }

    static class Tabla {
        String nombreTabla;
        List<Atributo> listaAtributos = new ArrayList<>();
        List<Map<String, String>> registros = new ArrayList<>();

        Tabla(String n) {
            nombreTabla = n;
        }

        void agregarAtributo(String n, String t, boolean pk) {
            listaAtributos.add(new Atributo(n, t, pk));
        }

        void insertarRegistro() {
            Map<String, String> nuevo = new LinkedHashMap<>();
            System.out.println("Insertando en la tabla " + nombreTabla);
            for (Atributo at : listaAtributos) {
                String val;
                while (true) {
                    System.out.print("Ingresa " + at.nombreAtributo + " (" + at.tipoDato + "): ");
                    val = entrada.nextLine().trim();
                    if (at.tipoDato.equals("int")) {
                        try {
                            Integer.parseInt(val);
                            break;
                        } catch (NumberFormatException e) {
                            System.err.println("Debe ser entero.");
                        }
                    } else if (at.tipoDato.equals("float")) {
                        try {
                            Float.parseFloat(val);
                            break;
                        } catch (NumberFormatException e) {
                            System.err.println("Debe ser decimal.");
                        }
                    } else
                        break; // char
                }
                nuevo.put(at.nombreAtributo, val);
            }
            registros.add(nuevo);
            System.out.println("Registro agregado.\n");
        }

        void mostrarRegistros() {
            System.out.println("Registros de " + nombreTabla);
            if (registros.isEmpty()) {
                System.out.println("(sin registros)");
                return;
            }
            registros.forEach(System.out::println);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("Tabla: " + nombreTabla + "\nAtributos:\n");
            listaAtributos.forEach(a -> sb.append("- ").append(a).append("\n"));
            return sb.toString();
        }
    }
    // ----- Fin clases internas -----

    // ================= Método principal =================
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n========== MENÚ PRINCIPAL ==========");
            System.out.println("1. Crear nueva tabla");
            System.out.println("2. Insertar un registro");
            System.out.println("3. Ver registros");
            System.out.println("4. Ver estructura de la tabla");
            System.out.println("5. Proyección de columnas");
            System.out.println("6. Selección de filas");
            System.out.println("7. Producto cartesiano");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            String op = entrada.nextLine().trim();

            switch (op) {
                case "1":
                    crearTabla();
                    break;
                case "2":
                    operarTabla(Tabla::insertarRegistro);
                    break;
                case "3":
                    operarTabla(Tabla::mostrarRegistros);
                    break;
                case "4":
                    operarTabla(t -> System.out.println(t));
                    break;
                case "5":
                    operarTabla(SimuladorBaseDeDatos::proyectarColumnas);
                    break;
                case "6":
                    operarTabla(SimuladorBaseDeDatos::seleccionarFilas);
                    break;
                case "7":
                    productoCartesiano();
                    break;
                case "0":
                    System.out.println("¡Hasta luego!");
                    return;
                default:
                    System.err.println("Opción no válida.");
            }
        }
    }

    // ---------- Creación de nueva tabla ----------
    private static void crearTabla() {
        System.out.print("Nombre de la nueva tabla: ");
        String nombre = entrada.nextLine().trim();
        if (tablas.containsKey(nombre)) {
            System.err.println("Ya existe esa tabla.");
            return;
        }

        Tabla nueva = new Tabla(nombre);
        System.out.print("¿Cuántos atributos tendrá? ");
        int nAttr;
        try {
            nAttr = Integer.parseInt(entrada.nextLine().trim());
        } catch (NumberFormatException e) {
            System.err.println("Número inválido.");
            return;
        }

        List<String> nombresTemp = new ArrayList<>();
        for (int i = 0; i < nAttr; i++) {
            System.out.print("Nombre del atributo #" + (i + 1) + ": ");
            nombresTemp.add(entrada.nextLine().trim());
        }
        for (String nom : nombresTemp) {
            String tipo;
            while (true) {
                System.out.print("Tipo de dato para '" + nom + "' (int, char, float): ");
                tipo = entrada.nextLine().trim().toLowerCase();
                if (tipo.equals("int") || tipo.equals("char") || tipo.equals("float"))
                    break;
                System.err.println("Tipo inválido.");
            }
            nueva.agregarAtributo(nom, tipo, false);
        }
        while (true) {
            System.out.println("Atributos disponibles para PK:");
            nueva.listaAtributos.forEach(a -> System.out.println("- " + a.nombreAtributo));
            System.out.print("Elige PK: ");
            String pk = entrada.nextLine().trim();
            boolean marcado = false;
            for (Atributo a : nueva.listaAtributos)
                if (a.nombreAtributo.equals(pk)) {
                    a.esLlavePrimaria = true;
                    marcado = true;
                }
            if (marcado)
                break;
            System.err.println("Atributo no encontrado.");
        }
        tablas.put(nombre, nueva);
        System.out.println("Tabla '" + nombre + "' creada.");
    }

    // ---------- Helper para operar sobre una tabla existente ----------
    private static void operarTabla(java.util.function.Consumer<Tabla> accion) {
        if (tablas.isEmpty()) {
            System.err.println("No hay tablas creadas.");
            return;
        }
        Tabla t = seleccionarTabla();
        if (t != null)
            accion.accept(t);
    }

    private static Tabla seleccionarTabla() {
        System.out.println("Tablas disponibles:");
        tablas.keySet().forEach(n -> System.out.println(" • " + n));
        System.out.print("Elige tabla: ");
        String nombre = entrada.nextLine().trim();
        Tabla t = tablas.get(nombre);
        if (t == null)
            System.err.println("Tabla inexistente.");
        return t;
    }

    // ========== Proyección ==========
    private static void proyectarColumnas(Tabla tabla) {
        System.out.println("Atributos de '" + tabla.nombreTabla + "':");
        tabla.listaAtributos.forEach(a -> System.out.println(" • " + a.nombreAtributo));

        System.out.print("Columnas a proyectar (coma): ");
        String[] cols = entrada.nextLine().split(",");
        List<String> pedidas = new ArrayList<>();
        for (String c : cols)
            pedidas.add(c.trim());

        List<String> validas = new ArrayList<>();
        for (String c : pedidas) {
            boolean existe = tabla.listaAtributos.stream().anyMatch(a -> a.nombreAtributo.equals(c));
            if (existe)
                validas.add(c);
            else
                System.err.println("No existe: " + c);
        }
        if (validas.isEmpty())
            return;

        System.out.println("== Proyección ==");
        validas.forEach(c -> System.out.print(c + "\t"));
        System.out.println();
        if (tabla.registros.isEmpty()) {
            System.out.println("(sin registros)");
            return;
        }
        for (Map<String, String> reg : tabla.registros) {
            for (String c : validas)
                System.out.print(reg.get(c) + "\t");
            System.out.println();
        }
    }

    // ========== Selección ==========
    private static void seleccionarFilas(Tabla tabla) {
        System.out.println("Atributos de '" + tabla.nombreTabla + "':");
        tabla.listaAtributos.forEach(a -> System.out.println(" • " + a.nombreAtributo + " (" + a.tipoDato + ")"));

        System.out.print("Atributo a filtrar: ");
        String attr = entrada.nextLine().trim();
        Atributo atributo = tabla.listaAtributos.stream()
                .filter(a -> a.nombreAtributo.equals(attr)).findFirst().orElse(null);
        if (atributo == null) {
            System.err.println("Atributo no existe.");
            return;
        }

        /* ---------- Búsqueda para tipo char ---------- */
        if (atributo.tipoDato.equals("char")) {
            System.out.print("Texto a buscar (contiene, sin distinguir mayúsculas/minúsculas): ");
            String buscado = entrada.nextLine().trim().toLowerCase();

            List<Map<String, String>> resultado = new ArrayList<>();
            for (Map<String, String> reg : tabla.registros) {
                String celda = reg.get(attr).toLowerCase();
                if (celda.contains(buscado))
                    resultado.add(reg);
            }

            System.out.println("== Filas que contienen '" + buscado + "' ==");
            if (resultado.isEmpty())
                System.out.println("(ninguna)");
            else
                resultado.forEach(System.out::println);
            return;
        }
        /* --------------------------------------------- */

        // ---------- Búsqueda numérica (int / float) ----------
        System.out.print("Operador (=, !=, <, >, <=, >=): ");
        String op = entrada.nextLine().trim();
        List<String> opValid = Arrays.asList("=", "!=", "<", ">", "<=", ">=");
        if (!opValid.contains(op)) {
            System.err.println("Operador inválido.");
            return;
        }

        System.out.print("Valor de comparación: ");
        String valor = entrada.nextLine().trim();

        List<Map<String, String>> resultado = new ArrayList<>();
        for (Map<String, String> reg : tabla.registros) {
            String celda = reg.get(attr);
            boolean cumple = false;
            try {
                if (atributo.tipoDato.equals("int")) {
                    int ci = Integer.parseInt(celda);
                    int vi = Integer.parseInt(valor);
                    cumple = compara(ci, vi, op);
                } else if (atributo.tipoDato.equals("float")) {
                    float cf = Float.parseFloat(celda);
                    float vf = Float.parseFloat(valor);
                    cumple = compara(cf, vf, op);
                }
            } catch (NumberFormatException e) {
                System.err.println("Formato numérico inválido.");
                return;
            }
            if (cumple)
                resultado.add(reg);
        }

        System.out.println("== Filas que cumplen la condición ==");
        if (resultado.isEmpty())
            System.out.println("(ninguna)");
        else
            resultado.forEach(System.out::println);
    }

    private static boolean compara(int a, int b, String op) {
        switch (op) {
            case "=":
                return a == b;
            case "!=":
                return a != b;
            case "<":
                return a < b;
            case ">":
                return a > b;
            case "<=":
                return a <= b;
            case ">=":
                return a >= b;
        }
        return false;
    }

    private static boolean compara(float a, float b, String op) {
        switch (op) {
            case "=":
                return a == b;
            case "!=":
                return a != b;
            case "<":
                return a < b;
            case ">":
                return a > b;
            case "<=":
                return a <= b;
            case ">=":
                return a >= b;
        }
        return false;
    }

    // ---------- Producto cartesiano ----------
    private static void productoCartesiano() {
        if (tablas.size() < 2) {
            System.err.println("Se necesitan al menos dos tablas.");
            return;
        }
        System.out.println("== Selecciona primera tabla ==");
        Tabla t1 = seleccionarTabla();
        if (t1 == null)
            return;

        System.out.println("== Selecciona segunda tabla ==");
        Tabla t2 = seleccionarTabla();
        if (t2 == null)
            return;

        if (t1.registros.isEmpty() || t2.registros.isEmpty()) {
            System.out.println("(una de las tablas no tiene registros)");
            return;
        }

        // Encabezados con prefijo tabla.
        List<String> headers = new ArrayList<>();
        t1.listaAtributos.forEach(a -> headers.add(t1.nombreTabla + "." + a.nombreAtributo));
        t2.listaAtributos.forEach(a -> headers.add(t2.nombreTabla + "." + a.nombreAtributo));

        System.out.println("== Producto cartesiano ==");
        headers.forEach(h -> System.out.print(h + "\t"));
        System.out.println();

        for (Map<String, String> r1 : t1.registros) {
            for (Map<String, String> r2 : t2.registros) {
                // Imprimir fila combinada
                for (Atributo a : t1.listaAtributos)
                    System.out.print(r1.get(a.nombreAtributo) + "\t");
                for (Atributo a : t2.listaAtributos)
                    System.out.print(r2.get(a.nombreAtributo) + "\t");
                System.out.println();
            }
        }
    }

}
