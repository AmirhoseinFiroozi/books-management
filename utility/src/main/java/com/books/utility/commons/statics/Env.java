package com.books.utility.commons.statics;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author : Armin.Nik
 * @project : shared
 */
public abstract class Env {
    public static String HOST_NAME;

    static {
        try {
            HOST_NAME = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignored) {
        }
    }

    private Env() {
    }
}
