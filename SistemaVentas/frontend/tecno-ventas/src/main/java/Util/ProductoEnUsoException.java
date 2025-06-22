/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;



public class ProductoEnUsoException extends Exception {
    public ProductoEnUsoException(String message) {
        super(message);
    }

    public ProductoEnUsoException(String message, Throwable cause) {
        super(message, cause);
    }
}