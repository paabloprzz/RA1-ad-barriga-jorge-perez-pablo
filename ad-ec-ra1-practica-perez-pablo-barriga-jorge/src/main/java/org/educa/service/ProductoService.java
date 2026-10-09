package org.educa.service;

import jakarta.xml.bind.JAXBException;
import org.educa.dao.ProductoDAO;
import org.educa.dao.ProductoDAOimpl;
import org.educa.entity.ProductoEntity;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;



public class ProductoService {
    private final ProductoDAO productoDAO=new ProductoDAOimpl();
    public List<ProductoEntity> readFile(String fileXml) throws JAXBException {
        return productoDAO.readXML(new File(fileXml));
    }

    public void exportSummary(String path, String fileXml) throws JAXBException, IOException {
        //TODO: Implementar
        productoDAO.crearFichero(path,new File(fileXml));

    }

    public void exportExcel(String path, String fileXml) throws JAXBException, IOException, ParseException {
        //TODO: Implementar
    }
}
