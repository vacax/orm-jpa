package edu.pucmm.eict.ormjpa.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class Encriptacion {

    private static final StandardPBEStringEncryptor encryptor = crear();

    private static StandardPBEStringEncryptor crear() {
        StandardPBEStringEncryptor e = new StandardPBEStringEncryptor();
        e.setPassword("clave-secreta-pucmm-icc352");
        return e;
    }

    public static String encriptar(String texto) {
        return encryptor.encrypt(texto);
    }

    public static String desencriptar(String texto) {
        return encryptor.decrypt(texto);
    }
}
