package com.yiqzq.miaoshasystem.result;

/**
 * @author yiqzq
 * @date 2020/6/4 15:56
 */

public class CodeMsg {


    private int code;
    private String msg;

    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    //登陆
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号不存在");
    //验证码
    public static CodeMsg CAPTCHA_IS_EMPTY = new CodeMsg(100001, "验证码为空");
    public static CodeMsg CAPTCHA_IS_ERROR = new CodeMsg(100002, "验证码错误");
    public static CodeMsg CAPTCHA_IS_NULL = new CodeMsg(100003, "验证码不存在");
    public static CodeMsg CAPTCHA_IS_EXPIRE = new CodeMsg(100004, "验证码已过期");
    //注册
    public static CodeMsg MOBILE_IS_EXIST = new CodeMsg(100005, "手机号已存在");
    public static CodeMsg PASSWORD_NOT_CONSISTENCY = new CodeMsg(100006, "密码不一致");
    public static CodeMsg PASSWORD_LENGTH_ERROR = new CodeMsg(100007, "密码长度应在6-16");
    public static CodeMsg USERNAME_LENGTH_ERROR = new CodeMsg(100008, "用户名长度应在3-10");
    public static CodeMsg DATE_ERROR = new CodeMsg(100009, "插入异常,请检查你的数据,再次尝试");
    public static CodeMsg MOBILE_FORMAT_ERROR = new CodeMsg(100010, "请输入正确的手机号");
    //jsr303异常
    public static CodeMsg BIND_ERROR = new CodeMsg(100011, "参数校验异常：%s");
    //用户信息过期
    public static CodeMsg SESSION_EXPIRE = new CodeMsg(100012, "用户信息已过期,请重新登陆");
    public static CodeMsg REDIS_CONNECT_TIME_OUT = new CodeMsg(100012, "Redis连接超时,请检查网络状态");
    //商品信息
    public static CodeMsg NO_GOODS = new CodeMsg(100013, "商品信息不存在");
    public static CodeMsg USER_NO_LOGIN = new CodeMsg(100014, "用户未登录,请重新登录");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(100015, "非法请求");
    public static CodeMsg SECKILL_OVER = new CodeMsg(100016, "该商品秒杀已经结束");
    public static CodeMsg REPEATE_MIAOSHA = new CodeMsg(100017, "重复秒杀");
    public static CodeMsg GOODS_TIME_NOT_REACH = new CodeMsg(200000, "秒杀时间未到");
    //订单信息
    public static final CodeMsg ORDER_NOT_EXIST = new CodeMsg(100018, "订单不存在");
    public static CodeMsg REQUEST_FREQUENT = new CodeMsg(100019, "请求过于频繁");

    //文件相关
    public static CodeMsg FILE_IS_EMPTY = new CodeMsg(100020, "文件为空,请重新上传");
    public static CodeMsg FILE_IS_TOO_BIG = new CodeMsg(100021, "文件太大,请上传2MB以下的内容");
    public static CodeMsg FILE_FORMAT_IS_ERROR = new CodeMsg(100022, "文件格式有误,请上传图片");
    public static CodeMsg FILE_UPLOAD_ERROR = new CodeMsg(100023, "文件上传出错啦,请再次尝试");
    //用户修改
    public static CodeMsg USERNAME_UPDATE_ERROR = new CodeMsg(100024, "用户名修改失败,请再次尝试");
    public static CodeMsg USERINFO_UPDATE_ERROR = new CodeMsg(100025, "发生未知异常,用户修改发生异常");
    //地址相关
    public static CodeMsg ADDRESS_ERROR = new CodeMsg(100026, "地址信息有误,请再次尝试");
    public static CodeMsg ADDRESS_IS_EMPTY = new CodeMsg(100027, "地址不能为空");
    //支付相关
    public static CodeMsg PAY_FAIL = new CodeMsg(100028, "支付失败");

    //未知错误
    public static CodeMsg UNKNOWN_ERROR = new CodeMsg(999999, "发生了未知错误");

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
        return "MsgCode{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

}
