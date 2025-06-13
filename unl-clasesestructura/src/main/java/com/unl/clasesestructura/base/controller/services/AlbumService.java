package com.unl.clasesestructura.base.controller.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.unl.clasesestructura.base.controller.dao.dao_models.DaoAlbum;
import com.unl.clasesestructura.base.controller.dao.dao_models.DaoBanda;
import com.unl.clasesestructura.base.controller.data_structure.LinkedList;
import com.unl.clasesestructura.base.models.Banda;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

/**
 * Servicio para gestionar operaciones relacionadas con canciones.
 * Permite crear, actualizar y listar canciones, así como obtener datos para
 * combos (selectores).
 * 
 * Anotado con @BrowserCallable para ser accesible desde el frontend
 * y @AnonymousAllowed para permitir acceso sin autenticación.
 */
@BrowserCallable
@AnonymousAllowed
public class AlbumService {

    private DaoAlbum da; // DAO para operaciones de base de datos de Álbumes

    /**
     * Constructor: Inicializa el DAO de Álbumes.
     */
    public AlbumService() {
        da = new DaoAlbum();
    }

    /**
     * Crea una nueva canción con los datos proporcionados.
     * 
     * @param nombre   Nombre de la canción (no vacío).
     * @param duracion Duración en segundos (cadena numérica).
     * @param url      URL del archivo de audio.
     * @param tipo     Tipo de archivo (MP3, WAV, etc.).
     * @param idGenero ID del género asociado.
     * @throws Exception Si hay errores de validación o al guardar.
     */
    public void createAlbum(@NotEmpty @NotBlank @NonNull String nombre, @NonNull Date fecha, Integer idBanda)
            throws Exception {
        // Validación básica de campos obligatorios
        if (nombre.trim().length() > 0 && fecha.toString().length() > 0 && idBanda != null) {
            // Asigna los valores al objeto Álbum
            da.getObj().setNombre(nombre);
            da.getObj().setFecha(fecha);
            da.getObj().setId_banda(idBanda);

            // Intenta guardar el álbum
            if (!da.save()) {
                throw new Exception("Error al guardar el álbum");
            }
        }
    }

    /**
     * Actualiza un álbum existente.
     * 
     * @param id      ID del álbum a actualizar.
     * @param nombre  Nuevo nombre (no vacío).
     * @param fecha   Nueva fecha de lanzamiento.
     * @param idBanda ID de la banda asociada.
     * @throws Exception Si hay errores de validación o al actualizar.
     */
    public void updateAlbum(Integer id, @NotEmpty @NotBlank @NonNull String nombre, @NonNull Date fecha,
            Integer idBanda) throws Exception {
        // Validación de campos y ID
        if (id != null && id > 0 && nombre.trim().length() > 0 && fecha.toString().length() > 0 && idBanda != null) {
            // Obtiene el álbum por ID y actualiza sus datos
            da.setObj(da.listAll().get(id - 1));
            da.getObj().setNombre(nombre);
            da.getObj().setFecha(fecha);
            da.getObj().setId_banda(idBanda);

            // Intenta aplicar la actualización
            if (!da.update(id - 1)) {
                throw new Exception("Error al actualizar el álbum");
            }
        }
    }

    /**
     * Obtiene una lista de álbumes en formato {id, label} para combos/selectores.
     * 
     * @return Lista de HashMap con IDs y nombres de álbumes.
     */
    public List<HashMap> ListaBandaCombo() {
        List<HashMap> lista = new ArrayList<>();
        DaoBanda db = new DaoBanda(); // DAO para bandas

        if (!db.listAll().isEmpty()) {
            // Convierte la lista de bandas a un arreglo
            Banda[] arreglo = db.listAll().toArray();

            // Formatea cada banda como {id, label} para frontend
            for (int i = 0; i < arreglo.length; i++) {
                HashMap<String, String> aux = new HashMap<>();
                aux.put("id", arreglo[i].getId().toString());
                aux.put("label", arreglo[i].getNombre());
                lista.add(aux);
            }
        }
        return lista;
    }

    /**
     * Obtiene una lista detallada de canciones con información relacionada (género,
     * álbum).
     * 
     * @return Lista de HashMap con todos los datos de álbumes.
     */
    public List<HashMap> listAlbum() throws Exception {
        return Arrays.asList(da.all().toArray());
    }

    public List<HashMap> listAll() throws Exception {

        return Arrays.asList(da.all().toArray());
    }

    public List<HashMap> order(String atribute, Integer type) throws Exception {
        return Arrays.asList(da.orderByAlbum(type, atribute).toArray());
    }

    public List<HashMap> search(String atribute, String text, Integer type) throws Exception {
        LinkedList<HashMap<String, String>> lista = da.search(atribute, text, type);
        if (!lista.isEmpty())
            return Arrays.asList(lista.toArray());
        else
            return new ArrayList<>();
    }

}