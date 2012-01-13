package escom.libreria.info.encriptamientoMD5;

public interface Encriptamiento {

    public byte[] encrypt(String message) throws Exception ;
    public String decrypt(byte[] message) throws Exception ;
    public String convertToHex(byte[] data);
    public  byte[] hexToBytes(String hexString) ;


}