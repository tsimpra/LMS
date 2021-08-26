package gr.apt.exception;

import java.io.Serializable;

public class LmsException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public LmsException() {
        super();
    }
    public LmsException(String msg)   {
        super(msg);
    }
    public LmsException(String msg, Exception e)  {
        super(msg, e);
    }
}
