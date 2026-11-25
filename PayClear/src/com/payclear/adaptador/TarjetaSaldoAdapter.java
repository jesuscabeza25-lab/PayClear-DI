
package com.payclear.adaptador;

/**
 *
 * @author DAM2
 */

import com.payclear.componente.TarjetaSaldoParticipante;
import com.payclear.modelo.Participante;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TarjetaSaldoAdapter {

    public static TarjetaSaldoParticipante crearTarjeta(
            Participante participante,
            Consumer<Participante> alSeleccionar) {

        TarjetaSaldoParticipante tarjeta = new TarjetaSaldoParticipante();
        actualizarTarjeta(tarjeta, participante);
        if (alSeleccionar != null) {
            tarjeta.addActionListener(e -> alSeleccionar.accept(participante));
        }
        return tarjeta;
    }

    public static void actualizarTarjeta(TarjetaSaldoParticipante tarjeta, Participante participante) {
        tarjeta.setNombreParticipante(participante.getNombre());
        tarjeta.setSaldoActual(participante.getSaldo());
    }

    public static List<TarjetaSaldoParticipante> crearListaTarjetas(
            List<Participante> participantes,
            Consumer<Participante> alSeleccionar) {

        List<TarjetaSaldoParticipante> tarjetas = new ArrayList<>();
        if (participantes == null) {
            return tarjetas;
        }
        for (Participante p : participantes) {
            tarjetas.add(crearTarjeta(p, alSeleccionar));
        }
        return tarjetas;
    }
}