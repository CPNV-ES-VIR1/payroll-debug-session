package ch.cpnv.es.payroll.Controllers;

public class EmployeeAlreadyExistsException extends RuntimeException{

    public EmployeeAlreadyExistsException(String name){
        super("Employee " + name + " already exists");}
}
