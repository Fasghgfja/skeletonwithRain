package at.qe.skeleton.exceptions;

public class IPCantReadException extends Throwable {
    public IPCantReadException(String m){
        System.out.println(m);
    }
}
