import com.payclear.componente.TarjetaSaldoParticipante;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.SwingUtilities;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Equipo PayClear
 */
public class TarjetaSaldoParticipanteTest {

    @Test
    public void testValoresPorDefecto() {
        TarjetaSaldoParticipante tarjeta = new TarjetaSaldoParticipante();
        assertEquals("Participante", tarjeta.getNombreParticipante());
        assertEquals(0.0, tarjeta.getSaldoActual(), 0.001);
        assertTrue(tarjeta.isFocusable());
    }

    @Test
    public void testSettersModificanEstadoInterno() {
        TarjetaSaldoParticipante tarjeta = new TarjetaSaldoParticipante();
        tarjeta.setNombreParticipante("Ana");
        tarjeta.setSaldoActual(45.50);
        assertEquals("Ana", tarjeta.getNombreParticipante());
        assertEquals(45.50, tarjeta.getSaldoActual(), 0.001);
    }

    @Test
    public void testEstadoVisualColoresPorSaldo() {
        TarjetaSaldoParticipante tarjeta = new TarjetaSaldoParticipante();

        tarjeta.setSaldoActual(25.0);
        assertEquals(tarjeta.getColorAcreedor(), tarjeta.getColorEstado());

        tarjeta.setSaldoActual(-15.0);
        assertEquals(tarjeta.getColorDeudor(), tarjeta.getColorEstado());

        tarjeta.setSaldoActual(0.0);
        assertEquals(tarjeta.getColorSaldado(), tarjeta.getColorEstado());
    }

    @Test
    public void testValoresNulosLanzanExcepcion() {
        TarjetaSaldoParticipante tarjeta = new TarjetaSaldoParticipante();
        assertThrows(IllegalArgumentException.class, () -> tarjeta.setNombreParticipante(null));
        assertThrows(IllegalArgumentException.class, () -> tarjeta.setNombreParticipante("   "));
        assertThrows(IllegalArgumentException.class, () -> tarjeta.setColorAcreedor(null));
        assertThrows(IllegalArgumentException.class, () -> tarjeta.setColorDeudor(null));
        assertThrows(IllegalArgumentException.class, () -> tarjeta.setColorSaldado(null));
    }

    @Test
    public void testClicDisparaActionListener() throws Exception {
        AtomicBoolean disparado = new AtomicBoolean(false);
        TarjetaSaldoParticipante tarjeta = new TarjetaSaldoParticipante();
        tarjeta.addActionListener(e -> disparado.set(true));

        SwingUtilities.invokeAndWait(() -> {
            MouseEvent clic = new MouseEvent(
                    tarjeta,
                    MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(),
                    0,
                    10,
                    10,
                    1,
                    false,
                    MouseEvent.BUTTON1
            );
            tarjeta.dispatchEvent(clic);
        });

        assertTrue(disparado.get());
    }

    @Test
    public void testCambioSaldoDisparaPropertyChangeEvent() {
        TarjetaSaldoParticipante tarjeta = new TarjetaSaldoParticipante();
        AtomicReference<PropertyChangeEvent> eventoCapturado = new AtomicReference<>();

        tarjeta.addPropertyChangeListener(TarjetaSaldoParticipante.PROP_SALDO, eventoCapturado::set);
        tarjeta.setSaldoActual(100.0);

        assertNotNull(eventoCapturado.get());
        assertEquals(0.0, (Double) eventoCapturado.get().getOldValue(), 0.001);
        assertEquals(100.0, (Double) eventoCapturado.get().getNewValue(), 0.001);
    }
}