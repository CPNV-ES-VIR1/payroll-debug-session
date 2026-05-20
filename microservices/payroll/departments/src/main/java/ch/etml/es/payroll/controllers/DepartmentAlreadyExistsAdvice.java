package ch.etml.es.payroll.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DepartmentAlreadyExistsAdvice {

    @ResponseBody
    @ExceptionHandler(DepartmentAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String departmentNotFoundHandler(DepartmentAlreadyExistsException ex){
        return ex.getMessage();
    }
}
