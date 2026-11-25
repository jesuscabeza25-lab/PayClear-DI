
package com.payclear.adaptador;

/**
 *
 * @author Equipo PayClear
 */

import com.payclear.modelo.Gasto;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class GastoTableModel extends AbstractTableModel {

    private final String[] columnas = {"Concepto", "Pagador", "Importe", "Fecha"};
    private List<Gasto> gastos = new ArrayList<>();

    public void setGastos(List<Gasto> nuevosGastos) {
        this.gastos = nuevosGastos != null ? new ArrayList<>(nuevosGastos) : new ArrayList<>();
        fireTableDataChanged();
    }

    public Gasto getGastoEnFila(int fila) {
        return gastos.get(fila);
    }

    @Override
    public int getRowCount() {
        return gastos.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Gasto gasto = gastos.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return gasto.getConcepto();
            case 1:
                return gasto.getPagador();
            case 2:
                return String.format("%.2f €", gasto.getImporte());
            case 3:
                return gasto.getFecha() != null ? gasto.getFecha().toString() : "";
            default:
                return "";
        }
    }
}