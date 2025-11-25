package com.example.ExoticWorld_Back.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ExoticWorld_Back.model.ProductoModel;
import com.example.ExoticWorld_Back.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<ProductoModel> listar(){
        return productoRepository.findAll();
    }

    public ProductoModel obtenerPorId (Integer id){
        return productoRepository.findById(id).orElse(null);
    }

    public ProductoModel guardar(ProductoModel producto){
        return productoRepository.save(producto);
    }

    public ProductoModel actualizarTodo(Integer id, ProductoModel producto){
        ProductoModel existente = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
            existente.setNombreProducto(producto.getNombreProducto());
            existente.setDescripcionProducto(producto.getDescripcionProducto());
            existente.setPrecioProducto(producto.getPrecioProducto());

            return productoRepository.save(existente);
    }

    public void eliminar(Integer id){
        ProductoModel producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        productoRepository.delete(producto);
    }
    
    public ProductoModel patchProducto(Integer id, ProductoModel productoParcial){
        ProductoModel existente = obtenerPorId(id);
        if(existente != null) {
            if(productoParcial.getNombreProducto() != null)
                existente.setNombreProducto(productoParcial.getNombreProducto());
            if(productoParcial.getDescripcionProducto() != null)
                existente.setDescripcionProducto(productoParcial.getDescripcionProducto());
            if(productoParcial.getPrecioProducto() != null)
                existente.setPrecioProducto(productoParcial.getPrecioProducto());
            return guardar(existente);
        }
        return null;
    }

    public List<ProductoModel> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreProductoContainingIgnoreCase(nombre);
    }
}
