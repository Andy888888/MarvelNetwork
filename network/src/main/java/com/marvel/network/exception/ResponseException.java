package com.marvel.network.exception;

/**
 * 描述:api请求异常对象.
 * <p>
 *
 * @author yanwenqiang.
 * @date 2019/1/30
 */
public final class ResponseException extends RuntimeException {
    public final int code;
    public final String message;
    /**
     * 原始信息，用于诊断具体问题
     */
    private String diagnostic;

    public ResponseException(int code) {
        this(code, null);
    }

    public ResponseException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public ResponseException setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
        return this;
    }
}
