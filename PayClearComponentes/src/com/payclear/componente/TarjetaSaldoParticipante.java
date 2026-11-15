package com.payclear.componente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Tarjeta que muestra el nombre de un participante de PayClear y cuánto dinero
 * le deben o debe.
 * <p>
 * El fondo se pinta de verde si le deben dinero, de rojo si debe y de gris si
 * está en paz. También lleva un signo y un texto, para que se entienda aunque
 * no se distingan bien los colores.
 * </p>
 *
 * @author Jose Luis Segura Fernandez
 * @version 1.0
 */
public class TarjetaSaldoParticipante extends JPanel implements Serializable {

    /** Versión de la clase, para guardarla y cargarla sin problemas. */
    private static final long serialVersionUID = 1L;

    // Constantes

    /** Nombre con el que la tarjeta avisa de que ha cambiado el nombre del participante. */
    public static final String PROP_NOMBRE = "nombreParticipante";

    /** Nombre con el que la tarjeta avisa de que ha cambiado el saldo. */
    public static final String PROP_SALDO = "saldoActual";

    /** Medio céntimo: los saldos más pequeños que esto se tratan como cero. */
    public static final double TOLERANCIA = 0.005;

    /** Los tres estados en los que puede estar la tarjeta según su saldo. */
    public enum Estado {
        /** Le deben dinero. */
        ACREEDOR,
        /** Debe dinero. */
        DEUDOR,
        /** No debe nada ni le deben nada. */
        SALDADO
    }

    // Atributos

    /** Nombre del participante que se muestra arriba. */
    private String nombreParticipante;

    /** Saldo en euros: positivo si le deben y negativo si debe. */
    private double saldoActual;

    /** Color de fondo cuando le deben dinero. */
    private Color colorAcreedor;

    /** Color de fondo cuando debe dinero. */
    private Color colorDeudor;

    /** Color de fondo cuando está en paz. */
    private Color colorSaldado;

    /** Se encarga de avisar a quien escucha cuando cambia el nombre o el saldo. */
    private final PropertyChangeSupport propertySupport;

    /** Etiqueta de arriba, con el nombre. */
    private final JLabel lblNombre;

    /** Etiqueta de abajo, con el importe y su texto. */
    private final JLabel lblImporte;

    // Constructor

    /**
     * Crea una tarjeta lista para usar, con el nombre "Participante", saldo 0
     * y un tamaño de 220 x 90 píxeles.
     */
    public TarjetaSaldoParticipante() {
        propertySupport = new PropertyChangeSupport(this);
        nombreParticipante = "Participante";
        saldoActual = 0.0;
        colorAcreedor = new Color(0x11734F);
        colorDeudor = new Color(0xB53C3C);
        colorSaldado = new Color(0x59665E);
        setPreferredSize(new Dimension(220, 90));
        setOpaque(true);

        // Etiquetas: el nombre arriba y el importe abajo, en blanco
        lblNombre = new JLabel();
        lblImporte = new JLabel();
        lblNombre.setForeground(Color.WHITE);
        lblImporte.setForeground(Color.WHITE);
        lblNombre.setFont(lblNombre.getFont().deriveFont(Font.BOLD, 15f));
        lblImporte.setFont(lblImporte.getFont().deriveFont(14f));

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        add(lblNombre, BorderLayout.NORTH);
        add(lblImporte, BorderLayout.SOUTH);

        actualizarVista();
    }

    // Getters y setters

    /**
     * Devuelve el nombre que se muestra en la tarjeta.
     *
     * @return el nombre del participante
     */
    public String getNombreParticipante() {
        return nombreParticipante;
    }

    /**
     * Cambia el nombre de la tarjeta y avisa del cambio a quien la esté
     * escuchando. Si llega vacío, no se hace nada y se queda el que había.
     *
     * @param nombreParticipante el nombre nuevo
     */
    public void setNombreParticipante(String nombreParticipante) {
        if (nombreParticipante != null && !nombreParticipante.isBlank()) {
            String anterior = this.nombreParticipante;
            this.nombreParticipante = nombreParticipante;
            propertySupport.firePropertyChange(PROP_NOMBRE, anterior, nombreParticipante);
            actualizarVista();
        }
    }

    /**
     * Devuelve el saldo del participante en euros.
     *
     * @return el saldo: positivo si le deben y negativo si debe
     */
    public double getSaldoActual() {
        return saldoActual;
    }

    /**
     * Cambia el saldo de la tarjeta y avisa del cambio a quien la esté
     * escuchando. Después se vuelve a pintar con el color y el texto que toquen.
     *
     * @param saldoActual el saldo en euros: positivo si le deben y negativo si debe
     * @see #getEstado()
     */
    public void setSaldoActual(double saldoActual) {
        double anterior = this.saldoActual;
        this.saldoActual = saldoActual;
        propertySupport.firePropertyChange(PROP_SALDO, anterior, saldoActual);
        actualizarVista();
    }

    /**
     * Devuelve el color de fondo que se usa cuando al participante le deben dinero.
     *
     * @return el color de acreedor
     */
    public Color getColorAcreedor() {
        return colorAcreedor;
    }

    /**
     * Cambia el color de fondo para cuando al participante le deben dinero.
     * Si llega vacío, se queda el que había.
     *
     * @param colorAcreedor el color nuevo
     */
    public void setColorAcreedor(Color colorAcreedor) {
        if (colorAcreedor != null) {
            this.colorAcreedor = colorAcreedor;
            actualizarVista();
        }
    }

    /**
     * Devuelve el color de fondo que se usa cuando el participante debe dinero.
     *
     * @return el color de deudor
     */
    public Color getColorDeudor() {
        return colorDeudor;
    }

    /**
     * Cambia el color de fondo para cuando el participante debe dinero.
     * Si llega vacío, se queda el que había.
     *
     * @param colorDeudor el color nuevo
     */
    public void setColorDeudor(Color colorDeudor) {
        if (colorDeudor != null) {
            this.colorDeudor = colorDeudor;
            actualizarVista();
        }
    }

    /**
     * Devuelve el color de fondo que se usa cuando el participante está en paz.
     *
     * @return el color de saldado
     */
    public Color getColorSaldado() {
        return colorSaldado;
    }

    /**
     * Cambia el color de fondo para cuando el participante está en paz.
     * Si llega vacío, se queda el que había.
     *
     * @param colorSaldado el color nuevo
     */
    public void setColorSaldado(Color colorSaldado) {
        if (colorSaldado != null) {
            this.colorSaldado = colorSaldado;
            actualizarVista();
        }
    }

    // Estado calculado

    /**
     * Dice si al participante le deben dinero, si debe o si está en paz. Las
     * cantidades de menos de medio céntimo cuentan como cero, para que los
     * restos de las divisiones no cambien el estado.
     *
     * @return {@code ACREEDOR} si le deben, {@code DEUDOR} si debe
     *         y {@code SALDADO} si está en paz
     */
    public Estado getEstado() {
        if (saldoActual > TOLERANCIA) {
            return Estado.ACREEDOR;
        }
        if (saldoActual < -TOLERANCIA) {
            return Estado.DEUDOR;
        }
        return Estado.SALDADO;
    }

    /**
     * Devuelve el color que le toca a la tarjeta según su estado.
     *
     * @return el color de acreedor, de deudor o de saldado
     * @see #getEstado()
     */
    public Color getColorEstado() {
        switch (getEstado()) {
            case ACREEDOR:
                return colorAcreedor;
            case DEUDOR:
                return colorDeudor;
            default:
                return colorSaldado;
        }
    }

    // Pone los textos y el color de fondo según el estado y vuelve a pintar la tarjeta
    private void actualizarVista() {
        NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("es", "ES"));
        String importe = formato.format(Math.abs(saldoActual));

        lblNombre.setText(nombreParticipante);

        switch (getEstado()) {
            case ACREEDOR:
                lblImporte.setText("+ Le deben " + importe);
                break;
            case DEUDOR:
                lblImporte.setText("- Debe " + importe);
                break;
            default:
                lblImporte.setText("✓ Saldado");
                break;
        }

        setBackground(getColorEstado());
        repaint();
    }

    // Eventos de acción

    /**
     * Apunta a alguien para que se entere cuando se pulse la tarjeta.
     * Los clics los programa Jesús en la tarea 3.2.
     *
     * @param listener quien quiere enterarse de las pulsaciones
     */
    public void addActionListener(ActionListener listener) {
        // TODO [3.2] Jesús: guardar el listener y avisarle al hacer clic
    }

    /**
     * Quita a alguien que estaba apuntado para enterarse de las pulsaciones.
     * Se completa en la tarea 3.2.
     *
     * @param listener quien ya no quiere enterarse
     */
    public void removeActionListener(ActionListener listener) {
        // TODO [3.2] Jesús: quitar el listener
    }

    // Avisos de cambios. Se apunta a cada uno en Swing y en nuestro propertySupport.
    // El if hace falta porque el constructor de JPanel llama a estos métodos
    // antes de que exista propertySupport.

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        if (propertySupport != null) {
            propertySupport.addPropertyChangeListener(listener);
        }
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        super.removePropertyChangeListener(listener);
        if (propertySupport != null) {
            propertySupport.removePropertyChangeListener(listener);
        }
    }

    @Override
    public void addPropertyChangeListener(String propiedad, PropertyChangeListener listener) {
        super.addPropertyChangeListener(propiedad, listener);
        if (propertySupport != null) {
            propertySupport.addPropertyChangeListener(propiedad, listener);
        }
    }

    @Override
    public void removePropertyChangeListener(String propiedad, PropertyChangeListener listener) {
        super.removePropertyChangeListener(propiedad, listener);
        if (propertySupport != null) {
            propertySupport.removePropertyChangeListener(propiedad, listener);
        }
    }
}