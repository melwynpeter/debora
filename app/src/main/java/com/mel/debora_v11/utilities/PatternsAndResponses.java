package com.mel.debora_v11.utilities;

import java.util.ArrayList;
import java.util.List;

public class PatternsAndResponses {
    public void getGreetingPatterns(){

    }
    public List<String> getResponses(String intent){

        if(intent.equals(Constants.INTENT_GREETING)) {
            // GREETING
            List<String> greetingResponses = new ArrayList<>();
            greetingResponses.add("hello!");
            greetingResponses.add("good to see you again!");
            greetingResponses.add("hi there how can i help?");
            greetingResponses.add("hello, how can i assist?");
            greetingResponses.add("hi, how can i assist you?");
            return greetingResponses;
        } else if (intent.equals(Constants.INTENT_GOODBYE)) {
            // GOODBYE
            List<String> goodbyeResponses = new ArrayList<>();
            goodbyeResponses.add("talk to you later");
            goodbyeResponses.add("see you soon");
            goodbyeResponses.add("goodbye!");
            return goodbyeResponses;
        }else if (intent.equals(Constants.INTENT_CREATOR)) {
            // CREATOR
            List<String> creatorResponses = new ArrayList<>();
            creatorResponses.add("I was developed by Melwyn Peter.");
            creatorResponses.add("I was developed by Melwyn Peter, an excellent Machine Learnng Expert.");
            creatorResponses.add("I was developed by Melwyn Peter, an excellent Machine Learning Engineer.");
            return creatorResponses;
        }else if (intent.equals(Constants.INTENT_NAME)) {
            // NAME
            List<String> nameResponses = new ArrayList<>();
            nameResponses.add("you can call me debora.");
            nameResponses.add("Im debora");
            nameResponses.add("I am a virtual assistant, you can call me debora");
            nameResponses.add("im debora, your assisant");
            nameResponses.add("im debora, your assisant, how can i assist you?");
            return nameResponses;
        } else if (intent.equals(Constants.INTENT_GENERATE_TEXT)){
            // TEXT
            // GENERATE TEXT
            List<String> generateTextResponses = new ArrayList<>();
            generateTextResponses.add("sure what topic would you like me to generate text on");
            generateTextResponses.add("sure what topic would you like me to generate some text on");
            generateTextResponses.add("sure what subject would you like me to generate text on");
            generateTextResponses.add("sure what subject would you like me to generate some text on");
            generateTextResponses.add("what topic would you like me to generate some text on");
            generateTextResponses.add("what subject would you like me to generate some text on");
            generateTextResponses.add("sure, please specify the topic");
            generateTextResponses.add("sure, please specify the subject");
            return generateTextResponses;
        } else if (intent.equals(Constants.INTENT_GENERATE_TEXT_WITH_SUBJECT)){
            // GENERATE TEXT WITH SUBJECT
            List<String> generateTextWithSubjectResponses = new ArrayList<>();
            generateTextWithSubjectResponses.add("sure");
            generateTextWithSubjectResponses.add("sure, right away");
            generateTextWithSubjectResponses.add("here it is");
            generateTextWithSubjectResponses.add("generating");
            generateTextWithSubjectResponses.add("generating text");
            generateTextWithSubjectResponses.add("creating");
            generateTextWithSubjectResponses.add("sure, generating");
            generateTextWithSubjectResponses.add("sure, creating");
        } else if (intent.equals(Constants.INTENT_GENERATE_EMAIL)) {
            // EMAIL
            // GENERATE EMAIL
            List<String> generateEmailResponses = new ArrayList<>();
            generateEmailResponses.add("sure what topic would you like me to generate an email on");
            generateEmailResponses.add("sure what topic would you like me to write an email on");
            generateEmailResponses.add("what subject would you like me to generate an email on");
            generateEmailResponses.add("what subject would you like me to write an email on");
            generateEmailResponses.add("sure, please specify the subject");
            generateEmailResponses.add("sure please specify the topic");
            generateEmailResponses.add("sure please specify the subject for me to generate an email");
            generateEmailResponses.add("sure please specify the topic for me to generate an email");
        } else if (intent.equals(Constants.INTENT_GENERATE_EMAIL_WITH_SUBJECT)) {
            // GENERATE EMAIL WITH SUBJECT
            List<String> generateEmailWithSubjectResponses = new ArrayList<>();
            generateEmailWithSubjectResponses.add("sure");
            generateEmailWithSubjectResponses.add("sure, right away");
            generateEmailWithSubjectResponses.add("here it is");
            generateEmailWithSubjectResponses.add("generating");
            generateEmailWithSubjectResponses.add("creating");
            generateEmailWithSubjectResponses.add("sure, generating");
            generateEmailWithSubjectResponses.add("sure, creating");
        }else if (intent.equals(Constants.INTENT_ALARM)) {
            // ALARM
            // SET AN ALARM
            List<String> setAlarmResponse = new ArrayList<>();
            setAlarmResponse.add("please specify a time");
            setAlarmResponse.add("please specify a time to set an alarm");
            setAlarmResponse.add("specify a time");
            setAlarmResponse.add("specify a time to set an alarm");
        }else if (intent.equals(Constants.INTENT_ALARM_WITH_TIME)) {
            // SET AN ALARM WITH TIME
            List<String> setAlarmWithTimeResponses = new ArrayList<>();
            setAlarmWithTimeResponses.add("sure... alarm set for");
            setAlarmWithTimeResponses.add("sure, right away... set an alarm for");
            setAlarmWithTimeResponses.add("set an alarm for");
        }else if (intent.equals(Constants.INTENT_ALARM_WITH_TIME_AND_DATE)) {
            // SET AN ALARM WITH TIME
            List<String> setAlarmWithTimeAndDateResponses = new ArrayList<>();
            setAlarmWithTimeAndDateResponses.add("sure... alarm set for");
            setAlarmWithTimeAndDateResponses.add("sure, right away... set an alarm for");
            setAlarmWithTimeAndDateResponses.add("set an alarm for");
        }else if (intent.equals(Constants.INTENT_TIMER)) {
            // TIMER
            // SET A TIMER
            List<String> setTimerResponses = new ArrayList<>();
            setTimerResponses.add("please specify a time");
            setTimerResponses.add("please specify a time to set a timer");
            setTimerResponses.add("specify a time");
            setTimerResponses.add("specify a time to set a timer");
        }else if (intent.equals(Constants.INTENT_TIMER_WITH_TIME)) {
            // SET AN TIMER WITH TIME
            List<String> setTimerWithTimerResponses = new ArrayList<>();
            setTimerWithTimerResponses.add("sure... timer set for");
            setTimerWithTimerResponses.add("sure, right away... set an timer for");
            setTimerWithTimerResponses.add("set an timer for");
        }else if (intent.equals(Constants.INTENT_YOUTUBE)) {
            // EXTERNAL APPS
            // OPEN YOUTUBE
            List<String> openYoutubeResponses = new ArrayList<>();
            openYoutubeResponses.add("sure, opening youtube");
            openYoutubeResponses.add("opening youtube");
        }else if (intent.equals(Constants.INTENT_YOUTUBE)) {
            // EXTERNAL APPS
            // OPEN WHATSAPP
            List<String> openWhatsappResponses = new ArrayList<>();
            openWhatsappResponses.add("sure, opening whatsapp");
            openWhatsappResponses.add("opening whatsapp");
        }





        return null;
    }
}
