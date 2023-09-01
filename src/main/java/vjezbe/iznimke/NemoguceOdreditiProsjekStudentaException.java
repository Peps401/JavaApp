package vjezbe.iznimke;

import java.io.Serial;
import java.io.Serializable;

//checked exception, ona se MORA obraditi
//baca se u metodi i tek se poslije metode hvata
public class NemoguceOdreditiProsjekStudentaException extends Exception implements Serializable {

    @Serial
    private static final long serialVersionUID = -5036051726218755914L;

    public NemoguceOdreditiProsjekStudentaException() {
    }

    public NemoguceOdreditiProsjekStudentaException(String message) {
        super(message);
    }

    public NemoguceOdreditiProsjekStudentaException(String message, Throwable cause) {
        super(message, cause);
    }

    public NemoguceOdreditiProsjekStudentaException(Throwable cause) {
        super(cause);
    }

    public NemoguceOdreditiProsjekStudentaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
