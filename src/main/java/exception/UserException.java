package exception;

import lombok.Getter;
import lombok.Setter;

public class UserException extends Exception {

    @Getter
    @Setter
    private String errCode;
    @Getter
    @Setter
    private String errMsg;


    public UserException(String s) {
        this.errCode = "400";
        this.errMsg = s;
    }
}
