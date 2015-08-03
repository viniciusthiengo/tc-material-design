package br.com.thiengo.tcmaterialdesign.domain;

/**
 * Created by viniciusthiengo on 8/2/15.
 */
public class Response {
    private int code;
    private boolean status;
    private String message;


    public Response() {}
    public Response(boolean status, String message) {
        this.status = status;
        this.message = message;
    }
    public Response(int code, boolean status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
