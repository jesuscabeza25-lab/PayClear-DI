
package com.payclear.modelo;

/**
 *
 * @author Equipo PayClear
 */

import java.io.Serializable;

public class Participante implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String nombre;
    private double saldo;

    public Participante(String id, String nombre, double saldo) {
        this.id = id;
        this.nombre = nombre;
        this.saldo = saldo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}