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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.ExoticWorld_Back.model.CarritoItemModel;
import com.example.ExoticWorld_Back.model.CarritoModel;
import com.example.ExoticWorld_Back.service.CarritoService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    @Operation(summary = "Lista todos los carritos", description = "Obtiene todos los carritos registrados")
    public ResponseEntity<List<CarritoModel>> listar() {
        List<CarritoModel> carritos = carritoService.listar();
        return carritos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(carritos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un carrito por su id", description = "Busca un carrito mediante su identificador único")
    public ResponseEntity<CarritoModel> obtenerPorId(@PathVariable Integer id) {
        CarritoModel carrito = carritoService.obtenerPorId(id);
        return carrito != null ? ResponseEntity.ok(carrito) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Crea un carrito", description = "Registra un nuevo carrito")
    public ResponseEntity<CarritoModel> crear(@Valid @RequestBody CarritoModel carrito) {
        CarritoModel creado = carritoService.guardar(carrito);
        return ResponseEntity.status(201).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un carrito", description = "Modifica completamente un carrito existente")
    public ResponseEntity<CarritoModel> actualizar(@PathVariable Integer id, @Valid @RequestBody CarritoModel carrito) {
        CarritoModel actualizado = carritoService.actualizarTodo(id, carrito);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualiza parcialmente un carrito", description = "Modifica parcialmente un carrito existente")
    public ResponseEntity<CarritoModel> patch(@PathVariable Integer id, @RequestBody CarritoModel carrito) {
        CarritoModel actualizado = carritoService.patchCarrito(id, carrito);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un carrito", description = "Elimina un carrito mediante su identificador único")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        CarritoModel carrito = carritoService.obtenerPorId(id);
        if (carrito == null) return ResponseEntity.notFound().build();
        carritoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtiene el carrito del usuario", description = "Si el carrito no existe, lo crea vacío")
    public ResponseEntity<CarritoModel> obtenerOCrearPorUsuario(@PathVariable String usuarioId) {
        CarritoModel carrito = carritoService.obtenerOCrearPorUsuario(usuarioId);
        return ResponseEntity.ok(carrito);
    }

    @GetMapping("/usuario/{usuarioId}/items")
    @Operation(summary = "Lista los items del carrito de un usuario", description = "Devuelve todos los productos dentro del carrito")
    public ResponseEntity<List<CarritoItemModel>> listarItems(@PathVariable String usuarioId) {
        List<CarritoItemModel> items = carritoService.listarItemsPorUsuario(usuarioId);
        return items.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(items);
    }

    @PostMapping("/usuario/{usuarioId}/agregar")
    @Operation(summary = "Agrega un producto al carrito", description = "Si el producto ya existe en el carrito, aumenta su cantidad")
    public ResponseEntity<CarritoItemModel> agregarProducto(
            @PathVariable String usuarioId,
            @RequestParam Integer productoId,
            @RequestParam(required = false, defaultValue = "1") Integer cantidad) {

        CarritoItemModel item = carritoService.agregarProducto(usuarioId, productoId, cantidad);
        return ResponseEntity.status(201).body(item);
    }

    @PutMapping("/usuario/{usuarioId}/actualizar-cantidad")
    @Operation(summary = "Actualiza la cantidad de un producto en el carrito",
            description = "Si la cantidad es 0 o menor, el item se elimina")
    public ResponseEntity<CarritoItemModel> actualizarCantidad(
            @PathVariable String usuarioId,
            @RequestParam Integer productoId,
            @RequestParam Integer cantidad) {

        CarritoItemModel item = carritoService.actualizarCantidad(usuarioId, productoId, cantidad);
        if (item == null && cantidad != null && cantidad <= 0) {
            return ResponseEntity.noContent().build();
        }
        return item != null ? ResponseEntity.ok(item) : ResponseEntity.notFound().build();
    }

    @PostMapping("/usuario/{usuarioId}/decrementar")
    @Operation(summary = "Disminuye en 1 la cantidad de un producto",
            description = "Si la cantidad llega a 0, el item se elimina")
    public ResponseEntity<Void> decrementarProducto(
            @PathVariable String usuarioId,
            @RequestParam Integer productoId) {

        carritoService.decrementarProducto(usuarioId, productoId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/usuario/{usuarioId}/eliminar-item")
    @Operation(summary = "Elimina un producto del carrito", description = "Quita completamente el producto del carrito")
    public ResponseEntity<Void> eliminarItem(
            @PathVariable String usuarioId,
            @RequestParam Integer productoId) {

        carritoService.eliminarItemPorProducto(usuarioId, productoId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/usuario/{usuarioId}/vaciar")
    @Operation(summary = "Vacía el carrito del usuario", description = "Elimina todos los items del carrito")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable String usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}/total")
    @Operation(summary = "Calcula el total del carrito", description = "Devuelve la suma total de todos los productos del carrito")
    public ResponseEntity<Double> total(@PathVariable String usuarioId) {
        Double total = carritoService.calcularTotal(usuarioId);
        return ResponseEntity.ok(total);
    }
}
