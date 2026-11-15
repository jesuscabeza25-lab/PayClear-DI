package com.payclear.componente;

/**
 *
 * @author Jose Luis Segura Fernandez
 */

import java.io.Serializable;
import javax.swing.JPanel;

public class TarjetaSaldoParticipante extends JPanel implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PROP_NOMBRE = "nombreParticipante";
    public static final String PROP_SALDO = "saldoActual";
    public static final double TOLERANCIA = 0.005;

    public enum Estado {
        ACREEDOR, DEUDOR, SALDADO
    }
}
