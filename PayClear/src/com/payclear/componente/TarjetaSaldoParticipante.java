
package com.payclear.componente;

/**
 *
 * @author Equipo PayClear
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Tarjeta que muestra el nombre de un participante de PayClear y cuánto dinero
 * le deben o debe.
 * <p>
 * El fondo se pinta de verde si le deben dinero, de rojo si debe y de gris si
 * está en paz. También lleva un signo y un texto para que se entienda aunque
 * no se distingan bien los colores, y responde a eventos de ratón y teclado.
 * </p>
 *
 * <h2>Reglas de estado</h2>
 * <ul>
 *   <li>{@link Estado#ACREEDOR}: cuando {@code saldoActual > TOLERANCIA}.</li>
 *   <li>{@link Estado#DEUDOR}: cuando {@code saldoActual < -TOLERANCIA}.</li>
 *   <li>{@link Estado#SALDADO}: cuando {@code Math.abs(saldoActual) <= TOLERANCIA}.</li>
 * </ul>
 *
 * @author Equipo PayClear
 * @see Estado
 * @see #setSaldoActual(double)
 */
public class TarjetaSaldoParticipante extends JPanel implements Serializable {

    /** Versión de la clase, para guardarla y cargarla sin problemas. */
    private static final long serialVersionUID = 1L;

    // Constantes

    /**
     * Nombre de propiedad con el que la tarjeta avisa de que ha cambiado el
     * nombre del participante. Su valor es {@value}.
     */
    public static final String PROP_NOMBRE = "nombreParticipante";

    /**
     * Nombre de propiedad con el que la tarjeta avisa de que ha cambiado el
     * saldo del participante. Su valor es {@value}.
     */
    public static final String PROP_SALDO = "saldoActual";

    /**
     * Tolerancia de medio céntimo ({@value}): los saldos cuyo valor absoluto
     * es menor o igual que este umbral se tratan como cero.
     */
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

    /** Lista de observadores que escuchan cuando el usuario pulsa o activa la tarjeta. */
    private final List<ActionListener> actionListeners;

    /** Indica si el cursor del ratón está encima de la tarjeta. */
    private boolean hover;

    /** Indica si la tarjeta está siendo pulsada actualmente con ratón o teclado. */
    private boolean presionado;

    /** Etiqueta de arriba, con el nombre. */
    private final JLabel lblNombre;

    /** Etiqueta de abajo, con el importe y su texto. */
    private final JLabel lblImporte;

    // Constructor

    /**
     * Crea una tarjeta lista para usar, con el nombre {@code "Participante"},
     * saldo {@code 0.0} y un tamaño preferido de 220 x 90 píxeles.
     */
    public TarjetaSaldoParticipante() {
        propertySupport = new PropertyChangeSupport(this);
        actionListeners = new ArrayList<>();
        hover = false;
        presionado = false;

        nombreParticipante = "Participante";
        saldoActual = 0.0;
        colorAcreedor = new Color(0x11734F);
        colorDeudor = new Color(0xB53C3C);
        colorSaldado = new Color(0x59665E);
        setPreferredSize(new Dimension(220, 90));
        setOpaque(true);

        // Permitir recibir foco con teclado (Tab) y mostrar cursor de mano
        setFocusable(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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

        inicializarEventosInteraccion();
        actualizarVista();
    }

    /**
     * Configura los escuchadores de ratón, foco y teclado para detectar la
     * interacción del usuario con la tarjeta.
     */
    private void inicializarEventosInteraccion() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    hover = true;
                    actualizarEstiloInteraccion();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                presionado = false;
                actualizarEstiloInteraccion();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled() && SwingUtilities.isLeftMouseButton(e)) {
                    presionado = true;
                    requestFocusInWindow();
                    actualizarEstiloInteraccion();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (presionado) {
                    presionado = false;
                    actualizarEstiloInteraccion();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (isEnabled() && SwingUtilities.isLeftMouseButton(e)) {
                    dispararActionEvent();
                }
            }
        });

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                actualizarEstiloInteraccion();
            }

            @Override
            public void focusLost(FocusEvent e) {
                presionado = false;
                actualizarEstiloInteraccion();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isEnabled() && (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE)) {
                    presionado = true;
                    actualizarEstiloInteraccion();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (presionado && (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE)) {
                    presionado = false;
                    actualizarEstiloInteraccion();
                    if (isEnabled()) {
                        dispararActionEvent();
                    }
                }
            }
        });
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
     * escuchando mediante la propiedad {@link #PROP_NOMBRE}.
     *
     * @param nombreParticipante el nombre nuevo del participante
     * @throws IllegalArgumentException si el nombre es {@code null} o está en blanco
     * @see #PROP_NOMBRE
     */
    public void setNombreParticipante(String nombreParticipante) {
        if (nombreParticipante == null || nombreParticipante.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacio");
        }
        String anterior = this.nombreParticipante;
        this.nombreParticipante = nombreParticipante;
        propertySupport.firePropertyChange(PROP_NOMBRE, anterior, this.nombreParticipante);
        actualizarVista();
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
     * escuchando mediante la propiedad {@link #PROP_SALDO}. Después se vuelve
     * a pintar con el color y el texto correspondientes.
     *
     * @param saldoActual el saldo en euros: positivo si le deben y negativo si debe
     * @see #getEstado()
     * @see #PROP_SALDO
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
     *
     * @param colorAcreedor el color nuevo para el estado acreedor
     * @throws IllegalArgumentException si {@code colorAcreedor} es {@code null}
     */
    public void setColorAcreedor(Color colorAcreedor) {
        if (colorAcreedor == null) {
            throw new IllegalArgumentException("El color de acreedor no puede ser nulo");
        }
        this.colorAcreedor = colorAcreedor;
        actualizarVista();
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
     *
     * @param colorDeudor el color nuevo para el estado deudor
     * @throws IllegalArgumentException si {@code colorDeudor} es {@code null}
     */
    public void setColorDeudor(Color colorDeudor) {
        if (colorDeudor == null) {
            throw new IllegalArgumentException("El color de deudor no puede ser nulo");
        }
        this.colorDeudor = colorDeudor;
        actualizarVista();
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
     *
     * @param colorSaldado el color nuevo para el estado saldado
     * @throws IllegalArgumentException si {@code colorSaldado} es {@code null}
     */
    public void setColorSaldado(Color colorSaldado) {
        if (colorSaldado == null) {
            throw new IllegalArgumentException("El color de saldado no puede ser nulo");
        }
        this.colorSaldado = colorSaldado;
        actualizarVista();
    }

    // Estado calculado

    /**
     * Indica si al participante le deben dinero, si debe o si está en paz. Las
     * cantidades cuyo valor absoluto es menor o igual que {@link #TOLERANCIA}
     * cuentan como cero, para que los restos de las divisiones no alteren el estado.
     *
     * @return {@link Estado#ACREEDOR} si le deben, {@link Estado#DEUDOR} si debe
     *         y {@link Estado#SALDADO} si está en paz
     * @see #TOLERANCIA
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
     * Devuelve el color que le corresponde a la tarjeta según su estado actual.
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

    // Pone los textos, accesibilidad y el color de fondo según el estado
    private void actualizarVista() {
        NumberFormat formato = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-ES"));
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

        // Accesibilidad para lectores de pantalla (NVDA)
        getAccessibleContext().setAccessibleName("Tarjeta de saldo de " + nombreParticipante);
        getAccessibleContext().setAccessibleDescription(
                "Participante " + nombreParticipante + ", " + lblImporte.getText()
                + ". Pulse Intro o Espacio para seleccionar."
        );

        actualizarEstiloInteraccion();
    }

    // Actualiza fondo y borde en hover, pulsación y foco manteniendo el margen total (12, 14, 12, 14)
    private void actualizarEstiloInteraccion() {
        Color base = getColorEstado();

        if (presionado) {
            setBackground(base.darker());
        } else if (hover) {
            setBackground(aclararColor(base, 18));
        } else {
            setBackground(base);
        }

        if (hasFocus()) {
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 2),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
            ));
        } else if (hover || presionado) {
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 255, 255, 180), 1),
                    BorderFactory.createEmptyBorder(11, 13, 11, 13)
            ));
        } else {
            setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        }

        repaint();
    }

    // Aclara ligeramente el color base en hover sin perder contraste con las letras blancas
    private Color aclararColor(Color color, int incremento) {
        int r = Math.min(255, color.getRed() + incremento);
        int g = Math.min(255, color.getGreen() + incremento);
        int b = Math.min(255, color.getBlue() + incremento);
        return new Color(r, g, b);
    }

    // Eventos de acción

    /**
     * Registra un observador para recibir avisos cuando se pulse o active la tarjeta.
     *
     * @param listener observador que recibirá los eventos de acción
     * @see #removeActionListener(ActionListener)
     */
    public void addActionListener(ActionListener listener) {
        if (listener != null && !actionListeners.contains(listener)) {
            actionListeners.add(listener);
        }
    }

    /**
     * Elimina un observador previamente registrado para los eventos de acción.
     *
     * @param listener observador que se desea dar de baja
     * @see #addActionListener(ActionListener)
     */
    public void removeActionListener(ActionListener listener) {
        if (listener != null) {
            actionListeners.remove(listener);
        }
    }

    /**
     * Notifica a todos los observadores registrados que la tarjeta ha sido activada.
     */
    protected void dispararActionEvent() {
        ActionEvent evento = new ActionEvent(
                this,
                ActionEvent.ACTION_PERFORMED,
                nombreParticipante
        );
        for (ActionListener listener : new ArrayList<>(actionListeners)) {
            listener.actionPerformed(evento);
        }
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