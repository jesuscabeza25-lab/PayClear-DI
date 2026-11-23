
/**
 *
 * @author Equipo PayClear
 */

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TarjetaSaldoParticipanteTest {

    private static int pasadas = 0;
    private static int falladas = 0;

    public static void main(String[] args) {
        ejecutar("1. Unitaria: Estado inicial por defecto", TarjetaSaldoParticipanteTest::testEstadoInicialPorDefecto);
        ejecutar("2. Unitaria: Estados, colores y tolerancia 0.005", TarjetaSaldoParticipanteTest::testEstadosYColoresConTolerancia);
        ejecutar("3. Unitaria: Eventos PropertyChangeListener", TarjetaSaldoParticipanteTest::testEventosPropertyChange);
        ejecutar("4. Unitaria: Accesibilidad NVDA", TarjetaSaldoParticipanteTest::testAccesibilidadNVDA);
        ejecutar("5. UI Automatizada: Clic izquierdo y hover", TarjetaSaldoParticipanteTest::testAutomatizacionRaton);
        ejecutar("6. UI Automatizada: Pulsacion tecla Enter", TarjetaSaldoParticipanteTest::testAutomatizacionTeclado);
        ejecutar("7. Seguridad y Robustez: Entradas nulas y extremas", TarjetaSaldoParticipanteTest::testSeguridadYRobustez);
        ejecutar("8. Rendimiento y Recursos: Estres 500 tarjetas", TarjetaSaldoParticipanteTest::testUsoDeRecursosYEstres);

        System.out.println("\n========================================");
        System.out.println("RESUMEN QA: " + pasadas + " PASADAS | " + falladas + " FALLADAS");
        System.out.println("========================================");
        if (falladas > 0) {
            System.exit(1);
        }
    }

    @FunctionalInterface
    interface Prueba {
        void run() throws Exception;
    }

    private static void ejecutar(String nombre, Prueba prueba) {
        try {
            prueba.run();
            pasadas++;
            System.out.println("[PASS] " + nombre);
        } catch (Throwable e) {
            falladas++;
            System.out.println("[FAIL] " + nombre + " -> " + e.getMessage());
        }
    }

    private static void verificar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }

    private static void verificarIgual(Object esperado, Object actual, String mensaje) {
        if (!Objects.equals(esperado, actual)) {
            throw new AssertionError(mensaje + " (esperado: " + esperado + ", actual: " + actual + ")");
        }
    }

    private static void testEstadoInicialPorDefecto() {
        TarjetaSaldoParticipante tarjeta = new TarjetaSaldoParticipante();
        verificarIgual("Participante", tarjeta.getNombreParticipante(), "Nombre inicial incorrecto");
        verificar(Math.abs(tarjeta.getSaldoActual() - 0.0) < 0.0001, "Saldo inicial distinto de 0");
        verificarIgual(TarjetaSaldoParticipante.Estado.SALDADO, tarjeta.getEstado(), "Estado inicial incorrecto");
        verificarIgual(new Color(0x59665E), tarjeta.getColorEstado(), "Color inicial incorrecto");
        verificar(tarjeta.isFocusable(), "La tarjeta debe ser focusable");
    }

    private static void testEstadosYColoresConTolerancia() {
        TarjetaSaldoParticipante tarjeta = new TarjetaSaldoParticipante();

        tarjeta.setSaldoActual(25.50);
        verificarIgual(TarjetaSaldoParticipante.Estado.ACREEDOR, tarjeta.getEstado(), "Debe ser ACREEDOR");
        verificarIgual(tarjeta.getColorAcreedor(), tarjeta.getColorEstado(), "Color acreedor incorrecto");

        tarjeta.setSaldoActual(-10.00);
        verificarIgual(TarjetaSaldoParticipante.Estado.DEUDOR, tarjeta.getEstado(), "Debe ser DEUDOR");
        verificarIgual(tarjeta.getColorDeudor(), tarjeta.getColorEstado(), "Color deudor incorrecto");

        tarjeta.setSaldoActual(0.004);
        verificarIgual(TarjetaSaldoParticipante.Estado.SALDADO, tarjeta.getEstado(), "0.004 debe ser SALDADO");

        tarjeta.setSaldoActual(-0.004);
        verificarIgual(TarjetaSaldoParticipante.Estado.SALDADO, tarjeta.getEstado(), "-0.004 debe ser SALDADO");
    }

    private static void testEventosPropertyChange() {
        TarjetaSaldoParticipante tarjeta = new TarjetaSaldoParticipante();
        AtomicReference<PropertyChangeEvent> eventoSaldo = new AtomicReference<>();
        AtomicReference<PropertyChangeEvent> eventoNombre = new AtomicReference<>();

        tarjeta.addPropertyChangeListener(TarjetaSaldoParticipante.PROP_SALDO, eventoSaldo::set);
        tarjeta.addPropertyChangeListener(TarjetaSaldoParticipante.PROP_NOMBRE, eventoNombre::set);

        tarjeta.setNombreParticipante("Ana");
        tarjeta.setSaldoActual(42.75);

        verificar(eventoNombre.get() != null, "No se disparo evento de nombre");
        verificarIgual("Participante", eventoNombre.get().getOldValue(), "Valor antiguo de nombre incorrecto");
        verificarIgual("Ana", eventoNombre.get().getNewValue(), "Valor nuevo de nombre incorrecto");

        verificar(eventoSaldo.get() != null, "No se disparo evento de saldo");
        verificar(Math.abs((double) eventoSaldo.get().getOldValue() - 0.0) < 0.0001, "Valor antiguo de saldo incorrecto");