package org.educa.dao;

import generated.Producto;
import generated.Productos;
import jakarta.xml.bind.JAXB;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.educa.entity.ProductoEntity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOimpl implements ProductoDAO {
    @Override
    public List<ProductoEntity> readXML(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Productos.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Productos productos = (Productos) unmarshaller.unmarshal(file);
        List<ProductoEntity> listaEntities = new ArrayList<>();

        if (productos != null && productos.getProducto() != null) {
            for (Producto p : productos.getProducto()) {
                ProductoEntity entity = new ProductoEntity();
                entity.setProducto(p);
                double precioFinal = p.getPrecio().intValue()-(p.getPrecio().intValue()*((double) p.getDescuento().intValue() /100));
                entity.setPrecioFinal(BigDecimal.valueOf(precioFinal));
                double coste= p.getPrecio().intValue()+p.getCostes().getCostesAlmacenaje().intValue()+p.getCostes().getCostesEnvio().intValue();
                entity.setCost(BigDecimal.valueOf(coste));
                double beneficio = entity.getCost().intValue()+entity.getPrecioFinal().intValue();
                entity.setProfit(BigDecimal.valueOf(beneficio));
                listaEntities.add(entity);
            }
        }

        return listaEntities;
    }

    @Override
    public void crearFichero(String path, File file) throws JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Productos.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Productos productos = (Productos) unmarshaller.unmarshal(file);

        String fichExtension = file.getName();
        String fichSinExtension = fichExtension.substring(0, fichExtension.lastIndexOf("."));

        String fecha = fichSinExtension.substring(fichSinExtension.indexOf('_') + 1);
        int numeroProductos = 0;
        double beneficioTotal = 0;

        if (productos != null && productos.getProducto() != null) {
            numeroProductos = productos.getProducto().size();
            for (Producto p : productos.getProducto()) {
                if (p.getPrecio() != null) {
                    double precioFinal= p.getPrecio().intValue()-(p.getPrecio().intValue()*((double) p.getDescuento().intValue() /100));
                    double coste= p.getPrecio().intValue()+p.getCostes().getCostesAlmacenaje().intValue()+p.getCostes().getCostesEnvio().intValue();
                    double beneficio = coste-precioFinal;
                     beneficioTotal+= beneficio;
                }
            }
        }
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

}



