
package com.payclear.servicio;

/**
 *
 * @author Equipo PayClear
 */

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import javax.swing.SwingWorker;

public class EjecutorAsincrono {

    public static <T> void ejecutar(
            Callable<T> tareaSegundoPlano,
            Consumer<T> alCompletarEnUI,
            Consumer<Throwable> alFallarEnUI) {

        SwingWorker<T, Void> worker = new SwingWorker<>() {
            @Override
            protected T doInBackground() throws Exception {
                return tareaSegundoPlano.call();
            }

            @Override
            protected void done() {
                try {
                    T resultado = get();
                    if (alCompletarEnUI != null) {
                        alCompletarEnUI.accept(resultado);
                    }
                } catch (Exception e) {
                    Throwable causa = e.getCause() != null ? e.getCause() : e;
                    if (alFallarEnUI != null) {
                        alFallarEnUI.accept(causa);
                    }
                }
            }
        };
        worker.execute();
    }
}