/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.payclear.servicio;

/**
 *
 * @author DAM2
 */

import com.payclear.adaptador.GastoTableModel;
import com.payclear.adaptador.TarjetaSaldoAdapter;
import com.payclear.componente.TarjetaSaldoParticipante;
import com.payclear.modelo.Gasto;
import com.payclear.modelo.Grupo;
import com.payclear.modelo.Participante;
import com.payclear.persistencia.RepositorioLocal;
import com.payclear.persistencia.RepositorioPayClear;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

public class PruebaT34 {

    public static void main(String[] args) throws Exception {
        Path archivoTemporal = Files.createTempFile("payclear-test-", ".dat");
        RepositorioLocal repoLocal = new RepositorioLocal(archivoTemporal);

        RepositorioPayClear repoVerificado = new RepositorioPayClear() {
            @Override
            public List<Grupo> cargarGrupos() throws IOException {
                System.out.println("Cargando en hilo: " + Thread.currentThread().getName()
                        + " | Es UI Thread (debe ser false): " + SwingUtilities.isEventDispatchThread());
                return repoLocal.cargarGrupos();
            }

            @Override
            public void guardarGrupos(List<Grupo> grupos) throws IOException {
                System.out.println("Guardando en hilo: " + Thread.currentThread().getName()
                        + " | Es UI Thread (debe ser false): " + SwingUtilities.isEventDispatchThread());
                repoLocal.guardarGrupos(grupos);
            }
        };

        GestorPersistenciaAsync gestor = new GestorPersistenciaAsync(repoVerificado);

        Grupo grupo = new Grupo("Viaje Sevilla");
        grupo.getParticipantes().add(new Participante("1", "Ana", 45.50));
        grupo.getParticipantes().add(new Participante("2", "Luis", -20.00));
        grupo.getParticipantes().add(new Participante("3", "Carlos", 0.0));
        grupo.getGastos().add(new Gasto("Cena", "Ana", 60.0, LocalDate.now()));
        grupo.getGastos().add(new Gasto("Gasolina", "Luis", 35.5, LocalDate.now()));

        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("Prueba T-3.4");
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setSize(720, 420);
            ventana.setLayout(new BorderLayout(10, 10));

            JPanel panelTarjetas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            GastoTableModel tableModel = new GastoTableModel();
            JTable tabla = new JTable(tableModel);

            ventana.add(panelTarjetas, BorderLayout.NORTH);
            ventana.add(new JScrollPane(tabla), BorderLayout.CENTER);
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);

            gestor.guardarGruposAsync(
                    List.of(grupo),
                    () -> {
                        System.out.println("Guardado OK en UI Thread (debe ser true): "
                                + SwingUtilities.isEventDispatchThread());
                        gestor.cargarGruposAsync(
                                gruposCargados -> {
                                    System.out.println("Cargado OK en UI Thread (debe ser true): "
                                            + SwingUtilities.isEventDispatchThread());
                                    Grupo g = gruposCargados.get(0);
                                    tableModel.setGastos(g.getGastos());

                                    List<TarjetaSaldoParticipante> tarjetas = TarjetaSaldoAdapter.crearListaTarjetas(
                                            g.getParticipantes(),
                                            p -> JOptionPane.showMessageDialog(ventana, "Seleccionado: " + p.getNombre())
                                    );
                                    for (TarjetaSaldoParticipante t : tarjetas) {
                                        panelTarjetas.add(t);
                                    }
                                    panelTarjetas.revalidate();
                                    panelTarjetas.repaint();
                                },
                                Throwable::printStackTrace
                        );
                    },
                    Throwable::printStackTrace
            );
        });
    }
}