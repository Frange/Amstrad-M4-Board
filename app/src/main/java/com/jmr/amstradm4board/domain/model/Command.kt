package com.jmr.amstradm4board.domain.model


enum class Command(val value: String) {
    NAVIGATE("ls"),
    RUN("run2"),

    RESET_M4("mres"),
    RESET_CPC("cres"),

    HACK_MENU("cnmi"),

    RENAME_CPC("navn"),

    CHANGE_SSID("ssid"),
    CHANGE_PASSWORD("pw"),

    CHANGE_LOCAL_IP("ip"),
    CHANGE_SUBNET("nm"),
    CHANGE_GATEWAY("gw"),

    CREATE_FOLDER("ccrt"),
    REMOVE("rm"),

    START_CART("ccrt"),

}