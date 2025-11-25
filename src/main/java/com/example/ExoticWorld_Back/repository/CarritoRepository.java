package com.example.ExoticWorld_Back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.ExoticWorld_Back.model.CarritoModel;

@Repository
public interface CarritoRepository extends JpaRepository<CarritoModel, Integer> {

    CarritoModel findByUsuarioId(String usuarioId);
}

