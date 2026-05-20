package com.cahuinlabs.autenticacion.controller;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.cahuinlabs.autenticacion.models.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

 //Este metodo se encarga de manejar cualquier RuntimeException que ocurra 
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> manejarRuntimeException(RuntimeException ex){
        
     //Crea un json de error ordenado
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
     //Se envia con un estado de bad request 400
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarErroresGenerales(Exception ex){
        ErrorResponse error = new ErrorResponse("Ocurrió un error interno en el servidor.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        ex.printStackTrace();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
