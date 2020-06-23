package com.yiqzq.miaoshasystem.exception;

import com.yiqzq.miaoshasystem.result.CodeMsg;

/**
 * @author yiqzq
 * @date 2020/6/6 12:45
 */
public class MyException extends RuntimeException {
    private CodeMsg cm;

    public MyException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }

    public void setCm(CodeMsg cm) {
        this.cm = cm;
    }
}
