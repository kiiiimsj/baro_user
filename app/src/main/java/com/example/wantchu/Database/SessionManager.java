package com.example.wantchu.Database;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    //Variables
    SharedPreferences usersSession;
    SharedPreferences.Editor editor;

    SharedPreferences usersDetailSession;
    SharedPreferences.Editor detailEditor;
    Context context;

    //Session names
    public static final String SESSION_USERSESSION = "userLoginSession";
    public static final String SESSION_REMEMMBERME = "rememberMe";

    //User session variables
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PHONENUMBER = "phoneNumber";
    public static final String KEY_DATE = "date";
    public static final String KEY_EMAIL = "email";

    //Remember Me variables
    private static final String IS_REMEMBERME = "IsRememberMe";
    public static final String KEY_SESSIONPHONENUMBER = "phoneNumber";
    public static final String KEY_SESSIONPASSWORD = "password";

    //토큰 저장
    private static final String KEY_USER_TOKEN = "userToken";

    //Constructor
    public SessionManager(Context _context, String sessionName) {
        context = _context;
        if(sessionName.equals(SESSION_REMEMMBERME)) {
            usersSession = context.getSharedPreferences(sessionName, Context.MODE_PRIVATE);
            editor = usersSession.edit();
        }
        if(sessionName.equals(SESSION_USERSESSION)) {
            usersDetailSession = context.getSharedPreferences(sessionName, Context.MODE_PRIVATE);
            detailEditor = usersDetailSession.edit();
        }
    }
    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public SharedPreferences getUsersSession() {
        return usersSession;
    }

    public SharedPreferences getUsersDetailSession() {
        return usersDetailSession;
    }

    public SharedPreferences.Editor getDetailEditor() {
        return detailEditor;
    }

    /*
        Users
        Login Session
         */
    public void createLoginSession(String name, String phone, String createDate, String email, String userToken) {
        detailEditor.putBoolean(IS_LOGIN, true);
        detailEditor.putString(KEY_USERNAME, name);
        detailEditor.putString(KEY_EMAIL, email);
        detailEditor.putString(KEY_PHONENUMBER, phone);
        detailEditor.putString(KEY_DATE, createDate);
        detailEditor.putString(KEY_USER_TOKEN, userToken);

        detailEditor.commit();
    }

    public HashMap<String, String> getUsersDetailFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_USERNAME, usersDetailSession.getString(KEY_USERNAME, null));
        userData.put(KEY_EMAIL, usersDetailSession.getString(KEY_EMAIL, null));
        userData.put(KEY_PHONENUMBER, usersDetailSession.getString(KEY_PHONENUMBER, null));
        userData.put(KEY_DATE, usersDetailSession.getString(KEY_DATE, null));
        userData.put(KEY_USER_TOKEN, usersDetailSession.getString(KEY_USER_TOKEN, null));

        return userData;
    }

    public boolean checkLogin() {
        if (usersSession.getBoolean(IS_LOGIN, false)) {
            return true;
        } else
            return false;
    }

    public void logoutUserFromSession() {
        editor.clear();
        editor.commit();
    }

    /*
    Remember Me
    Session Functions
     */

    public void createRememberMeSession(String phoneNo, String password) {

        editor.putBoolean(IS_REMEMBERME, true);
        editor.putString(KEY_SESSIONPHONENUMBER, phoneNo);
        editor.putString(KEY_SESSIONPASSWORD, password);

        editor.commit();
    }

    public void changePassUserSession(String password) {
        editor.putString(KEY_SESSIONPASSWORD, password);
        editor.commit();
    }
    public HashMap<String, String> getRemeberMeDetailsFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_SESSIONPHONENUMBER, usersSession.getString(KEY_SESSIONPHONENUMBER, null));
        userData.put(KEY_SESSIONPASSWORD, usersSession.getString(KEY_SESSIONPASSWORD, null));

        return userData;
    }

    public boolean checkRememberMe() {
        if (usersSession.getBoolean(IS_REMEMBERME, false)) {

            return true;
        } else
            return false;
    }

    public void clearRememberMeSession() {
        editor.clear();
        editor.commit();
    }

    public void clearDetailUserSession() {
        detailEditor.clear();
        detailEditor.commit();
    }

    public String getString(String fileName){
        return (usersDetailSession.getString(fileName,"") == null? "":(usersDetailSession.getString(fileName,"")));
    }

    @Override
    public String toString() {
        return "SessionManager{" +
                "usersSession=" + usersSession +
                ", editor=" + editor +
                ", usersDetailSession=" + usersDetailSession +
                ", detailEditor=" + detailEditor +
                ", context=" + context +
                '}';
    }
}
