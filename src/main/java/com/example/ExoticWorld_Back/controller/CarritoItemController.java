package com.example.ExoticWorld_Back.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.ExoticWorld_Back.model.CarritoItemModel;
import com.example.ExoticWorld_Back.service.CarritoItemService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/carrito-items")
public class CarritoItemController {

    @Autowired
    private CarritoItemService carritoItemService;

    @GetMapping
    @Operation(summary = "Lista todos los items de carrito", description = "Obtiene todos los productos en carritos")
    public ResponseEntity<List<CarritoItemModel>> listar(){
        List<CarritoItemModel> items = carritoItemService.listar();
        return items.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un item de carrito por su id", description = "Busca un item mediante su identificador único")
    public ResponseEntity<CarritoItemModel> obtenerPorId(@PathVariable Integer id){
        CarritoItemModel item = carritoItemService.obtenerPorId(id);
        return item != null ? ResponseEntity.ok(item) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Agrega un producto al carrito", description = "Registra un nuevo item dentro del carrito")
    public ResponseEntity<CarritoItemModel> crear(@Valid @RequestBody CarritoItemModel item){
        CarritoItemModel creado = carritoItemService.guardar(item);
        return ResponseEntity.status(201).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un item del carrito", description = "Modifica un item mediante su identificador")
    public ResponseEntity<CarritoItemModel> actualizar(@PathVariable Integer id, @Valid @RequestBody CarritoItemModel item){
        CarritoItemModel actualizado = carritoItemService.actualizarTodo(id, item);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualiza parcialmente un item de carrito", description = "Permite modificar campos específicos del item")
    public ResponseEntity<CarritoItemModel> patch(@PathVariable Integer id, @RequestBody CarritoItemModel parcial){
        CarritoItemModel actualizado = carritoItemService.patchCarritoItem(id, parcial);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un item del carrito", description = "Elimina un item mediante su identificador")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        carritoItemService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
