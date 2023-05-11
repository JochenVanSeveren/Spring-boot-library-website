package exception;

import lombok.Getter;
import lombok.Setter;


public class GenericException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    @Getter
    @Setter
    private String errCode;
    @Getter
    @Setter
    private String errMsg;


    public GenericException(String errorInForm, String s) {
        this.errCode = "400";
        this.errMsg = errorInForm + s;
    }
}