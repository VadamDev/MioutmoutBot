package net.vadamdev.mioutmout;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public enum LoginType {
    RELEASE("TOKEN HERE", "ID HERE","m/"),
    BETA("TOKEN HERE", "ID HERE", "mb/", null, OnlineStatus.DO_NOT_DISTURB);

    private final String token, applicationId, prefix;
    private final Activity activity;
    private final OnlineStatus status;

    LoginType(String token, String applicationId, String prefix, Activity activity, OnlineStatus status) {
        this.token = token;
        this.applicationId = applicationId;
        this.prefix = prefix;
        this.activity = activity;
        this.status = status;
    }

    LoginType(String token, String applicationId, String prefix) {
        this(token, applicationId, prefix, Activity.competing("FTM ▪ Team"), OnlineStatus.ONLINE);
    }

    public String getToken() {
        return token;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getCommandPrefix() {
        return prefix;
    }

    public Activity getActivity() {
        return activity;
    }

    public OnlineStatus getStatus() {
        return status;
    }
}
