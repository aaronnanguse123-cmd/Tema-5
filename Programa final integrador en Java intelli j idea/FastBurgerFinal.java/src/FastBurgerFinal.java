import java.util.Scanner;

/**
 * FAST BURGER
 * Programa Final Integrador - Simulación
 *
 * Representa el proceso BPMN:
 * Inicio -> Solicitar menú -> Tomar pedido -> Registrar pedido
 * -> Confirmar pedido -> Verificar ingredientes -> Preparar pedido
 * -> Calcular total -> Cobrar -> Realizar pago
 * -> Entregar pedido -> Recibir pedido -> Fin.
 */
public class FastBurgerFinal {

    private static final Scanner scanner = new Scanner(System.in);

    // Precios del menú
    private static final double PRECIO_CLASICA = 80.00;
    private static final double PRECIO_QUESO = 95.00;
    private static final double PRECIO_DOBLE = 120.00;
    private static final double PRECIO_ESPECIAL = 140.00;

    // Inventario simulado de cada producto
    private static int stockClasica = 10;
    private static int stockQueso = 8;
    private static int stockDoble = 6;
    private static int stockEspecial = 0;

    public static void main(String[] args) {

        boolean continuar = true;

        System.out.println("================================================");
        System.out.println("             FAST BURGER");
        System.out.println("       PROGRAMA FINAL INTEGRADOR");
        System.out.println("================================================");

        while (continuar) {
            mostrarMenuPrincipal();
            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    procesarVenta();
                    break;

                case 2:
                    mostrarInventario();
                    break;

                case 3:
                    System.out.println("\nGracias por utilizar Fast Burger.");
                    System.out.println("Programa finalizado.");
                    continuar = false;
                    break;

                default:
                    System.out.println("\nOpción no válida.");
            }
        }

        scanner.close();
    }

    // =========================================================
    // MENÚ PRINCIPAL
    // =========================================================

    private static void mostrarMenuPrincipal() {
        System.out.println("\n---------------- MENÚ PRINCIPAL ----------------");
        System.out.println("1. Realizar pedido");
        System.out.println("2. Consultar inventario");
        System.out.println("3. Salir");
        System.out.println("------------------------------------------------");
    }

    // =========================================================
    // PROCESO PRINCIPAL DE VENTA
    // =========================================================

    private static void procesarVenta() {

        System.out.println("\n================================================");
        System.out.println("              NUEVO PEDIDO");
        System.out.println("================================================");

        mostrarMenuProductos();

        int producto = seleccionarProducto();

        // Ciclo de confirmación del cliente
        boolean confirmado = false;

        while (!confirmado) {

            mostrarResumenProducto(producto);

            String respuesta = leerSiNo(
                    "\n¿El cliente confirma el pedido? (S/N): "
            );

            if (respuesta.equals("S")) {

                confirmado = true;
                System.out.println("Pedido confirmado.");

            } else {

                // Gateway: ¿Cliente confirma? -> No
                System.out.println("\nEl cliente desea modificar el pedido.");
                mostrarMenuProductos();
                producto = seleccionarProducto();
            }
        }

        // Gateway: ¿Hay ingredientes?
        if (!hayIngredientes(producto)) {

            System.out.println("\n-----------------------------------------------");
            System.out.println("No hay ingredientes suficientes.");
            System.out.println("-----------------------------------------------");

            producto = ofrecerAlternativa(producto);

            if (producto == 0) {
                System.out.println("No se pudo continuar con el pedido.");
                return;
            }
        }

        // Preparar pedido
        prepararPedido(producto);

        // Calcular total
        double total = calcularTotal(producto);

        System.out.printf("\nTotal del pedido: $%.2f%n", total);

        // Cobrar pedido
        boolean pagoAprobado = cobrarPedido(total);

        if (!pagoAprobado) {
            System.out.println("\nNo fue posible completar el pago.");
            System.out.println("La venta ha sido cancelada.");
            return;
        }

        // Descontar inventario después de confirmar el pago
        descontarInventario(producto);

        // Entrega
        entregarPedido(producto, total);
    }

    // =========================================================
    // MENÚ DE PRODUCTOS
    // =========================================================

    private static void mostrarMenuProductos() {

        System.out.println("\n--------------- MENÚ FAST BURGER ---------------");
        System.out.printf("1. Hamburguesa clásica  - $%.2f%n", PRECIO_CLASICA);
        System.out.printf("2. Hamburguesa con queso - $%.2f%n", PRECIO_QUESO);
        System.out.printf("3. Hamburguesa doble     - $%.2f%n", PRECIO_DOBLE);
        System.out.printf("4. Hamburguesa especial  - $%.2f%n", PRECIO_ESPECIAL);
        System.out.println("------------------------------------------------");
    }

    private static int seleccionarProducto() {

        while (true) {

            int producto = leerEntero("Seleccione el producto (1-4): ");

            if (producto >= 1 && producto <= 4) {
                return producto;
            }

            System.out.println("Producto no válido. Seleccione una opción del 1 al 4.");
        }
    }

    // =========================================================
    // CONFIRMACIÓN Y PEDIDO
    // =========================================================

    private static void mostrarResumenProducto(int producto) {

        System.out.println("\n------------- PEDIDO REGISTRADO ----------------");
        System.out.println("Producto: " + nombreProducto(producto));
        System.out.printf("Precio: $%.2f%n", calcularTotal(producto));
        System.out.println("-----------------------------------------------");
    }

    // =========================================================
    // INGREDIENTES
    // =========================================================

    private static boolean hayIngredientes(int producto) {

        switch (producto) {

            case 1:
                return stockClasica > 0;

            case 2:
                return stockQueso > 0;

            case 3:
                return stockDoble > 0;

            case 4:
                return stockEspecial > 0;

            default:
                return false;
        }
    }

    private static int ofrecerAlternativa(int productoOriginal) {

        System.out.println("\n------------- ALTERNATIVA ---------------------");

        System.out.println("El producto seleccionado no está disponible.");
        System.out.println("Se ofrecerá una alternativa disponible.");

        // Se busca automáticamente una alternativa disponible.
        for (int producto = 1; producto <= 4; producto++) {

            if (producto != productoOriginal && hayIngredientes(producto)) {

                System.out.println("Alternativa: " + nombreProducto(producto)
                        + " - $" + String.format("%.2f", calcularTotal(producto)));

                String aceptar = leerSiNo("¿El cliente acepta la alternativa? (S/N): ");

                if (aceptar.equals("S")) {
                    return producto;
                }
            }
        }

        System.out.println("No se encontró una alternativa aceptada.");
        return 0;
    }

    // =========================================================
    // PREPARACIÓN
    // =========================================================

    private static void prepararPedido(int producto) {

        System.out.println("\n------------- PREPARAR PEDIDO -----------------");
        System.out.println("Cocina está preparando: " + nombreProducto(producto));
        System.out.println("Pedido preparado correctamente.");
    }

    // =========================================================
    // TOTAL
    // =========================================================

    private static double calcularTotal(int producto) {

        switch (producto) {

            case 1:
                return PRECIO_CLASICA;

            case 2:
                return PRECIO_QUESO;

            case 3:
                return PRECIO_DOBLE;

            case 4:
                return PRECIO_ESPECIAL;

            default:
                return 0.00;
        }
    }

    // =========================================================
    // PAGO
    // =========================================================

    private static boolean cobrarPedido(double total) {

        System.out.println("\n---------------- COBRO -------------------------");
        System.out.printf("Total a pagar: $%.2f%n", total);

        while (true) {

            System.out.println("\nMétodos de pago:");
            System.out.println("1. Efectivo");
            System.out.println("2. Tarjeta");
            System.out.println("3. Transferencia");

            int metodo = leerEntero("Seleccione método de pago: ");

            if (metodo < 1 || metodo > 3) {

                System.out.println("Método de pago no válido.");
                continue;
            }

            System.out.println("Método seleccionado: " + nombreMetodoPago(metodo));

            String aprobado = leerSiNo("¿El pago fue aprobado? (S/N): ");

            if (aprobado.equals("S")) {

                System.out.println("Pago aprobado correctamente.");
                return true;

            } else {

                // Gateway: ¿Pago aprobado? -> No
                System.out.println("Pago rechazado.");
                System.out.println("Solicitando otro método de pago...");
            }
        }
    }

    // =========================================================
    // INVENTARIO
    // =========================================================

    private static void descontarInventario(int producto) {

        switch (producto) {

            case 1:
                stockClasica--;
                break;

            case 2:
                stockQueso--;
                break;

            case 3:
                stockDoble--;
                break;

            case 4:
                stockEspecial--;
                break;
        }
    }

    private static void mostrarInventario() {

        System.out.println("\n------------- INVENTARIO -----------------------");
        System.out.println("Hamburguesa clásica : " + stockClasica);
        System.out.println("Hamburguesa con queso: " + stockQueso);
        System.out.println("Hamburguesa doble    : " + stockDoble);
        System.out.println("Hamburguesa especial : " + stockEspecial);
        System.out.println("-----------------------------------------------");
    }

    // =========================================================
    // ENTREGA
    // =========================================================

    private static void entregarPedido(int producto, double total) {

        System.out.println("\n------------- ENTREGA --------------------------");
        System.out.println("Pedido: " + nombreProducto(producto));
        System.out.printf("Total pagado: $%.2f%n", total);
        System.out.println("Empleado entrega el pedido.");
        System.out.println("Cliente recibe el pedido.");

        System.out.println("\n================================================");
        System.out.println("       PEDIDO COMPLETADO CORRECTAMENTE");
        System.out.println("================================================");
    }

    // =========================================================
    // FUNCIONES AUXILIARES
    // =========================================================

    private static String nombreProducto(int producto) {

        switch (producto) {

            case 1:
                return "Hamburguesa clásica";

            case 2:
                return "Hamburguesa con queso";

            case 3:
                return "Hamburguesa doble";

            case 4:
                return "Hamburguesa especial";

            default:
                return "Producto desconocido";
        }
    }

    private static String nombreMetodoPago(int metodo) {

        switch (metodo) {

            case 1:
                return "Efectivo";

            case 2:
                return "Tarjeta";

            case 3:
                return "Transferencia";

            default:
                return "Desconocido";
        }
    }

    private static String leerSiNo(String mensaje) {

        while (true) {

            System.out.print(mensaje);

            String respuesta = scanner.nextLine().trim().toUpperCase();

            if (respuesta.equals("S") || respuesta.equals("N")) {
                return respuesta;
            }

            System.out.println("Respuesta no válida. Escriba S o N.");
        }
    }

    private static int leerEntero(String mensaje) {

        while (true) {

            System.out.print(mensaje);

            try {

                return Integer.parseInt(scanner.nextLine().trim());

            } catch (NumberFormatException e) {

                System.out.println("Entrada no válida. Escriba un número.");
            }
        }
    }
}
