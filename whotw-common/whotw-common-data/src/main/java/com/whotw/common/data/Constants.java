package com.whotw.common.data;

import java.math.BigDecimal;

/**
 * @author EdisonXu
 * @date 2019-07-23
 */

public interface Constants {

    String PHONE_REGEX = "^((13[0-9])|(14[5,7,9])|(15[^4])|(16[6])|(18[0-9])|(19[9])|(17[0,1,3,5,6,7,8]))\\d{8}$";

    String REGISTER_CHINESE_INVALID_REGEX = "^[\\u4e00-\\u9fa5]{0,}";

    String VALIDATION_CODE_KEY_PREFIX = "val_code_";

    String CHAR_UNDER_LINE = "_";

    String CHAR_DOUBLE_COLON = "::";
    String CHAR_DOUBLE_BAR = "-";

    String KEY_OPEN_ID = "openId";

    String KEY_UNION_ID = "unionId";

    String CREATED_TIME = "createdTime";

    String LAST_UPDATED_TIME = "lastUpdatedTime";

    String DUE_TIME = "dueTime";

    String ID = "id";

    int MINUS_ONE = -1;

    int EXPORT_PAGE_SIZE = 1000;

    int MAX_EXCEL_DATA_NUM = 65535;

    int CASH_SCALE = 2;

    int DEFAULT_SCALE = 4;

    int COMPUTE_SCALE = 8;

    int DATA_SCALE = 6;

    int DEFAULT_PRECISION = 16;

    int DATA_PRECISION = 18;

    String DATE_PATTERN_LOCAL_TIME_DEFAULT = "HH:mm";

    BigDecimal DECIMAL_ONE_HUNDRED = new BigDecimal("100");

    String REDIS_KEY_PREFIX_NEW_ORDER = "NEW_ORDER_KEY_";

    String REDIS_KEY_PREFIX_REFUND_ORDER = "REFUND_ORDER_KEY_";

    String REDIS_KEY_ROLE_ID = "XSH_APP_ROLE_";

    String STUDENT = "student";

    String TEACHER = "teacher";

    String DEFAULT_REFUND_REASON = "无理由";

    Long DEFAULT_INSTITUTION_ID = 1L;

    String INVITATION_CODE = "invitationCode";

    String PNG_BASE64 = "data:image/png;base64,";

    String INVITATION_QR_CODE = "qrCode";

    String AVATAR_FOLDER = "avatar";

    String AVATAR_PREFIX = "avl";

    String LOGO_PREFIX = "logo";

    String SHARE_PREFIX = "share";

    String DOWNLOAD_URI = "download";

    String SHARE_PARAM = "share_param";

    String EXPIRE_KEY = "expire_key";//redis过期key
    String MP = "mp"; //公众号key标示
    String TICKET = "ticket";
    Long EXPIRE_KEY_MP_TIMEOUT = 5L; //key过期时长
    Long EXPIRE_KEY_TICKET_TIMEOUT = 10L; //微信服务器每隔十分钟发送一次

}
