package com.ayushbadoni5.leaveManagement.Exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, String fileName, Object fieldValue) {
        super(String.format("%s not found with %s,'%s'", resourceName,fileName,fieldValue));
    }
}
