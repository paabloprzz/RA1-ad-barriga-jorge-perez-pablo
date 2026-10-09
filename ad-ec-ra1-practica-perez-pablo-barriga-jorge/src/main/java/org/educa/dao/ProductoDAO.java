package org.educa.dao;

import jakarta.xml.bind.JAXBException;
import org.educa.entity.ProductoEntity;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ProductoDAO {
    List<ProductoEntity> readXML(File file) throws JAXBException;

    void crearFichero(String path, File file) throws JAXBException, IOException;
}
