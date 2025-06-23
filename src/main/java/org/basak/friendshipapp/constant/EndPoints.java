package org.basak.friendshipapp.constant;

public class EndPoints {
    public static final String VERSION = "/v1";
    public static final String API = "/api";
    public static final String DEV = "/dev";
    public static final String TEST = "/test";
    public static final String PROD = "/prod";

    public static final String ROOT = VERSION + DEV;
    //	Controller Sınıflar:
    public static final String FOLLOW = ROOT+ "/follow";
    public static final String MESSAGE = ROOT+ "/message";
    public static final String USER = ROOT+ "/user";
//	Metodlar

    //ORTAK:
    public static final String SAVE = "/save";
    public static final String FINDALL = "/find-all";

    //USERCONTROLLER
    public static final String REGISTER = "/register";
}
