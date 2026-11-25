
package com.payclear.modelo;

/**
 *
 * @author Equipo PayClear
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Grupo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private List<Participante> participantes;
    private List<Gasto> gastos;

    public Grupo(String nombre) {
        this.nombre = nombre;
        this.participantes = new ArrayList<>();
        this.gastos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Participante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Participante> participantes) {
        this.participantes = participantes != null ? new ArrayList<>(participantes) : new ArrayList<>();
    }

    public List<Gasto> getGastos() {
        return gastos;
    }

    public void setGastos(List<Gasto> gastos) {
        this.gastos = gastos != null ? new ArrayList<>(gastos) : new ArrayList<>();
    }
}