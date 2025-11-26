package com.edu.api_supermercado.exception;

import com.edu.api_supermercado.exception.models.producto.ProductoEliminadoException;
import com.edu.api_supermercado.exception.models.producto.ProductoNoEncontradoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGlobalExceptions(Exception ex) {
        log.error("Error inesperado, intente más tarde: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.
                forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());

        problemDetail.setTitle("Error Interno del Servidor");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    // Validaciones

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Error de validación: {}", ex.getMessage());

        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));

        ProblemDetail problemDetail = ProblemDetail.
                forStatusAndDetail(HttpStatus.BAD_REQUEST, "Error de validación en uno o más campo");

        problemDetail.setTitle("Error De Validación");
        problemDetail.setProperty("errors", errores);
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    // Productos

    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ProblemDetail handleProductoNoEncontrado(ProductoNoEncontradoException ex) {
        log.error("Error, ProductoNoEncontrado: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.
                forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());

        problemDetail.setTitle("Producto No Encontrado");
        problemDetail.setProperty("productoId", ex.getProductoId());
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(ProductoEliminadoException.class)
    public ProblemDetail handleProductoEliminado(ProductoEliminadoException ex) {
        log.error("Error, ProductoEliminado: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.
                forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());

        problemDetail.setTitle("Producto No Disponible");
        problemDetail.setProperty("productoId", ex.getProductoId());
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}
