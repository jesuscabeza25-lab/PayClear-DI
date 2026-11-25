
package com.payclear.persistencia;

/**
 *
 * @author Equipo PayClear
 */

import com.payclear.modelo.Grupo;
import java.io.IOException;
import java.util.List;

public interface RepositorioPayClear {

    List<Grupo> cargarGrupos() throws IOException;

    void guardarGrupos(List<Grupo> grupos) throws IOException;
}