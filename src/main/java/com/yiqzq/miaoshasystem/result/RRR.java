package com.yiqzq.miaoshasystem.result;

/**
 * @author yiqzq
 * @date 2020/6/6 13:11
 */
public class RRR<T> {
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "RRR{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    public boolean isSuccess() {
        return this.code == CodeMsg.SUCCESS.getCode();
    }
        public boolean isrrrrr() {
        return this.code == CodeMsg.SUCCESS.getCode();
    }

    public RRR(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
