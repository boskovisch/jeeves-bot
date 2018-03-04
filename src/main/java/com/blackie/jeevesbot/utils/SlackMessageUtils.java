package com.blackie.jeevesbot.utils;

public class SlackMessageUtils {

    private static final String LESS_THAN = "<";
    private static final String GREATER_THAN = ">";
    private static final String AT = "@";
    private static final String STAR = "*";

    public static final String mentionUser(final String userId) {
        return LESS_THAN + AT + userId + GREATER_THAN;
    }

    public static final String makeTextBold(final String text) {
        return STAR + text + STAR;
    }

}
