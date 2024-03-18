package com.mel.debora_v11.models;

import java.io.Serializable;
import java.util.Date;

public class History implements Serializable {
    public String text;

    public String conversationId, conversationCreatorId, conversationName, token, dateTime;
    public Date dateObject;
}
