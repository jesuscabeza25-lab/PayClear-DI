
package com.payclear.persistencia;

/**
 *
 * @author Equipo PayClear
 */

import com.payclear.modelo.Grupo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RepositorioLocal implements RepositorioPayClear {

    private final Path archivoDatos;

    public RepositorioLocal() {
        this(Paths.get(System.getProperty("user.home"), ".payclear", "datos.dat"));
    }

    public RepositorioLocal(Path archivoDatos) {
        this.archivoDatos = archivoDatos;
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized List<Grupo> cargarGrupos() throws IOException {
        if (!Files.exists(archivoDatos)) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(archivoDatos))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                return new ArrayList<>((List<Grupo>) obj);
            }
            return new ArrayList<>();
        } catch (ClassNotFoundException e) {
            throw new IOException("Formato de datos incompatible", e);
        }
    }

    @Override
    public synchronized void guardarGrupos(List<Grupo> grupos) throws IOException {
        Path directorio = archivoDatos.getParent();
        if (directorio != null && !Files.exists(directorio)) {
            Files.createDirectories(directorio);
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(archivoDatos))) {
            oos.writeObject(new ArrayList<>(grupos));
        }
    }
}