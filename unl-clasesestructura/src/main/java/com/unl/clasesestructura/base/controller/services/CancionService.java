package com.unl.clasesestructura.base.controller.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.unl.clasesestructura.base.controller.dao.dao_models.DaoAlbum;
import com.unl.clasesestructura.base.controller.dao.dao_models.DaoCancion;
import com.unl.clasesestructura.base.controller.dao.dao_models.DaoGenero;
import com.unl.clasesestructura.base.controller.data_structure.LinkedList;
import com.unl.clasesestructura.base.models.Album;
import com.unl.clasesestructura.base.models.Genero;
import com.unl.clasesestructura.base.models.TipoArchivoEnum;
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
public class CancionService {

    private DaoCancion dc; // DAO para operaciones de base de datos de Canciones

    /**
     * Constructor: Inicializa el DAO de Canciones.
     */
    public CancionService() {
        dc = new DaoCancion();
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
    public void createCancion(String nombre, String duracion, String url, TipoArchivoEnum tipo, Integer idGenero)
            throws Exception {
        // Validación básica de campos obligatorios
        if (nombre.trim().length() > 0 && duracion.trim().length() > 0 && url.trim().length() > 0 && tipo != null
                && idGenero != null) {
            // Asigna los valores al objeto Canción
            dc.getObj().setNombre(nombre);
            dc.getObj().setDuracion(Integer.parseInt(duracion));
            dc.getObj().setUrl(url);
            dc.getObj().setTipo(tipo);
            dc.getObj().setId_genero(idGenero);

            // Intenta guardar la canción
            if (!dc.save()) {
                throw new Exception("Error al guardar la canción");
            }
        }
    }

    /**
     * Actualiza una canción existente.
     * 
     * @param id       ID de la canción a actualizar.
     * @param nombre   Nuevo nombre (no vacío).
     * @param duracion Nueva duración (cadena numérica).
     * @param url      Nueva URL.
     * @param tipo     Nuevo tipo de archivo.
     * @param idGenero ID del nuevo género.
     * @param idAlbum  ID del nuevo álbum.
     * @throws Exception Si hay errores de validación o al actualizar.
     */
    public void updateCancion(Integer id, @NotEmpty @NotBlank @NonNull String nombre,
            @NotEmpty @NotBlank @NonNull String duracion, @NotEmpty @NotBlank @NonNull String url,
            TipoArchivoEnum tipo, Integer idGenero, Integer idAlbum) throws Exception {
        // Validación de campos y ID
        if (id != null && id > 0 && nombre.trim().length() > 0 && duracion.trim().length() > 0
                && url.trim().length() > 0 && tipo != null && idGenero != null && idAlbum != null) {
            // Obtiene la canción por ID y actualiza sus datos
            dc.setObj(dc.listAll().get(id - 1));
            dc.getObj().setNombre(nombre);
            dc.getObj().setDuracion(Integer.parseInt(duracion));
            dc.getObj().setUrl(url);
            dc.getObj().setTipo(tipo);
            dc.getObj().setId_genero(idGenero);
            dc.getObj().setId_album(idAlbum);

            // Intenta aplicar la actualización
            if (!dc.update(id - 1)) {
                throw new Exception("Error al actualizar la canción");
            }
        }
    }

    /**
     * Obtiene una lista de álbumes en formato {id, label} para combos/selectores.
     * 
     * @return Lista de HashMap con IDs y nombres de álbumes.
     */
    public List<HashMap> ListaAlbumCombo() {
        List<HashMap> lista = new ArrayList<>();
        DaoAlbum da = new DaoAlbum(); // DAO para álbumes

        if (!da.listAll().isEmpty()) {
            // Convierte la lista de álbumes a un arreglo
            Album[] arreglo = da.listAll().toArray();

            // Formatea cada álbum como {id, label} para frontend
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
     * Obtiene una lista de géneros en formato {id, label} para combos/selectores.
     * 
     * @return Lista de HashMap con IDs y nombres de géneros.
     */
    public List<HashMap> listaAlbumGenero() {
        List<HashMap> lista = new ArrayList<>();
        DaoGenero dg = new DaoGenero(); // DAO para géneros

        if (!dg.listAll().isEmpty()) {
            Genero[] arreglo = dg.listAll().toArray();

            // Formatea cada género como {id, label}
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
     * @return Lista de HashMap con todos los datos de canciones.
     */
    public List<HashMap> listCancion() throws Exception {
        return Arrays.asList(dc.all().toArray());
    }

    public List<HashMap> listAll() throws Exception {

        return Arrays.asList(dc.all().toArray());
    }

    public List<HashMap> order(String atribute, Integer type) throws Exception {
        return Arrays.asList(dc.orderByCancion(type, atribute).toArray());
    }

    public List<HashMap> search(String atribute, String text, Integer type) throws Exception {
        LinkedList<HashMap<String, String>> lista = dc.search(atribute, text, type);
        if (!lista.isEmpty())
            return Arrays.asList(lista.toArray());
        else
            return new ArrayList<>();
    }

    /**
     * Obtiene todas las canciones en formato de lista de objetos Cancion.
     * 
     * @return Lista de canciones.
     */
    /*
     * public List<Cancion> listAllCancion() {
     * return Arrays.asList(dc.listAll().toArray());
     * }
     */
    /**
     * Obtiene los tipos de archivo disponibles (valores del enum TipoArchivoEnum).
     * 
     * @return Lista de nombres de tipos de archivo (ej: "MP3", "WAV").
     */
    public List<String> listTipoArchivo() {
        List<String> lista = new ArrayList<>();
        for (TipoArchivoEnum tipo : TipoArchivoEnum.values()) {
            lista.add(tipo.name()); // Agrega cada valor del enum
        }
        return lista;
    }

}