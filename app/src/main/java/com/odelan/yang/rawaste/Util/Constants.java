package com.odelan.yang.rawaste.Util;

import java.math.BigDecimal;

public class Constants {
    public static final String SERVER_ADDRESS = "0x034825e120bb83af33c93c9eef0dd77ba8fd108e";
    public static final String SERVER_PRIVATE_KEY = "104796639170737302935174682745466016675743504127254983758937663549549839418030";
    public static final String SERVER_PUBLIC_KEY = "1013312342043906984872075895796517223221990186610564928878603591809760449339996986741823026241678552748080078520279766275379201360001283409845864202320018";
    public static final BigDecimal ONE_ETHER = new BigDecimal("1000000000000000000");

    public static final String FIREBASE_USER            = "User";
    public static final String FIREBASE_EMAIL           = "email";
    public static final String FIREBASE_FACEBOOK        = "facebookID";
    public static final String FIREBASE_GOOGLE          = "googleID";
    public static final String FIREBASE_PRODUCT         = "Product";
    public static final String FIREBASE_FAVORITE        = "Favorite";
    public static final String FIREBASE_CART            = "Cart";
    public static final String FIREBASE_SUBSCRIBER      = "Subscriber";
    public static final String FIREBASE_SUBSCRIPTION    = "Subscription";
    public static final String FIREBASE_ORDER           = "Order";
    public static final String FIREBASE_CONTACT         = "Contact";
    public static final String FIREBASE_CHAT            = "Chat";
    public static final String FIREBASE_BONUSCHAIN      = "Bonuschain";
    public static final String FIREBASE_PURCHASE        = "Purchase";
    public static final String FIREBASE_Sale            = "Sale";
    public static final String FIREBASE_TIMESTAMP       = "timestamp";
    public static final int FIREBASE_TEXT               = 0;
    public static final int FIREBASE_IMAGE              = 1;

    //    chat
    public static final int CHAT_MY                     = 0;
    public static final int CHAT_OTHER                  = 1;

    public static final String USER_TYPE_INDIVIDUAL = "Individual";
    public static final String USER_TYPE_COMPANY = "Company";

    public static final String LOGIN_ISLOGGEDIN = "login_isloggedin";
    public static final String LOGIN_EMAIL = "login_email";
    public static final String LOGIN_PASSWORD = "login_password";

    public static final String ACTIVITY_MODE = "activity_mode";
    public static final String ADD_MODE = "add_mode";
    public static final String EDIT_MODE = "edit_mode";

    public static final String CART_CURRENCY = "currency";
    public static final String CART_SUBTOTAL = "subtotal";
    public static final String CART_TOTAL = "total";
    public static final String CART_TAX = "tax";

    public static final String ADDRESS_ISSAVED = "address_issaved";
    public static final String ADDRESS_APARTMENT = "address_apartment";
    public static final String ADDRESS_STREET = "address_street";
    public static final String ADDRESS_CITY = "address_city";
    public static final String ADDRESS_COUNTRY = "address_country";
    public static final String ADDRESS_COUNTRY_CODE = "address_country_code";
    public static final String ADDRESS_PHONE_CODE = "address_phone_code";
    public static final String ADDRESS_PHONE = "address_phone";
    public static final String ADDRESS_PINCODE = "address_pincode";

    public static final String INTENT_PRODUCT = "product";
    public static final String INTENT_ORDER = "order";
    public static final String INTENT_USER = "user";
}
