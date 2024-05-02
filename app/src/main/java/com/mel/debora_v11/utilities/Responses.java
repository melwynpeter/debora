package com.mel.debora_v11.utilities;

import java.util.ArrayList;
import java.util.ArrayList;

public class Responses {
    public void getGreetingPatterns(){

    }
    public ArrayList<String> getResponses(String intent){

        if(intent.equals(Constants.INTENT_GREETING)) {
            // GREETING
            ArrayList<String> greetingResponses = new ArrayList<>();
            greetingResponses.add("hello!");
            greetingResponses.add("good to see you again!");
            greetingResponses.add("hi there how can i help?");
            greetingResponses.add("hello, how can i assist?");
            greetingResponses.add("hi, how can i assist you?");
            return greetingResponses;
        } else if (intent.equals(Constants.INTENT_GOODBYE)) {
            // GOODBYE
            ArrayList<String> goodbyeResponses = new ArrayList<>();
            goodbyeResponses.add("talk to you later");
            goodbyeResponses.add("see you soon");
            goodbyeResponses.add("goodbye!");
            return goodbyeResponses;
        }else if (intent.equals(Constants.INTENT_CREATOR_OF_MODEL)) {
            // CREATOR
            ArrayList<String> creatorResponses = new ArrayList<>();
            creatorResponses.add("I was developed by Melwyn Peter.");
            creatorResponses.add("I was developed by Melwyn Peter, an excellent Machine Learnng Expert.");
            creatorResponses.add("I was developed by Melwyn Peter, an excellent Machine Learning Engineer.");
            return creatorResponses;
        }else if (intent.equals(Constants.INTENT_NAME_OF_MODEL)) {
            // NAME
            ArrayList<String> nameResponses = new ArrayList<>();
            nameResponses.add("you can call me debora.");
            nameResponses.add("Im debora");
            nameResponses.add("I am a virtual assistant, you can call me debora");
            nameResponses.add("im debora, your assisant");
            nameResponses.add("im debora, your assisant, how can i assist you?");
            return nameResponses;
        } else if (intent.equals(Constants.INTENT_GENERATE_TEXT)){
            // TEXT
            // GENERATE TEXT
            ArrayList<String> generateTextResponses = new ArrayList<>();
            generateTextResponses.add("sure");
            generateTextResponses.add("sure, right away");
            generateTextResponses.add("here it is");
            generateTextResponses.add("generating text about {subject}");
            generateTextResponses.add("generating");
            generateTextResponses.add("generating text");
            generateTextResponses.add("creating");
            generateTextResponses.add("sure, generating");
            generateTextResponses.add("sure, creating");
            return generateTextResponses;
        } else if (intent.equals(Constants.INTENT_GENERATE_TEXT_WITHOUT_SUBJECT)){
            // GENERATE TEXT WITH SUBJECT
            ArrayList<String> generateTextWithoutSubjectResponses = new ArrayList<>();

            generateTextWithoutSubjectResponses.add("sure what topic would you like me to generate text on");
            generateTextWithoutSubjectResponses.add("sure what topic would you like me to generate some text on");
            generateTextWithoutSubjectResponses.add("sure what subject would you like me to generate text on");
            generateTextWithoutSubjectResponses.add("sure what subject would you like me to generate some text on");
            generateTextWithoutSubjectResponses.add("what topic would you like me to generate some text on");
            generateTextWithoutSubjectResponses.add("what subject would you like me to generate some text on");
            generateTextWithoutSubjectResponses.add("sure, please specify the topic");
            generateTextWithoutSubjectResponses.add("sure, please specify the subject");
            return generateTextWithoutSubjectResponses;
        } else if (intent.equals(Constants.INTENT_GENERATE_EMAIL)) {
            // EMAIL
            // GENERATE EMAIL
            ArrayList<String> generateEmailResponses = new ArrayList<>();
            generateEmailResponses.add("sure what topic would you like me to generate an email on");
            generateEmailResponses.add("sure what topic would you like me to write an email on");
            generateEmailResponses.add("what subject would you like me to generate an email on");
            generateEmailResponses.add("what subject would you like me to write an email on");
            generateEmailResponses.add("sure, please specify the subject");
            generateEmailResponses.add("sure please specify the topic");
            generateEmailResponses.add("sure please specify the subject for me to generate an email");
            generateEmailResponses.add("sure please specify the topic for me to generate an email");
            return generateEmailResponses;
        } else if (intent.equals(Constants.INTENT_GENERATE_EMAIL_WITH_SUBJECT)) {
            // GENERATE EMAIL WITH SUBJECT
            ArrayList<String> generateEmailWithSubjectResponses = new ArrayList<>();
            generateEmailWithSubjectResponses.add("sure");
            generateEmailWithSubjectResponses.add("sure, right away");
            generateEmailWithSubjectResponses.add("here it is");
            generateEmailWithSubjectResponses.add("generating");
            generateEmailWithSubjectResponses.add("creating");
            generateEmailWithSubjectResponses.add("sure, generating");
            generateEmailWithSubjectResponses.add("sure, creating");
            return generateEmailWithSubjectResponses;
        }else if (intent.equals(Constants.INTENT_ALARM)) { // SET AN ALARM
            ArrayList<String> setAlarmResponse = new ArrayList<>();
            setAlarmResponse.add("sure... alarm set for {time}");
            setAlarmResponse.add("sure, right away... set an alarm for {time}");
            setAlarmResponse.add("set an alarm for {time}");
            return setAlarmResponse;
        }else if (intent.equals(Constants.INTENT_ALARM_WITHOUT_TIME)) { // ALARM WITHOUT TIME
            ArrayList<String> setAlarmWithoutTimeResponses = new ArrayList<>();
            setAlarmWithoutTimeResponses.add("please specify a time");
            setAlarmWithoutTimeResponses.add("please specify a time to set an alarm");
            setAlarmWithoutTimeResponses.add("specify a time");
            setAlarmWithoutTimeResponses.add("specify a time to set an alarm");
            return setAlarmWithoutTimeResponses;
        }else if (intent.equals(Constants.INTENT_TIMER)) { // SET A TIMER
            ArrayList<String> setTimerResponses = new ArrayList<>();
            setTimerResponses.add("sure... timer set for {time}");
            setTimerResponses.add("sure, right away... set a timer for {time}");
            setTimerResponses.add("set a timer for {time}");
            return setTimerResponses;
        }else if (intent.equals(Constants.INTENT_TIMER_WITHOUT_TIME)) { // TIMER WITHOUT TIME
            ArrayList<String> setTimerWithoutTimerResponses = new ArrayList<>();
            setTimerWithoutTimerResponses.add("please specify a time");
            setTimerWithoutTimerResponses.add("please specify a time to set a timer");
            setTimerWithoutTimerResponses.add("specify a time");
            setTimerWithoutTimerResponses.add("specify a time to set a timer");
            return setTimerWithoutTimerResponses;
        }else if (intent.equals(Constants.INTENT_REMINDER)) { // REMINDER
            ArrayList<String> setReminderResponses = new ArrayList<>();
//            setReminderResponses.add("sure... reminder set for {time}");
//            setReminderResponses.add("sure, right away... set reminder for {time}");
//            setReminderResponses.add("set an timer for {time}");
            setReminderResponses.add("okay, ill remind you...");
            setReminderResponses.add("sure, ill remind you about {remind}");
            setReminderResponses.add("sure, ill remind you about {remind} {time}");
            setReminderResponses.add("sure, ill remind you about {remind} in {time}");
            setReminderResponses.add("sure, ill remind you about {remind} at {time}");
            return setReminderResponses;
        }else if (intent.equals(Constants.INTENT_TODO)) { // TO DO
            ArrayList<String> addTodoResponses = new ArrayList<>();
            addTodoResponses.add("todo added...");
            addTodoResponses.add("{todo} added to todo list");
            addTodoResponses.add("todo created");
            return addTodoResponses;
        }else if (intent.equals(Constants.INTENT_OPEN_YOUTUBE)) { // OPEN YOUTUBE
            ArrayList<String> openYoutubeResponses = new ArrayList<>();
            openYoutubeResponses.add("sure, opening youtube");
            openYoutubeResponses.add("opening youtube");
            return openYoutubeResponses;
        }else if (intent.equals(Constants.INTENT_OPEN_YOUTUBE_AND_PLAY)) { // OPEN YOUTUBE AND PLAY
            ArrayList<String> openYoutubeAndPlayResponses = new ArrayList<>();
            openYoutubeAndPlayResponses.add("sure, playing {videoQuery} on Youtube");
            openYoutubeAndPlayResponses.add("playing {videoQuery} on Youtube");
            openYoutubeAndPlayResponses.add("opening youtube, and playing {videoQuery}");
            openYoutubeAndPlayResponses.add("opening youtube, and playing videos about {videoQuery}");
            return openYoutubeAndPlayResponses;
        }else if (intent.equals(Constants.INTENT_WHATSAPP)) { // OPEN WHATSAPP
            ArrayList<String> openWhatsappResponses = new ArrayList<>();
            openWhatsappResponses.add("sure, opening whatsapp");
            openWhatsappResponses.add("opening whatsapp");
            return openWhatsappResponses;
        }else if (intent.equals(Constants.INTENT_GENERAQA)) { // GENERAL QA
            ArrayList<String> generalQAResponses = new ArrayList<>();
            generalQAResponses.add("sure, here is some information about ");
            generalQAResponses.add("here is some information about");
            generalQAResponses.add("information about");
            generalQAResponses.add("sure");
            return generalQAResponses;
        }else if (intent.equals(Constants.INTENT_PHONE_CALL)) { // MAKE CALL
            ArrayList<String> makeCallResponses = new ArrayList<>();
            makeCallResponses.add("calling {callRecipient} ");
            makeCallResponses.add("sure, calling {callRecipient}");
            return makeCallResponses;
        }else if (intent.equals(Constants.INTENT_PLAY_MUSIC)) { // MAKE CALL
            ArrayList<String> makeCallResponses = new ArrayList<>();
            makeCallResponses.add("calling {callRecipient} ");
            makeCallResponses.add("sure, calling {callRecipient}");
            return makeCallResponses;
        }





        return null;
    }
}
