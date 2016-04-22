package com.wangjie.rxandroideventssample.global;

/**
 * Created by zhanghongbo on 16/1/7.
 */
public class APIInterface {
    public final static String API_SERVER = "http://yidian.miaomiaostudy.com/api.php?";
    public final static String SERVER_WAP = "http://yidian.miaomiaostudy.com/wap.php?";
    public final static String SERVER = "http://yidian.miaomiaostudy.com/";
    public final static String VALIDATION = SERVER + "s/";
    public final static String FEEDBACK_API = API_SERVER + "app=member&act=feedback";
    public final static String MORE_API = SERVER_WAP + "app=article&act=detail&is_app=1&id=";
    public final static String LOGIN_API = API_SERVER + "app=passport&act=login";
    public final static String CATEGORY_API = API_SERVER + "app=eshop&act=types";
    public final static String SHOP_APPLY_API = API_SERVER + "app=eshop&act=apply";
    public final static String HOME_API = API_SERVER + "app=home&act=index";
    public final static String DISTRIBUTOR_APPLY_API = API_SERVER + "home&act=to_be_reseller";
    public final static String SHOP_SETTING_API = API_SERVER + "app=eshop&act=edit_info";
    public static final String COMMISION_LIST_API = API_SERVER + "app=commission&act=index";
    public static final String COMMISSION_ADD_API = API_SERVER + "app=commission&act=add";
    public static final String COMMISSION_EDIT_API = API_SERVER + "app=commission&act=edit";
    public static final String COMMISSION_DEL_API = API_SERVER + "app=commission&act=delete";
    public static final String MY_GOODS_LIST_API = API_SERVER + "app=eshop&act=my_goods";
    public static final String PUBLISH_GOODS_API = API_SERVER + "app=goods&act=create";
    public static final String EDIT_GOODS_API = API_SERVER + "app=goods&act=edit";
    public static final String DEL_GOODS_API = API_SERVER + "app=goods&act=delete";
    public static final String ONSALE_GOODS_API = API_SERVER + "app=goods&act=on_sale";
    public static final String OFFSALE_GOODS_API = API_SERVER + "app=goods&act=off_sale";
    public static final String RESELLER_LIST_API = API_SERVER + "app=eshop&act=resellers";
    public static final String MEMBER_SETTING_API = API_SERVER + "app=member&act=edit_info";
    public static final String MEMBER_AVATAR_API = API_SERVER + "app=member&act=avatar";
    public static final String MEMBER_ACOUNT_LIST_API = API_SERVER + "app=bank&act=mybank";
    public static final String MEMBER_ACOUNT_ADD_API = API_SERVER + "app=bank&act=add";
    public static final String MEMBER_ACOUNT_EDIT_API = API_SERVER + "app=bank&act=edit";
    public static final String MEMBER_ACOUNT_DELETE_API = API_SERVER + "app=bank&act=delete";
    public static final String MESSAGE_LIST_API = API_SERVER + "app=message&act=index";
    public static final String MESSAGE_READ_API = API_SERVER + "app=message&act=read";
    public static final String MESSAGE_DELETE_API = API_SERVER + "app=message&act=delete";
    public static final String CANCEL_RESELLE_API = API_SERVER + "app=eshop&act=reseller_delete";
    public static final String HOME_GOODS_API = API_SERVER + "app=goods&act=index";
    public static final String ORDER_LIST_API = API_SERVER + "app=shop_order&act=index";
    public static final String ORDER_DETAIL_API = API_SERVER + "app=shop_order&act=detail";
    public static final String CONSIGNMENT_INFO_API = API_SERVER + "app=member&act=get_shipping_company_and_goods";
    public static final String CONSIGNMENT_CONFIRM_API = API_SERVER + "app=member&act=shop_order_fahuo";
    public static final String MYINCOME_API = API_SERVER + "app=income&act=index";
    public static final String FINISH_INCOME_API = API_SERVER + "app=income&act=ok_income";
    public static final String UNFINISH_INCOME_API = API_SERVER + "app=income&act=notok_income";
    public static final String INCOME_HISTORY_API = API_SERVER + "app=withdraw&act=history";
    public static final String FREEZE_INCOME_API = API_SERVER + "app=income&act=freeze";
    public static final String WITHDRAW_API = API_SERVER + "app=withdraw&act=apply";
    public static final String SHOP_LIST_API = API_SERVER + "app=eshop&act=factories";
    public static final String CANCEL_PROXY_API = API_SERVER + "app=eshop&act=factory_delete";
    public static final String DISTRIBUTOR_PROXY_APPLY_API = API_SERVER + "app=eshop&act=reseller_apply";
    public static final String DISTRIBUTOR_PROXY_APPLY_LIST_API = API_SERVER + "app=eshop&act=reseller_apply_list";
    public static final String FANS_LIST_API = API_SERVER + "app=eshop&act=followers";
    public static final String FANS_DELETE_API = API_SERVER + "app=eshop&act=follower_delete";
    public static final String CHANGE_PASSWORD_API = API_SERVER + "app=member&act=password";
    public static final String SEND_VALIDATE_CODE_API = API_SERVER + "app=passport&act=check_mobile";
    public static final String REGISTER_API = API_SERVER + "app=passport&act=register";
    public static final String GOODS_LIST_API = API_SERVER + "app=goods&act=search";
    public static final String FACTORY_AUDIT_API = API_SERVER + "app=eshop&act=reseller_apply_audit";
    public static final String CHAT_AVATAR_API = API_SERVER + "app=member&act=get_avatar";
    public static final String CHECKED_QRCODE_API = API_SERVER + "app=goods&act=check_qrcode";
    public static final String SEND_FORGET_VALIDATE_CODE_API=API_SERVER+"app=passport&act=forget_send_sms";
    public static final String FORGET_PASSWORD_API = API_SERVER + "app=passport&act=forget";
    public static final String ORDER_SHOP_INDEX = API_SERVER + "app=eshop&act=other_shop_index";
    public static final String GOODS_DETAIL = API_SERVER +"app=goods&act=detail";

    public static final String SHOP_DETAIL_WAP = SERVER_WAP + "app=eshop&act=other_shop_index";
    public static final String GOODS_DETAIL_WAP = SERVER_WAP + "app=goods&act=detail&is_app=1";


}
