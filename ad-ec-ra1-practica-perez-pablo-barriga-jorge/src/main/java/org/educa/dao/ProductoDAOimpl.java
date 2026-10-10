package org.educa.dao;

import generated.Producto;
import generated.Productos;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.educa.entity.ProductoEntity;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductoDAOimpl implements ProductoDAO {

    @Override
    public Productos readXML(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Productos.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (Productos) unmarshaller.unmarshal(file);
    }

    @Override
    public void writeSummaryFile(String path, String fecha, int numeroProductos, double beneficioTotal, File file, String fichSinExtension) throws IOException {
        File outputDir = new File(path);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        File outputFile = new File(outputDir, "result_" + fecha + ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write("Fecha: " + fecha);
        writer.newLine();
        writer.write("NumeroDeProductos: " + numeroProductos);
        writer.newLine();
        writer.write("BeneficioTotal: " + beneficioTotal);
        writer.newLine();
        writer.write("Ruta del fichero: " + file.getAbsolutePath());
        writer.newLine();
        writer.write("Nombre del fichero: " + fichSinExtension);
        writer.newLine();
        writer.write("Tamaño del fichero: " + file.length() + " bytes");
        writer.close();
    }
    @Override
    public void writeExcelFile(String path, String fecha, Workbook workbook) throws IOException {

    }
}