package org.educa.dao;

import generated.Producto;
import generated.Productos;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.educa.entity.ProductoEntity;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOimpl implements ProductoDAO {
    @Override
    public List<ProductoEntity> readXML(File file) throws JAXBException {
        JAXBContext jaxbContext= JAXBContext.newInstance(Productos.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Productos productos = (Productos) unmarshaller.unmarshal(file);
        List<ProductoEntity> listaEntities = new ArrayList<>();

        if (productos != null && productos.getProducto() != null) {
            for (Producto p : productos.getProducto()) {
                ProductoEntity entity = new ProductoEntity();
                entity.setProducto(p);
                //Falta añadir Precio final, coste y beneficio
                listaEntities.add(entity);
            }
        }

        return listaEntities;
    }
}
