package ch.etml.es.payroll.controllers;

public class DepartmentAlreadyExistsException extends RuntimeException{

    public DepartmentAlreadyExistsException(String acronym){
        super("Department " + acronym + " already exists");}
}
