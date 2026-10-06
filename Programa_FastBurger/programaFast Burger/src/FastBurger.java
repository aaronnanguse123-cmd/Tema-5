import java.util.Scanner;

public class FastBurger {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("        FAST BURGER - SISTEMA DE VENTA");
        System.out.println("======================================");

        boolean continuar = true;

        while (continuar) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Realizar pedido");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    procesarPedido();
                    break;
                case 2:
                    continuar = false;
                    System.out.println("\nGracias por utilizar Fast Burger.");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }

        sc.close();
    }

    public static void procesarPedido() {
        System.out.println("\n--- SOLICITAR MENÚ ---");
        mostrarMenu();

        String pedido = tomarPedido();

        boolean confirmado = false;

        while (!confirmado) {
            System.out.println("\nPedido registrado: " + pedido);
            System.out.print("¿El cliente confirma el pedido? (S/N): ");
            String respuesta = sc.nextLine().trim().toUpperCase();

            if (respuesta.equals("S")) {
                confirmado = true;
            } else if (respuesta.equals("N")) {
                pedido = modificarPedido();
            } else {
                System.out.println("Respuesta no válida. Escriba S o N.");
            }
        }

        boolean ingredientesDisponibles = verificarIngredientes(pedido);

        if (!ingredientesDisponibles) {
            System.out.println("\nNo hay ingredientes suficientes para el pedido.");
            System.out.println("Se ofrecerá una alternativa.");
            pedido = ofrecerAlternativa();
        }

        double total = calcularTotal(pedido);

        System.out.printf("\nTotal del pedido: $%.2f%n", total);
        cobrarPedido(total);

        entregarPedido(pedido, total);
    }

    public static void mostrarMenu() {
        System.out.println("\n--- MENÚ FAST BURGER ---");
        System.out.println("1. Hamburguesa clásica - $80");
        System.out.println("2. Hamburguesa con queso - $95");
        System.out.println("3. Hamburguesa doble - $120");
        System.out.println("4. Hamburguesa especial - $140");
    }

    public static String tomarPedido() {
        while (true) {
            System.out.print("Seleccione el producto (1-4): ");
            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    return "Hamburguesa clásica";
                case 2:
                    return "Hamburguesa con queso";
                case 3:
                    return "Hamburguesa doble";
                case 4:
                    return "Hamburguesa especial";
                default:
                    System.out.println("Producto no válido.");
            }
        }
    }

    public static String modificarPedido() {
        System.out.println("\n--- MODIFICAR PEDIDO ---");
        mostrarMenu();
        return tomarPedido();
    }

    public static boolean verificarIngredientes(String pedido) {
        System.out.println("\n--- VERIFICAR INGREDIENTES ---");

        // Simulación: la hamburguesa especial se considera sin disponibilidad.
        if (pedido.equals("Hamburguesa especial")) {
            System.out.println("Ingredientes disponibles: NO");
            return false;
        }

        System.out.println("Ingredientes disponibles: SÍ");
        return true;
    }

    public static String ofrecerAlternativa() {
        System.out.println("\n--- OFRECER ALTERNATIVA ---");
        System.out.println("Alternativa disponible: Hamburguesa con queso - $95");
        System.out.print("¿Aceptar alternativa? (S/N): ");

        while (true) {
            String respuesta = sc.nextLine().trim().toUpperCase();

            if (respuesta.equals("S")) {
                return "Hamburguesa con queso";
            }

            if (respuesta.equals("N")) {
                System.out.println("Se regresará a la selección del pedido.");
                mostrarMenu();
                return tomarPedido();
            }

            System.out.print("Respuesta no válida. Escriba S o N: ");
        }
    }

    public static double calcularTotal(String pedido) {
        switch (pedido) {
            case "Hamburguesa clásica":
                return 80.0;
            case "Hamburguesa con queso":
                return 95.0;
            case "Hamburguesa doble":
                return 120.0;
            case "Hamburguesa especial":
                return 140.0;
            default:
                return 0.0;
        }
    }

    public static void cobrarPedido(double total) {
        System.out.println("\n--- COBRAR PEDIDO ---");
        System.out.printf("Monto a pagar: $%.2f%n", total);

        boolean pagado = false;

        while (!pagado) {
            System.out.println("\n--- REALIZAR PAGO ---");
            System.out.println("1. Efectivo");
            System.out.println("2. Tarjeta");
            System.out.println("3. Transferencia");
            System.out.print("Seleccione método de pago: ");

            int metodo = leerEntero();

            if (metodo < 1 || metodo > 3) {
                System.out.println("Método no válido.");
                continue;
            }

            System.out.print("¿El pago fue aprobado? (S/N): ");
            String respuesta = sc.nextLine().trim().toUpperCase();

            if (respuesta.equals("S")) {
                pagado = true;
                System.out.println("Pago aprobado correctamente.");
            } else if (respuesta.equals("N")) {
                System.out.println("Pago rechazado.");
                System.out.println("Solicitando otro método de pago...");
            } else {
                System.out.println("Respuesta no válida.");
            }
        }
    }

    public static void entregarPedido(String pedido, double total) {
        System.out.println("\n--- ENTREGA DEL PEDIDO ---");
        System.out.println("Pedido: " + pedido);
        System.out.printf("Total pagado: $%.2f%n", total);
        System.out.println("Pedido entregado.");
        System.out.println("Cliente recibe el pedido.");
        System.out.println("\n=== PROCESO FINALIZADO ===");
    }

    public static int leerEntero() {
        while (true) {
            try {
                int valor = Integer.parseInt(sc.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número válido: ");
            }
        }
    }
}
