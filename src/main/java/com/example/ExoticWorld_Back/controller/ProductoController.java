package com.example.ExoticWorld_Back.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ExoticWorld_Back.model.ProductoModel;
import com.example.ExoticWorld_Back.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary = "Lista todos los productos", description = "Obtiene todos los productos disponibles")
    public ResponseEntity<List<ProductoModel>> listar() {
        List<ProductoModel> productos = productoService.listar();
        return productos.isEmpty() ? ResponseEntity.noContent().build(): ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lista productos por su id", description = "busca productos por su identificador unico")
    public ResponseEntity<ProductoModel> obtenerPorId(@PathVariable Integer id) {
        ProductoModel producto = productoService.obtenerPorId(id);
        return producto != null ? ResponseEntity.ok(producto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Crea un producto", description = "registra productos nuevos")
    public ResponseEntity<ProductoModel> crear(@Valid @RequestBody ProductoModel producto) {
        ProductoModel creado = productoService.guardar(producto);
        return ResponseEntity.status(201).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un producto", description = "Modifica productos especificos mediante su identificador unico")
    public ResponseEntity<ProductoModel> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ProductoModel producto) {

        ProductoModel actualizado = productoService.actualizarTodo(id, producto);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualiza productos parcialmente", description = "Modifica productos parcialmente mediante su identificador unico")
    public ResponseEntity<ProductoModel> patch(
            @PathVariable Integer id,
            @RequestBody ProductoModel producto) {

        ProductoModel actualizado = productoService.patchProducto(id, producto);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un producto", description = "Elimina un producto en especifico mediante su identificador unico")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        ProductoModel producto = productoService.obtenerPorId(id);
        if (producto == null) return ResponseEntity.notFound().build();
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar/nombre")
    @Operation(summary = "Busca productos por nombre", description = "Obtiene productos mediante su nombre")
    public ResponseEntity<List<ProductoModel>> buscarPorNombre(@RequestParam String nombre) {
        List<ProductoModel> productos = productoService.buscarPorNombre(nombre);
        return productos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(productos);
    }
}
