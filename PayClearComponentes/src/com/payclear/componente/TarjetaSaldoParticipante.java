package com.payclear.componente;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.swing.JPanel;

/**
 *
 * @author Jose Luis Segura Fernandez
 */
public class TarjetaSaldoParticipante extends JPanel implements Serializable {

    private static final long serialVersionUID = 1L;

    // Constantes 
    public static final String PROP_NOMBRE = "nombreParticipante";
    public static final String PROP_SALDO = "saldoActual";
    public static final double TOLERANCIA = 0.005;

    public enum Estado {
        ACREEDOR, DEUDOR, SALDADO
    }

    //  Atributos 
    private String nombreParticipante;
    private double saldoActual;
    private Color colorAcreedor;
    private Color colorDeudor;
    private Color colorSaldado;

    private final PropertyChangeSupport propertySupport;

    //Constructor 
    public TarjetaSaldoParticipante() {
        propertySupport = new PropertyChangeSupport(this);
        nombreParticipante = "Participante";
        saldoActual = 0.0;
        colorAcreedor = new Color(0x11734F);
        colorDeudor = new Color(0xB53C3C);
        colorSaldado = new Color(0x59665E);
        setPreferredSize(new Dimension(220, 90));
        setOpaque(true);
    }

        //Getters y setters
    public String getNombreParticipante() {
        return nombreParticipante;
    }

    public void setNombreParticipante(String nombreParticipante) {
        if (nombreParticipante != null && !nombreParticipante.isBlank()) {
            String anterior = this.nombreParticipante;
            this.nombreParticipante = nombreParticipante;
            propertySupport.firePropertyChange(PROP_NOMBRE, anterior, nombreParticipante);
            repaint();
        }
    }

    public double getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(double saldoActual) {
        double anterior = this.saldoActual;
        this.saldoActual = saldoActual;
        propertySupport.firePropertyChange(PROP_SALDO, anterior, saldoActual);
        repaint();
    }

    public Color getColorAcreedor() {
        return colorAcreedor;
    }

    public void setColorAcreedor(Color colorAcreedor) {
        if (colorAcreedor != null) {
            this.colorAcreedor = colorAcreedor;
            repaint();
        }
    }

    public Color getColorDeudor() {
        return colorDeudor;
    }

    public void setColorDeudor(Color colorDeudor) {
        if (colorDeudor != null) {
            this.colorDeudor = colorDeudor;
            repaint();
        }
    }

    public Color getColorSaldado() {
        return colorSaldado;
    }

    public void setColorSaldado(Color colorSaldado) {
        if (colorSaldado != null) {
            this.colorSaldado = colorSaldado;
            repaint();
        }
    }


    //Estado calculado 
    public Estado getEstado() {
        if (saldoActual > TOLERANCIA) {
            return Estado.ACREEDOR;
        }
        if (saldoActual < -TOLERANCIA) {
            return Estado.DEUDOR;
        }
        return Estado.SALDADO;
    }
    public Color getColorEstado() {
        return switch (getEstado()) {
            case ACREEDOR -> colorAcreedor;
            case DEUDOR -> colorDeudor;
            default -> colorSaldado;
        };
    }

    // Eventos de acción 
    public void addActionListener(ActionListener listener) {
        // TODO [3.2] Jesús: guardar el listener y avisarle al hacer clic
    }

    public void removeActionListener(ActionListener listener) {
        // TODO [3.2] Jesús: quitar el listener
    }

    //  Avisos de cambios: propiedades enlazadas 
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