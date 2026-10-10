package org.educa.dao;

import generated.Productos;
import jakarta.xml.bind.JAXBException;
import org.apache.poi.ss.usermodel.Workbook;
import org.educa.entity.ProductoEntity;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ProductoDAO {
    Productos readXML(File file) throws JAXBException;
    void writeSummaryFile(String path, String fecha, int numeroProductos, double beneficioTotal, File file, String fichSinExtension) throws IOException;
    void writeExcelFile(String path, String fecha, Workbook workbook) throws IOException;}
