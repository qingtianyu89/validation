package com.yzf.dop.common;

/**
 * Created by xuzhuo on 16/1/15.
 * <p/>
 * 全局返回码
 * <p/>
 * 0 成功
 * 1000~1999 通用异常
 * 2000~2999 登录业务异常
 * 3000~3999 商家业务异常
 * 4000~4999 订单业务异常
 * 5000~5999 商品业务异常
 */
public enum ErrorEnum {



    SUCCESS(0, "成功"),
    ERROR(1000, "系统异常"),
    SIGN_ERROR(1001, "签名错误"),
    PARAM_EMPTY(1002, "参数为空"),
    PARAM_ERROR(1003, "参数非法"),
    TOKEN_EMPTY(1004, "token为空"),
    TOKEN_ERROR(1005, "登录失效,请重新登录"),//token无效
    IMEI_ERROR(1006, "IMEI无效"),
    TOKEN_CUSTOM_ID_EMPTY(1007, "customId为空"),
    VERSION_NOT_SUPPORT(1008, "您当前的版本过低，无法使用该功能，请升级到最新版本~"),
    DISCOVERY_LOGIN(1009, "您已经是到家-附近的商家，暂无法使用商家端"),
    DISCOVERY_REGISTER(1010, "您的手机号已在到家-附近中被注册，暂无法使用商家端"),
    IN_BLACKLIST(1011, "您已经被拉黑"),


    //登录 start
    VERIFY_CODE_ERROR(2001, "验证码错误"),
    USER_NAME_IS_EXIST(2002, "用户已存在"),
    USER_NAME_IS_NOT_EXIST(2003, "用户不存在"),
    USERNAME_OR_PASSOWORD_ERROR(2004, "手机号或密码错误"),
    USER_PHONE_IS_EXIST(2005, "该手机号已存在"),
    USER_PHONE_IS_NOT_EXIST(2006, "该手机号没有注册"),
    USER_ID_EMPTY(2007, "用户id为空"),
    UPDATE_PASSWORD_ERROR(2008, "修改密码失败"),
    UPDATE_FACTORCUSTOM_MODIFYPHONE_ERROR(2009, "您当前的身份为机构代管老师，无法修改联系方式"),
    VERIFY_CODE_ERROR_NUM_LIMIT(2010, "验证码错误三次，请重新获取！"),
    //登录 end

    //商家 start
    CUSTOM_IS_NOT_EXIST(3000, "商家不存在"),
    CUSTOM_INFO_ERROR(3001, "商家信息异常"),
    CUSTOM_UPDATE_ADDRESS_ERROR(3002, "更新商家地址异常"),
    CUSTOM_UPDATE_WORKCYCLECONTENT_ERROR(3003, "更新商家地址异常"),
    CUSTOM_SAVE_ERROR(3004, "保存商家信息异常"),
    CUSTOM_IDENTITY_ERROR(3005, "商家认证异常"),
    CUSTOM_UPDATE_ALBUM_ERROR(3006, "编辑相册异常"),
    CUSTOM_DEL_ALBUM_ERROR(3007, "删除相册异常"),
    CUSTOM_IDCARD_ERROR(3008, "请输入15-20位身份证号"),
    CUSTOM_DIF(3009, "商家信息不一致"),
    CUSTOM_ADDRESS_IS_NOT_EXIST(3010, "您暂未添加地址导致无法添加课程"),
    //商家 end

    //订单4000 start
    ORDER_ID_EMPTY(4000, "订单id为空"),
    ORDER_PARAM_ERROR(4001, "订单参数异常"),
    ORDER_EMPTY(4002, "订单数据不存在"),
//    ORDER_TITLE_DUPLICATE(4003, "商品标题重复"),
    ORDER_DOWNLINE_FAIL(4004, "订单下线失败"),
    ORDER_ACCEPT_FAIL(4005, "接单异常"),
    ORDER_CANCEL_FAIL(4006, "拒单异常"),
    ORDER_UPDATE_CHILD_FAIL(4007, "编辑子订单异常"),
    ORDER_UPDATE_SERVICETIME(4008, "服务时间必须晚于当前时间2小时"),
    //订单 end

    //提现 5000 start
    WITHDRAWCASH_ERROR(5001, "提现失败"),
    //提现5000 end


    //机构 5000 start
    ORG_INFO_ERROR(6000, "机构信息异常"),
    ORG_NAME_DUPLICATE(6001, "机构名称已存在"),
    ORG_IDENTITY_COMPANY_DUPLICATE(6002, "营业执照注册号已存在"),
    ORG_IDENTITY_SCHOOL_DUPLICATE(6003, "办学许可证或组织机构代码证编号已存在"),
    //机构 5000 end

    ACTIVITY_RECOVER_ERROR(7001, "恢复活动异常"),
    ACTIVITY_STOP_ERROR(7002, "暂停活动异常"),
    ACTIVITY_CANCEL_ERROR(7003, "取消活动异常"),
    ACTIVITY_DEL_HISTORY_ACTIVITY_ERROR(7004, "删除历史活动异常"),
    ACTIVITY_DEL_TEMPLATE_ACTIVITY_ERROR(7005, "删除历史活动异常"),

    EDIT_PRODUCT_ERROR(8001, "保存或编辑商品异常"),

    SPEC_MUSTFILL_ERROR(9001, "缺少必填规格项");
    private int code;
    private String desc;

    ErrorEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(int code) {
        for (ErrorEnum refer : ErrorEnum.values())
            if (code == refer.getCode())
                return refer.getDesc();
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
