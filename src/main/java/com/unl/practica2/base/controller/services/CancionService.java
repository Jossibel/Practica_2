package com.unl.practica2.base.controller.services;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import com.unl.practica2.base.controller.DataStruct.list.LinkedList;
import com.unl.practica2.base.controller.dao.dao_models.DaoAlbum;
import com.unl.practica2.base.controller.dao.dao_models.DaoCancion;
import com.unl.practica2.base.controller.dao.dao_models.DaoGenero;
import com.unl.practica2.base.models.Album;
import com.unl.practica2.base.models.Cancion;
import com.unl.practica2.base.models.TipoArchivoEnum;
import com.unl.practica2.base.models.Genero;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotEmpty;

@BrowserCallable
@AnonymousAllowed
public class CancionService {
    private DaoCancion dc;

    public CancionService() {
        dc = new DaoCancion();
    }

    public void createCancion(@NotEmpty String nombre, Integer id_genero, Integer duracion, 
                            @NotEmpty String url, @NotEmpty String tipo, Integer id_album) throws Exception {
        if (nombre.trim().length() > 0 && url.trim().length() > 0 && tipo.trim().length() > 0 
            && id_genero > 0 && duracion > 0 && id_album > 0) {
            dc.getObj().setNombre(nombre);
            dc.getObj().setId_album(id_album); 
            dc.getObj().setId_genero(id_genero);
            dc.getObj().setDuracion(duracion);
            dc.getObj().setUrl(url);
            dc.getObj().setTipo(TipoArchivoEnum.valueOf(tipo));
            if (!dc.save())
                throw new Exception("No se pudo guardar los datos de la cancion");
        }
    }

    public void updateCancion(Integer id, @NotEmpty String nombre,Integer id_genero, Integer duracion, @NotEmpty String url, @NotEmpty String tipo, Integer id_album) throws Exception {
        if (nombre.trim().length() > 0 && url.trim().length() > 0 && tipo.trim().length() > 0 && id_genero > 0 && duracion > 0 && id_album > 0) {
            dc.setObj(dc.listAll().get(id -1));
            dc.getObj().setNombre(nombre);
            dc.getObj().setId_album(id_album); 
            dc.getObj().setId_genero(id_genero);
            dc.getObj().setDuracion(duracion);
            dc.getObj().setTipo(TipoArchivoEnum.valueOf(tipo));
            dc.getObj().setUrl(url);

            if (!dc.update(id -1))
                throw new Exception("No se pudo guardar los datos de la cancion");
        }
    }

    public List<HashMap<String, String>> listAlbumCombo() {
        List<HashMap<String, String>> lista = new ArrayList<>();
        DaoAlbum dc = new DaoAlbum();

        if(!dc.listAll().isEmpty()) {
            Album[] arreglo = dc.listAll().toArray();
            for(int i = 0; i < arreglo.length; i++) {
                HashMap<String, String> aux = new HashMap<>();
                aux.put("value", arreglo[i].getId().toString());
                aux.put("label", arreglo[i].getNombre());
                lista.add(aux);
            }
        }
        return lista;
    }

    public List<HashMap<String, String>> listaAlbumGenero() {
        List<HashMap<String, String>> lista = new ArrayList<>();
        DaoGenero dg = new DaoGenero();
        if(!dg.listAll().isEmpty()) {
            Genero[] arreglo = dg.listAll().toArray();
            for(int i = 0; i < arreglo.length; i++) {
                HashMap<String, String> aux = new HashMap<>();
                aux.put("value", arreglo[i].getId().toString());
                aux.put("label", arreglo[i].getNombre());
                lista.add(aux);
            }
        }
        return lista;
    }

    public List<HashMap<String, String>> listCancion() {
        List<HashMap<String, String>> lista = new ArrayList<>();
        if(!dc.listAll().isEmpty()) {
            Cancion[] arreglo = dc.listAll().toArray();
            for(int i = 0; i < arreglo.length; i++) {
                HashMap<String, String> aux = new HashMap<>();
                aux.put("id", arreglo[i].getId().toString());
                aux.put("nombre", arreglo[i].getNombre());
                aux.put("url", arreglo[i].getUrl());
                aux.put("duracion", arreglo[i].getDuracion().toString());
                aux.put("genero", new DaoGenero().listAll().get(arreglo[i].getId_genero() - 1).getNombre());
                aux.put("id_genero", new DaoGenero().listAll().get(arreglo[i].getId_genero() - 1).getId().toString());
                aux.put("album", new DaoAlbum().listAll().get(arreglo[i].getId_album() - 1).getNombre());
                aux.put("id_album", new DaoAlbum().listAll().get(arreglo[i].getId_album() - 1).getId().toString());
                aux.put("tipo", arreglo[i].getTipo().toString());
                lista.add(aux);
            }
        }
        return lista;
    }

    public List<String> listTipo() {
        List<String> lista = new ArrayList<>();
        for(TipoArchivoEnum r: TipoArchivoEnum.values()) {
            lista.add(r.toString());
        }        
        return lista;
    }

    public List<HashMap<String, String>> listAll() throws Exception {
        return Arrays.asList(dc.all().toArray());
    }

    public List<HashMap<String, String>> order(String attribute, Integer type) throws Exception {
        return Arrays.asList(dc.orderQuickCancion(type, attribute).toArray());
    }

    public List<HashMap<String, String>> search(String attribute, String text, Integer type) throws Exception {
        LinkedList<HashMap<String, String>> lista = dc.search(attribute, text, type);
        if(!lista.isEmpty())
            return Arrays.asList(lista.toArray());
        else
            return new ArrayList<>();
    }
}