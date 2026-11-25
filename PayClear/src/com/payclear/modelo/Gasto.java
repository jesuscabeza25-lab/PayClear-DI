
package com.payclear.modelo;

/**
 *
 * @author Equipo PayClear
 */

import java.io.Serializable;
import java.time.LocalDate;

public class Gasto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String concepto;
    private String pagador;
    private double importe;
    private LocalDate fecha;

    public Gasto(String concepto, String pagador, double importe, LocalDate fecha) {
        this.concepto = concepto;
        this.pagador = pagador;
        this.importe = importe;
        this.fecha = fecha;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getPagador() {
        return pagador;
    }

    public void setPagador(String pagador) {
        this.pagador = pagador;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}