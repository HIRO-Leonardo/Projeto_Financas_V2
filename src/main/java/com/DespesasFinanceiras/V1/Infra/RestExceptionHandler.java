package com.DespesasFinanceiras.V1.Infra;


import com.DespesasFinanceiras.V1.DTOS.ErrorDTO;
import com.DespesasFinanceiras.V1.Exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<ErrorDTO> notFoundList(NotFoundException exception){
            ErrorDTO errorDTO = new ErrorDTO("404", "Not Found", exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

        }

        @ExceptionHandler(ValorInvalidoException.class)
        public ResponseEntity<ErrorDTO> valueInvalid(ValorInvalidoException exception){
            ErrorDTO errorDTO = new ErrorDTO("400","Bad Request", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }

        @ExceptionHandler(RegistroDuplicadoException.class)
        public ResponseEntity<ErrorDTO> RegistryDuplicate(RegistroDuplicadoException exception){
            ErrorDTO errorDTO = new ErrorDTO("400","Conflict", exception.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
        }

        @ExceptionHandler(DateInvalidException.class)
        public ResponseEntity<ErrorDTO> DateInvalid(DateInvalidException exception){
            ErrorDTO errorDTO = new ErrorDTO("400","Bad Request", exception.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
        }

        @ExceptionHandler(OperationNotAllowedException.class)
        public ResponseEntity<ErrorDTO> operationNotAllowed(OperationNotAllowedException exception){
            ErrorDTO errorDTO = new ErrorDTO("403","Forbidden", exception.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDTO);
        }


}
