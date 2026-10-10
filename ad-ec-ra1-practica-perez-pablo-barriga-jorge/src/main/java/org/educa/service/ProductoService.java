package org.educa.service;

import generated.Producto;
import generated.Productos;
import jakarta.xml.bind.JAXBException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.educa.dao.ProductoDAO;
import org.educa.dao.ProductoDAOimpl;
import org.educa.entity.ProductoEntity;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductoService {

    private final ProductoDAO productoDAO = new ProductoDAOimpl();

    public List<ProductoEntity> readFile(String fileXml) throws JAXBException {
        File file = new File(fileXml);
        Productos productos = productoDAO.readXML(file);
        List<ProductoEntity> listaEntities = new ArrayList<>();

        if (productos != null && productos.getProducto() != null) {
            for (Producto p : productos.getProducto()) {
                ProductoEntity entity = new ProductoEntity();
                entity.setProducto(p);

                double precioFinal = p.getPrecio().doubleValue() - (p.getPrecio().doubleValue() * (p.getDescuento().doubleValue() / 100));
                entity.setPrecioFinal(BigDecimal.valueOf(precioFinal));

                double coste = p.getPrecio().doubleValue() + p.getCostes().getCostesAlmacenaje().doubleValue() + p.getCostes().getCostesEnvio().doubleValue();
                entity.setCost(BigDecimal.valueOf(coste));

                double beneficio =  coste - precioFinal;
                entity.setProfit(BigDecimal.valueOf(beneficio));

                listaEntities.add(entity);
            }
        }

        return listaEntities;    }
    public void exportSummary(String path, String fileXml) throws JAXBException, IOException {
        File file = new File(fileXml);
        Productos productos = productoDAO.readXML(file);

        String fichExtension = file.getName();
        String fichSinExtension = fichExtension.substring(0, fichExtension.lastIndexOf("."));
        String fecha = fichSinExtension.substring(fichSinExtension.indexOf('_') + 1);

        int numeroProductos = 0;
        double beneficioTotal = 0;

        if (productos != null && productos.getProducto() != null) {
            numeroProductos = productos.getProducto().size();
            for (Producto p : productos.getProducto()) {
                if (p.getPrecio() != null) {
                    double precioFinal = p.getPrecio().doubleValue() - (p.getPrecio().doubleValue() * (p.getDescuento().doubleValue() / 100));
                    double coste = p.getPrecio().doubleValue() + p.getCostes().getCostesAlmacenaje().doubleValue() + p.getCostes().getCostesEnvio().doubleValue();
                    double beneficio = coste - precioFinal;
                    beneficioTotal += beneficio;
                }
            }
        }

        productoDAO.writeSummaryFile(path, fecha, numeroProductos, beneficioTotal, file, fichSinExtension);
    }

    public void exportExcel(String path, String fileXml) throws JAXBException, IOException, ParseException {
        File file = new File(fileXml);
        Productos productos = productoDAO.readXML(file);

        String fichExtension = file.getName();
        String fichSinExtension = fichExtension.substring(0, fichExtension.lastIndexOf('.'));
        String fecha = fichSinExtension.substring(fichSinExtension.indexOf('_') + 1);

        SimpleDateFormat sdf = new SimpleDateFormat("MMMMyyyy", new Locale("es", "ES"));
        sdf.parse(fecha);

        // Construcción del libro Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Productos");

        CellStyle estiloCabecera = workbook.createCellStyle();
        Font fuenteCabecera = workbook.createFont();
        fuenteCabecera.setBold(true);
        estiloCabecera.setFont(fuenteCabecera);
        estiloCabecera.setAlignment(HorizontalAlignment.CENTER);
        estiloCabecera.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        estiloCabecera.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle estiloPar = workbook.createCellStyle();
        estiloPar.setAlignment(HorizontalAlignment.LEFT);

        CellStyle estiloImpar = workbook.createCellStyle();
        estiloImpar.setAlignment(HorizontalAlignment.LEFT);
        estiloImpar.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        estiloImpar.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row filaCabecera = sheet.createRow(0);
        String[] columns = {"Código", "Marca", "Modelo", "Categoría", "Año", "Garantía", "Precio"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = filaCabecera.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(estiloCabecera);
        }

        int numeroFila = 1;
        if (productos != null && productos.getProducto() != null) {
            for (Producto p : productos.getProducto()) {
                Row fila = sheet.createRow(numeroFila);
                CellStyle estiloActual = (numeroFila % 2 == 0) ? estiloPar : estiloImpar;

                fila.createCell(0).setCellValue(p.getCodigo());
                fila.createCell(1).setCellValue(p.getMarca());
                fila.createCell(2).setCellValue(p.getModelo());
                fila.createCell(3).setCellValue(p.getCategoria());
                fila.createCell(4).setCellValue(p.getAnioLanzamiento());
                fila.createCell(5).setCellValue(p.getGarantiaMeses());
                fila.createCell(6).setCellValue(p.getPrecio().doubleValue());

                for (int i = 0; i < 7; i++) {
                    fila.getCell(i).setCellStyle(estiloActual);
                }

                numeroFila++;
            }
        }
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        productoDAO.writeExcelFile(path, fecha, workbook);
    }
}
