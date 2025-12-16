package com.example.ExoticWorld_Back.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "producto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer producto_id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Column(length = 35, nullable = false)
    private String nombreProducto;

    @NotBlank(message = "La descripcion del producto no puede estar vacía")
    @Column(length = 100, nullable = false)
    private String descripcionProducto;

    @NotNull(message = "El precio no puede estar vacío")
    @Column(nullable = false)
    private Integer precioProducto;

    @NotNull(message = "La categoría no puede estar vacía")
    @Column(name = "categoria_id", nullable = false)
    private Integer categoriaId;
}
