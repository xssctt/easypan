package com.example.easypan.bean;

public class Constants {

    public static final Integer LENGTH_5=5;
    public static final Integer LENGTH_10=10;
    public static final Integer LENGTH_15=15;
    public static final Integer LENGTH_30=30;
    public static final Integer LENGTH_150=150;


    public static final Long KB=1024L;
    public static final Long MB=1024 * 1024L;


    public static final Integer ZERO=0;
    public static final Integer ONE=1;

    public static final Integer REDIS_KEY_EXPIRE_ONE_MINUTES=60;

    public static final Integer REDIS_KEY_EXPIRE_DAY=REDIS_KEY_EXPIRE_ONE_MINUTES * 60 * 24;
    public static final Integer REDIS_KEY_EXPIRE_ONE_HOUR=REDIS_KEY_EXPIRE_ONE_MINUTES * 60;


    public static final String FILE_FOLDER_FILE="/file/";
    public static final String FILE_FOLDER_TEMP="/temp/";

    public static final String FILE_FOLDER_AVATAR_NAME="avatar/";
    public static final String AVATAR_SUFFIX=".jpg";
    public static final String IMAGE_PNG_SUFFIX=".png";
    public static final String AVATAR_DEFULT="defult_avatar.jpg";


    public static final String CHECK_CODE_KEY="check_code_key";
    public static final String CHECK_CODE_KEY_EMAIL="check_code_key_email";

    public static final String VIEW_OBJ_RESULT_KEY="result";



    public static final String REDIS_KEYS_SYS_SETTING="easypan:syssetting:";
    public static final String REDIS_KEYS_USER_SPACE_USER="easypan:user:spaceuse";
    public static final String REDIS_KEYS_USER_TEMP_SIZE="easypan:user:file:temp:";

    public static final String SESSION_KEY="session_key";


    public static final String REDIS_KEYS_EMAIL_CODE_REGISTER="redis_keys_email_code_register";
    public static final String REDIS_KEYS_EMAIL_CODE_BACK="redis_keys_email_code_back";


    public static final String TS_NAME="index.ts";
    public static final String M3U8_NAME="index.m3u8";


}
