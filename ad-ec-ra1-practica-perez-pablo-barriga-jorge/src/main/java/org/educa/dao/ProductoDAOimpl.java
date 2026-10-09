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
                BigDecimal precioFinal = p.getPrecio().multiply(BigDecimal.valueOf(0.845));
                entity.setPrecioFinal(precioFinal);
                BigDecimal coste = p.getPrecio().add(p.getCostes().getCostesAlmacenaje().add(p.getCostes().getCostesEnvio()));
                entity.setCost(coste);
                BigDecimal beneficio = entity.getCost().subtract(entity.getPrecioFinal());
                entity.setProfit(beneficio);
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
        BigDecimal beneficioTotal = BigDecimal.ZERO;

        if (productos != null && productos.getProducto() != null) {
            numeroProductos = productos.getProducto().size();
            for (Producto p : productos.getProducto()) {
                if (p.getPrecio() != null) {
                    BigDecimal precioFinal = p.getPrecio().multiply(BigDecimal.valueOf(0.845));
                    BigDecimal coste = p.getPrecio().add(
                            p.getCostes().getCostesAlmacenaje().add(p.getCostes().getCostesEnvio()));
                    BigDecimal beneficio = precioFinal.subtract(coste);
                    beneficioTotal = beneficioTotal.add(beneficio);
                }
            }
        }
        //Falta crear el fichero y escribirlo

    }

}



