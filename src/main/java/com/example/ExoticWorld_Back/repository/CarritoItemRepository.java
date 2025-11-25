package com.example.ExoticWorld_Back.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ExoticWorld_Back.model.CarritoItemModel;
import com.example.ExoticWorld_Back.model.CarritoModel;
import com.example.ExoticWorld_Back.model.ProductoModel;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItemModel, Integer> {

    List<CarritoItemModel> findByCarrito(CarritoModel carrito);

    List<CarritoItemModel> findByProducto(ProductoModel producto);

    Optional<CarritoItemModel> findByCarritoAndProducto(CarritoModel carrito, ProductoModel producto);
}




