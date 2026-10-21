package com.DespesasFinanceiras.V1.Infra;


import com.DespesasFinanceiras.V1.DTOS.ErrorDTOTest;
import com.DespesasFinanceiras.V1.Exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestExceptionHandlerTest extends ResponseEntityExceptionHandler {

        @ExceptionHandler(NotFoundExceptionTest.class)
        public ResponseEntity<ErrorDTOTest> notFoundList(NotFoundExceptionTest exception){
            ErrorDTOTest errorDTOTest = new ErrorDTOTest("404", "Not Found", exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTOTest);

        }

        @ExceptionHandler(ValorInvalidoExceptionTest.class)
        public ResponseEntity<ErrorDTOTest> valueInvalid(ValorInvalidoExceptionTest exception){
            ErrorDTOTest errorDTOTest = new ErrorDTOTest("400","Bad Request", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTOTest);
        }

        @ExceptionHandler(RegistroDuplicadoExceptionTest.class)
        public ResponseEntity<ErrorDTOTest> RegistryDuplicate(RegistroDuplicadoExceptionTest exception){
            ErrorDTOTest errorDTOTest = new ErrorDTOTest("400","Conflict", exception.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTOTest);
        }

        @ExceptionHandler(DateInvalidExceptionTest.class)
        public ResponseEntity<ErrorDTOTest> DateInvalid(DateInvalidExceptionTest exception){
            ErrorDTOTest errorDTOTest = new ErrorDTOTest("400","Bad Request", exception.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTOTest);
        }

        @ExceptionHandler(OperationNotAllowedExceptionTest.class)
        public ResponseEntity<ErrorDTOTest> operationNotAllowed(OperationNotAllowedExceptionTest exception){
            ErrorDTOTest errorDTOTest = new ErrorDTOTest("403","Forbidden", exception.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDTOTest);
        }


}
