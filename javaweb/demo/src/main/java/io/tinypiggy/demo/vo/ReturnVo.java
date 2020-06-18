package io.tinypiggy.demo.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReturnVo<T> {

    /**
     * 状态码，比如1000代表响应成功
     */
    private int code;
    /**
     * 响应信息，用来说明响应情况
     */
    private String msg;
    /**
     * 响应的具体数据
     */
    private T data;

    public ReturnVo(T data) {
        this(200, "success", data);
    }

    public ReturnVo(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

}
