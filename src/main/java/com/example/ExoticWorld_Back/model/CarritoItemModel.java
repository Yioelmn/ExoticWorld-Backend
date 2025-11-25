package com.example.ExoticWorld_Back.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carrito_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer item_id;

    @NotNull(message = "La cantidad no puede estar vacía")
    @Column(nullable = false)
    private Integer cantidad;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    private CarritoModel carrito;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductoModel producto;
}
