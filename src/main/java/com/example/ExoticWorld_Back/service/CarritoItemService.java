package com.example.ExoticWorld_Back.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ExoticWorld_Back.model.CarritoItemModel;
import com.example.ExoticWorld_Back.repository.CarritoItemRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CarritoItemService {

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    public List<CarritoItemModel> listar(){
        return carritoItemRepository.findAll();
    }

    public CarritoItemModel obtenerPorId(Integer id){
        return carritoItemRepository.findById(id).orElse(null);
    }

    public CarritoItemModel guardar(CarritoItemModel item){
        return carritoItemRepository.save(item);
    }

    public CarritoItemModel actualizarTodo(Integer id, CarritoItemModel item){
        CarritoItemModel existente = carritoItemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ítem de carrito no encontrado"));

        existente.setCantidad(item.getCantidad());
        existente.setProducto(item.getProducto());
        existente.setCarrito(item.getCarrito());

        return carritoItemRepository.save(existente);
    }

    public void eliminar(Integer id){
        CarritoItemModel existente = carritoItemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ítem de carrito no encontrado"));
        carritoItemRepository.delete(existente);
    }

    public CarritoItemModel patchCarritoItem(Integer id, CarritoItemModel parcial){
        CarritoItemModel existente = obtenerPorId(id);
        if(existente != null){
            if(parcial.getCantidad() != null)
                existente.setCantidad(parcial.getCantidad());
            if(parcial.getProducto() != null)
                existente.setProducto(parcial.getProducto());
            if(parcial.getCarrito() != null)
                existente.setCarrito(parcial.getCarrito());

            return guardar(existente);
        }
        return null;
    }
}
