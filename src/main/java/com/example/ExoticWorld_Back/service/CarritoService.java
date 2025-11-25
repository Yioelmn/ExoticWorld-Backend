package com.example.ExoticWorld_Back.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ExoticWorld_Back.model.CarritoItemModel;
import com.example.ExoticWorld_Back.model.CarritoModel;
import com.example.ExoticWorld_Back.model.ProductoModel;
import com.example.ExoticWorld_Back.repository.CarritoItemRepository;
import com.example.ExoticWorld_Back.repository.CarritoRepository;
import com.example.ExoticWorld_Back.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<CarritoModel> listar() {
        return carritoRepository.findAll();
    }

    public CarritoModel obtenerPorId(Integer id) {
        return carritoRepository.findById(id).orElse(null);
    }

    public CarritoModel guardar(CarritoModel carrito) {
        return carritoRepository.save(carrito);
    }

    public CarritoModel actualizarTodo(Integer id, CarritoModel carrito) {
        CarritoModel existente = carritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        existente.setUsuarioId(carrito.getUsuarioId());

        return carritoRepository.save(existente);
    }

    public CarritoModel patchCarrito(Integer id, CarritoModel parcial) {
        CarritoModel existente = obtenerPorId(id);
        if (existente != null) {
            if (parcial.getUsuarioId() != null && !parcial.getUsuarioId().isBlank()) {
                existente.setUsuarioId(parcial.getUsuarioId());
            }
            return guardar(existente);
        }
        return null;
    }

    public void eliminar(Integer id) {
        CarritoModel carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        carritoRepository.delete(carrito);
    }

    // sacar si no me funcina
    public CarritoModel obtenerOCrearPorUsuario(String usuarioId) {
        CarritoModel carrito = carritoRepository.findByUsuarioId(usuarioId);
        if (carrito == null) {
            carrito = new CarritoModel();
            carrito.setUsuarioId(usuarioId);
            carrito = carritoRepository.save(carrito);
        }
        return carrito;
    }

    public List<CarritoItemModel> listarItemsPorUsuario(String usuarioId) {
        CarritoModel carrito = obtenerOCrearPorUsuario(usuarioId);
        return carritoItemRepository.findByCarrito(carrito);
    }

    public CarritoItemModel agregarProducto(String usuarioId, Integer productoId, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            cantidad = 1;
        }

        CarritoModel carrito = obtenerOCrearPorUsuario(usuarioId);

        ProductoModel producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        CarritoItemModel item = carritoItemRepository
                .findByCarritoAndProducto(carrito, producto)
                .orElse(null);

        if (item == null) {
            item = new CarritoItemModel();
            item.setCarrito(carrito);
            item.setProducto(producto);
            item.setCantidad(cantidad);
        } else {
            item.setCantidad(item.getCantidad() + cantidad);
        }

        return carritoItemRepository.save(item);
    }

    public CarritoItemModel actualizarCantidad(String usuarioId, Integer productoId, Integer cantidad) {
        CarritoModel carrito = obtenerOCrearPorUsuario(usuarioId);

        ProductoModel producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        CarritoItemModel item = carritoItemRepository
                .findByCarritoAndProducto(carrito, producto)
                .orElseThrow(() -> new RuntimeException("Item no encontrado en el carrito"));

        if (cantidad == null || cantidad <= 0) {
            carritoItemRepository.delete(item);
            return null;
        }

        item.setCantidad(cantidad);
        return carritoItemRepository.save(item);
    }

    public void decrementarProducto(String usuarioId, Integer productoId) {
        CarritoModel carrito = obtenerOCrearPorUsuario(usuarioId);

        ProductoModel producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        CarritoItemModel item = carritoItemRepository
                .findByCarritoAndProducto(carrito, producto)
                .orElse(null);

        if (item == null) {
            return;
        }

        int nuevaCantidad = item.getCantidad() - 1;
        if (nuevaCantidad <= 0) {
            carritoItemRepository.delete(item);
        } else {
            item.setCantidad(nuevaCantidad);
            carritoItemRepository.save(item);
        }
    }

    public void eliminarItemPorProducto(String usuarioId, Integer productoId) {
        CarritoModel carrito = obtenerOCrearPorUsuario(usuarioId);

        ProductoModel producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        CarritoItemModel item = carritoItemRepository
                .findByCarritoAndProducto(carrito, producto)
                .orElse(null);

        if (item != null) {
            carritoItemRepository.delete(item);
        }
    }

    public void vaciarCarrito(String usuarioId) {
        CarritoModel carrito = obtenerOCrearPorUsuario(usuarioId);
        List<CarritoItemModel> items = carritoItemRepository.findByCarrito(carrito);
        carritoItemRepository.deleteAll(items);
    }

    public Double calcularTotal(String usuarioId) {
        CarritoModel carrito = obtenerOCrearPorUsuario(usuarioId);
        List<CarritoItemModel> items = carritoItemRepository.findByCarrito(carrito);

        return items.stream()
                .mapToDouble(i -> i.getCantidad() * i.getProducto().getPrecioProducto())
                .sum();
    }
}
