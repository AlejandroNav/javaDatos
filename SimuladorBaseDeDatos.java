import java.util.*;

// Clase principal del simulador
public class SimuladorBaseDeDatos {
    // Scanner global para entrada de usuario
    static Scanner entrada = new Scanner(System.in);

    // Clase interna para representar un atributo (columna) de la tabla
    static class Atributo {
        // definimos los atributos de la clase Atributo
        String nombreAtributo;
        String tipoDato;
        boolean esLlavePrimaria;

        // Constructor del atributo(asignamos los valores)
        Atributo(String nombreAtributo, String tipoDato, boolean esLlavePrimaria) {
            // this hace referencia a el objeto que creamos cuando metetmos los datos
            // clase es un molde o plantilla, un objeto seria Alumno o Profesor
            this.nombreAtributo = nombreAtributo;
            this.tipoDato = tipoDato;
            this.esLlavePrimaria = esLlavePrimaria;
        }

        // Método para representar el atributo como texto, mostrando si es PK
        public String toString() { // esto e ssolo para mostrar el atributo creado
            String pk = ""; // y si tiene llave primaria o nop
            if (esLlavePrimaria) {
                pk = " (Primary Key)";
            }
            return nombreAtributo + " (" + tipoDato + ")" + pk;
        }
    }

    // Clase interna para representar la tabla
    static class Tabla { 
        String nombreTabla; // Nombre de la tabla es  un string
        List<Atributo> listaAtributos = new ArrayList<>(); // Guardamos los atributos o columnas de la tabla
        List<Map<String, String>> registros = new ArrayList<>(); // Lista de registros (filas) de la tabla

        Tabla(String nombreTabla) { // Constructor de la tabla
            this.nombreTabla = nombreTabla;
        }

        // Método para agregar atributos(columnas) a la tabla
        public void agregarAtributo(String nombre, String tipo, boolean esPK) {
            listaAtributos.add(new Atributo(nombre, tipo, esPK));
        }

        // Método para insertar un registro (fila o alumno en el ejemplo) en la tabla
        public void insertarRegistro() {
            Map<String, String> nuevoRegistro = new HashMap<>();
            // la unica manera de 
            System.out.println("Insertando nuevo registro en la tabla: " + nombreTabla);

            for (Atributo atributo : listaAtributos) {
                String valor;

                // Ciclo para validar tipo de dato según el atributo
                while (true) {
                    System.out.print("Ingresa " + atributo.nombreAtributo + " (" + atributo.tipoDato + "): ");
                    valor = entrada.nextLine();

                    // Validación de tipo int
                    if (atributo.tipoDato.equals("int")) {
                        try {
                            Integer.parseInt(valor);
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println(" ERROR Valor inválido. Debe ser un número entero.");
                        }
                    }
                    // Validación de tipo float
                    else if (atributo.tipoDato.equals("float")) {
                        try {
                            Float.parseFloat(valor);
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println(" ERROR Valor inválido. Debe ser un número decimal.");
                        }
                    }
                    // Tipo char: se acepta cualquier entrada
                    else {
                        break;
                    }
                }

                nuevoRegistro.put(atributo.nombreAtributo, valor);
            }

            registros.add(nuevoRegistro);
            System.out.println("  Registro agregado correctamente.\n");
        }

        // Método para mostrar todos los registros guardados
        public void mostrarRegistros() {
            System.out.println(" Registros de la tabla: " + nombreTabla);
            if (registros.isEmpty()) {
                System.out.println("No hay registros aún.");
                return;
            }

            for (Map<String, String> registro : registros) {
                System.out.println(registro);
            }
        }

        // Método para mostrar estructura de la tabla
        public String toString() {
            StringBuilder texto = new StringBuilder();
            texto.append("  Tabla: ").append(nombreTabla).append("\nAtributos:\n");
            for (Atributo atributo : listaAtributos) {
                texto.append("- ").append(atributo).append("\n");
            }
            return texto.toString();
        }
    }

    // Método principal con el menú interactivo
    public static void main(String[] args) {
        Tabla tablaActual = null;

        while (true) {
            // Menú de opciones
            System.out.println("\n======+++ MENU PRINCIPAL +++======\n");
            System.out.println("1. Crear nueva tabla");
            System.out.println("2. Insertar un registro");
            System.out.println("3. Ver registros");
            System.out.println("4. Ver estructura de la tabla");
            System.out.println("0. Salir \n");
            System.out.print("Selecciona una opción: ");
            String opcion = entrada.nextLine();

            switch (opcion) {
                case "1":
                    // Crear tabla
                    System.out.print("🆕 Ingresa el nombre de la nueva tabla: ");
                    String nombreTabla = entrada.nextLine();
                    tablaActual = new Tabla(nombreTabla);

                    System.out.print("¿Cuántos atributos tendrá la tabla? ");
                    int cantidadAtributos = Integer.parseInt(entrada.nextLine());

                    List<String> nombresTemp = new ArrayList<>();

                    // Recolección de nombres de atributos
                    for (int i = 0; i < cantidadAtributos; i++) {
                        System.out.print("Nombre del atributo #" + (i + 1) + ": ");
                        nombresTemp.add(entrada.nextLine());
                    }

                    // Asignación de tipo a cada atributo
                    for (String nombre : nombresTemp) {
                        String tipoDato;
                        while (true) {
                            System.out.print("Tipo de dato para '" + nombre + "' (int, char, float): ");
                            tipoDato = entrada.nextLine().toLowerCase();
                            if (tipoDato.equals("int") || tipoDato.equals("char") || tipoDato.equals("float")) break;
                            System.out.println(" ERROR Tipo inválido.");
                        }
                        tablaActual.agregarAtributo(nombre, tipoDato, false);
                    }

                    // Selección obligatoria de llave primaria
                    while (true) {
                        System.out.println("\nSelecciona el nombre del atributo que será la llave primaria (PK):");
                        for (Atributo a : tablaActual.listaAtributos) {
                            System.out.println("- " + a.nombreAtributo);
                        }

                        System.out.print("Escribe el nombre exacto del atributo PK: ");
                        String elegido = entrada.nextLine();

                        boolean encontrado = false;
                        for (Atributo a : tablaActual.listaAtributos) {
                            if (a.nombreAtributo.equals(elegido)) {
                                a.esLlavePrimaria = true;
                                encontrado = true;
                                break;
                            }
                        }

                        if (encontrado) {
                            System.out.println(" # Atributo marcado como llave primaria.");
                            break;
                        } else {
                            System.out.println(" ERROR Atributo no encontrado. Intenta de nuevo.");
                        }
                    }

                    System.out.println(" # Tabla creada correctamente.");
                    break;

                case "2":
                    if (tablaActual == null) {
                        System.out.println(" ERROR Primero debes crear una tabla.");
                    } else {
                        tablaActual.insertarRegistro();
                    }
                    break;

                case "3":
                    if (tablaActual == null) {
                        System.out.println(" ERROR No hay ninguna tabla creada.");
                    } else {
                        tablaActual.mostrarRegistros();
                    }
                    break;

                case "4":
                    if (tablaActual == null) {
                        System.out.println(" ERROR No hay ninguna tabla creada.");
                    } else {
                        System.out.println(tablaActual);
                    }
                    break;

                case "0":
                    System.out.println("  Adiós. Gracias por usar el simulador de Amalia.");
                    return;

                default:
                    System.out.println(" ERROR Opción no válida.");
            }
        }
    }
}
