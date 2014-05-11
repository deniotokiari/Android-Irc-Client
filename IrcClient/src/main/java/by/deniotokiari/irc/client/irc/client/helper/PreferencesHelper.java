package by.deniotokiari.irc.client.irc.client.helper;

import by.istin.android.xcore.preference.PreferenceHelper;
import by.istin.android.xcore.utils.StringUtil;

public class PreferencesHelper {

    public static final String NICK_NAME_0 = "key:nick_name_0";
    public static final String NICK_NAME_1 = "key:nick_name_1";
    public static final String NICK_NAME_2 = "key:nick_name_2";
    public static final String USER_NAME = "key:user_name";
    public static final String REAL_NAME = "key:rael_name";

    public void setNick(String nick) {
        PreferenceHelper.set(NICK_NAME_0, nick);
    }

    public String getNick() {
        return PreferenceHelper.getString(NICK_NAME_0, StringUtil.EMPTY);
    }

    public void setSecondNick(String nick) {
        PreferenceHelper.set(NICK_NAME_1, nick);
    }

    public String getSecondNick() {
        return PreferenceHelper.getString(NICK_NAME_1, StringUtil.EMPTY);
    }

    public void setThirdNick(String nick) {
        PreferenceHelper.set(NICK_NAME_2, nick);
    }

    public String getThirdNick() {
        return PreferenceHelper.getString(NICK_NAME_2, StringUtil.EMPTY);
    }

    public void setUserName(String userName) {
        PreferenceHelper.set(USER_NAME, userName);
    }

    public String getUserName() {
        return PreferenceHelper.getString(USER_NAME, StringUtil.EMPTY);
    }

    public void setRealName(String realName) {
        PreferenceHelper.set(REAL_NAME, realName);
    }

    public String getRealName() {
        return PreferenceHelper.getString(REAL_NAME, StringUtil.EMPTY);
    }

}
