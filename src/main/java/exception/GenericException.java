package exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;


public class GenericException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    @Getter
    @Setter
    private String errCode;
    @Getter
    @Setter
    private String errMsg;


    public GenericException(String error, String s) {
        this.errCode = "400";
        this.errMsg = error + s;
    }
}