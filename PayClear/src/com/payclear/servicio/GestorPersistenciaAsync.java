
package com.payclear.servicio;

/**
 *
 * @author Equipo PayClear
 */

import com.payclear.modelo.Grupo;
import com.payclear.persistencia.RepositorioPayClear;
import java.util.List;
import java.util.function.Consumer;

public class GestorPersistenciaAsync {

    private final RepositorioPayClear repositorio;

    public GestorPersistenciaAsync(RepositorioPayClear repositorio) {
        this.repositorio = repositorio;
    }

    public void cargarGruposAsync(Consumer<List<Grupo>> alCargar, Consumer<Throwable> alFallar) {
        EjecutorAsincrono.ejecutar(
                repositorio::cargarGrupos,
                alCargar,
                alFallar
        );
    }

    public void guardarGruposAsync(List<Grupo> grupos, Runnable alGuardar, Consumer<Throwable> alFallar) {
        EjecutorAsincrono.ejecutar(
                () -> {
                    repositorio.guardarGrupos(grupos);
                    return null;
                },
                ignorado -> {
                    if (alGuardar != null) {
                        alGuardar.run();
                    }
                },
                alFallar
        );
    }
}