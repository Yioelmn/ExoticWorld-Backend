package com.example.ExoticWorld_Back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ExoticWorld_Back.model.ProductoModel;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoModel, Integer>{

    List<ProductoModel> findByNombreProductoContainingIgnoreCase(String nombreProducto);

}
