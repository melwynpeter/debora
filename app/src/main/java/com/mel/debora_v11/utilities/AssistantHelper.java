package com.mel.debora_v11.utilities;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AssistantHelper {
    private String speechCommand;
    private String s;
    private String subject = "";

    private final String TAG = "deb11";

    private String getIntent(String speechCommand){
        String intent = "";

        s = speechCommand.toLowerCase();


        // GREETING
        List<String> greetingPatterns = new ArrayList<>();
        greetingPatterns.add("hey");
        greetingPatterns.add("heyy");
        greetingPatterns.add("hello");
        greetingPatterns.add("hey how are you");
        greetingPatterns.add("hi");
        greetingPatterns.add("how are you");
        greetingPatterns.add("is anyone there");
        greetingPatterns.add("good day");
        greetingPatterns.add("how are ya");
        greetingPatterns.add("whatsup");

        // GOODBYE
        List<String> goodbyePatterns = new ArrayList<>();
        goodbyePatterns.add("cya");
        goodbyePatterns.add("see you");
        goodbyePatterns.add("bye bye");
        goodbyePatterns.add("see you later");
        goodbyePatterns.add("goodbye");
        goodbyePatterns.add("i am leaving");
        goodbyePatterns.add("bye");
        goodbyePatterns.add("have a good day");
        goodbyePatterns.add("talk to you later");
        goodbyePatterns.add("ttyl");
        goodbyePatterns.add("i got to go");
        goodbyePatterns.add("gtg");

        // CREATOR
        List<String> creatorPatterns = new ArrayList<>();
        creatorPatterns.add("what is the name of your developers");
        creatorPatterns.add("what is the name of your creators");
        creatorPatterns.add("what is the name of the developers");
        creatorPatterns.add("what is the name of the creators");
        creatorPatterns.add("who created you");
        creatorPatterns.add("your developers");
        creatorPatterns.add("your creators");
        creatorPatterns.add("who are your developers");
        creatorPatterns.add("developers");
        creatorPatterns.add("you are made by");
        creatorPatterns.add("you are made by whom");
        creatorPatterns.add("who created you");
        creatorPatterns.add("who create you");
        creatorPatterns.add("creators");
        creatorPatterns.add("who made you");
        creatorPatterns.add("who designed you");

        // NAME
        List<String> namePatterns = new ArrayList<>();
        namePatterns.add("name");
        namePatterns.add("your name");
        namePatterns.add("do you have a name");
        namePatterns.add("what are you called");
        namePatterns.add("what is your name");
        namePatterns.add("what should i call you");
        namePatterns.add("whats your name");
        namePatterns.add("what are you");
        namePatterns.add("who are you");
        namePatterns.add("who is this");
        namePatterns.add("who am i speaking to");
        namePatterns.add("who am i chatting with");
        namePatterns.add("who am i talking to");
        namePatterns.add("what are you");

        // TEXT
        // GENERATE TEXT
        List<String> generateTextPatterns = new ArrayList<>();
        generateTextPatterns.add("generate text");
        generateTextPatterns.add("generate some text");
        generateTextPatterns.add("please generate text");
        generateTextPatterns.add("please generate some text");
        generateTextPatterns.add("write text");
        generateTextPatterns.add("write some text");
        generateTextPatterns.add("please write some text");
        generateTextPatterns.add("please write text");
        generateTextPatterns.add("can you generate text");
        generateTextPatterns.add("can you please generate text");
        generateTextPatterns.add("can you generate some text");
        generateTextPatterns.add("can you please generate some text");
        generateTextPatterns.add("generate text for me");
        generateTextPatterns.add("please generate text for me");
        generateTextPatterns.add("can you please generate some text for me");

        // GENERATE TEXT WITH SUBJECT
        List<String> generateTextWithSubjectPatterns = new ArrayList<>();
        generateTextPatterns.add("generate text on <subject>");
        generateTextPatterns.add("generate text about <subject>");
        generateTextPatterns.add("generate some text on <subject>");
        generateTextPatterns.add("generate some text about <subject>");
        generateTextPatterns.add("please generate text on <subject>");
        generateTextPatterns.add("please generate text about <subject>");
        generateTextPatterns.add("please generate some text on <subject>");
        generateTextPatterns.add("please generate some text about <subject>");
        generateTextPatterns.add("write text on <subject>");
        generateTextPatterns.add("write text about <subject>");
        generateTextPatterns.add("write some text on <subject>");
        generateTextPatterns.add("write some text about <subject>");
        generateTextPatterns.add("please write some text on <subject>");
        generateTextPatterns.add("please write some text about <subject>");
        generateTextPatterns.add("please write text on <subject>");
        generateTextPatterns.add("please write text about <subject>");
        generateTextPatterns.add("can you generate text on <subject>");
        generateTextPatterns.add("can you generate text about <subject>");
        generateTextPatterns.add("can you please generate text on <subject>");
        generateTextPatterns.add("can you please generate text about <subject>");
        generateTextPatterns.add("can you generate some text on <subject>");
        generateTextPatterns.add("can you generate some text about <subject>");
        generateTextPatterns.add("can you please generate some text on <subject>");
        generateTextPatterns.add("can you please generate some text about <subject>");
        generateTextPatterns.add("generate text for me on <subject>");
        generateTextPatterns.add("generate text for me about <subject>");
        generateTextPatterns.add("please generate text for me on <subject>");
        generateTextPatterns.add("please generate text for me about <subject>");
        generateTextPatterns.add("can you please generate some text for me on <subject>");
        generateTextPatterns.add("can you please generate some text for me about <subject>");

        List<String> cleanedGenerateTextWithSubjectPatterns = removeWordsFromCollection(generateTextWithSubjectPatterns, " <subject>");

        subject = extractSubject(s, cleanedGenerateTextWithSubjectPatterns);

        // EMAIL
        // GENERATE EMAIL
        List<String> generateEmailPatterns = new ArrayList<>();
        generateEmailPatterns.add("generate email");
        generateEmailPatterns.add("write email");
        generateEmailPatterns.add("please generate email");
        generateEmailPatterns.add("please write email");
        generateEmailPatterns.add("generate an email");
        generateEmailPatterns.add("generate an email for me");
        generateEmailPatterns.add("can you generate an email");
        generateEmailPatterns.add("can you generate an email for me");
        generateEmailPatterns.add("please generate an email");
        generateEmailPatterns.add("please generate an email for me");
        generateEmailPatterns.add("write an email");
        generateEmailPatterns.add("please write an email");
        generateEmailPatterns.add("can you write an email for me");
        generateEmailPatterns.add("can you please write an email for me");
        generateEmailPatterns.add("please generate an electronic mail");
        generateEmailPatterns.add("generate an electronic mail");
        generateEmailPatterns.add("write an electronic mail for");
        generateEmailPatterns.add("please write an electronic mail");
        generateEmailPatterns.add("write a mail for me");
        generateEmailPatterns.add("generate a mail for me");


        // GENERATE EMAIL WITH SUBJECT
        List<String> generateEmailWithSubjectPatterns = new ArrayList<>();
        generateEmailWithSubjectPatterns.add("generate email on <subject>");
        generateEmailWithSubjectPatterns.add("write email on <subject>");
        generateEmailWithSubjectPatterns.add("generate email regarding <subject>");
        generateEmailWithSubjectPatterns.add("write email regarding <subject>");
        generateEmailWithSubjectPatterns.add("please generate email on <subject>");
        generateEmailWithSubjectPatterns.add("please write email on <subject>");
        generateEmailWithSubjectPatterns.add("generate an email on <subject>");
        generateEmailWithSubjectPatterns.add("generate an email regarding <subject>");
        generateEmailWithSubjectPatterns.add("generate an email for me on <subject>");
        generateEmailWithSubjectPatterns.add("generate an email for me regarding <subject>");
        generateEmailWithSubjectPatterns.add("can you generate an email on <subject>");
        generateEmailWithSubjectPatterns.add("can you generate an email regarding <subject>");
        generateEmailWithSubjectPatterns.add("can you generate an email for me on <subject>");
        generateEmailWithSubjectPatterns.add("can you generate an email for me regarding <subject>");
        generateEmailWithSubjectPatterns.add("please generate an email on <subject>");
        generateEmailWithSubjectPatterns.add("please generate an email regarding <subject>");
        generateEmailWithSubjectPatterns.add("please generate an email for me on <subject>");
        generateEmailWithSubjectPatterns.add("please generate an email for me regarding <subject>");
        generateEmailWithSubjectPatterns.add("write an email on <subject>");
        generateEmailWithSubjectPatterns.add("write an email regarding <subject>");
        generateEmailWithSubjectPatterns.add("please write an email on <subject>");
        generateEmailWithSubjectPatterns.add("please write an email regarding <subject>");
        generateEmailWithSubjectPatterns.add("can you write an email for me on <subject>");
        generateEmailWithSubjectPatterns.add("can you write an email for me regarding <subject>");
        generateEmailWithSubjectPatterns.add("can you please write an email for me on <subject>");
        generateEmailWithSubjectPatterns.add("can you please write an email for me regarding <subject>");
        generateEmailWithSubjectPatterns.add("please generate an electronic mail on <subject>");
        generateEmailWithSubjectPatterns.add("please generate an electronic mail regarding <subject>");
        generateEmailWithSubjectPatterns.add("generate an electronic mail on <subject>");
        generateEmailWithSubjectPatterns.add("generate an electronic mail regarding <subject>");
        generateEmailWithSubjectPatterns.add("write an electronic mail on <subject>");
        generateEmailWithSubjectPatterns.add("write an electronic mail regarding <subject>");
        generateEmailWithSubjectPatterns.add("please write an electronic mail on <subject>");
        generateEmailWithSubjectPatterns.add("please write an electronic mail regarding <subject>");
        generateEmailWithSubjectPatterns.add("write a mail for me on <subject>");
        generateEmailWithSubjectPatterns.add("write a mail for me regarding <subject>");
        generateEmailWithSubjectPatterns.add("generate a mail for me on <subject>");
        generateEmailWithSubjectPatterns.add("generate a mail for me regarding <subject>");
        List<String> cleanedGenerateEmailWithSubjectPattern = removeWordsFromCollection(generateEmailWithSubjectPatterns, " <subject>");

        subject = extractSubject(s, cleanedGenerateEmailWithSubjectPattern);

        // GENERATE EMAIL WITH TO AND SUBJECT
        List<String> generateEmailWithToAndSubjectPatterns = new ArrayList<>();
        generateEmailWithToAndSubjectPatterns.add("generate email to send to <person> with the subject <subject>");
        generateEmailWithToAndSubjectPatterns.add("generate email to send to <person> on the subject <subject>");
        generateEmailWithToAndSubjectPatterns.add("generate email to send to <person> on the subject of <subject>");
        generateEmailWithToAndSubjectPatterns.add("generate email to send to the <person> with the subject <subject>");
        generateEmailWithToAndSubjectPatterns.add("generate email to send to the <person> on the subject <subject>");
        generateEmailWithToAndSubjectPatterns.add("generate email to send to the <person> on the subject of <subject>");
        generateEmailWithToAndSubjectPatterns.add("generate email to send to my <person> with the subject <subject>");
        generateEmailWithToAndSubjectPatterns.add("generate email to send to my <person> on the subject <subject>");
        generateEmailWithToAndSubjectPatterns.add("generate email to send to my <person> on the subject of <subject>");

        generateEmailWithSubjectPatterns.add("write email to <person> with subject <subject>");
        generateEmailWithSubjectPatterns.add("write email to <person> with the subject <subject>");
        generateEmailWithSubjectPatterns.add("write email to the <person> with the subject <subject>");
        generateEmailWithSubjectPatterns.add("write email to the <person> on the subject <subject>");
        generateEmailWithSubjectPatterns.add("write email to the <person> on the subject of <subject>");

        generateEmailWithToAndSubjectPatterns.add("generate an email to send to <person> with the subject <subject>");
        generateEmailWithToAndSubjectPatterns.add("generate an email to send to <person> on the subject <subject>");
        generateEmailWithToAndSubjectPatterns.add("generate an email to send to <person> on the subject of <subject>");
        generateEmailWithToAndSubjectPatterns.add("generate an email to send to the <person> with the subject <subject>");
        generateEmailWithToAndSubjectPatterns.add("generate an email to send to the <person> on the subject <subject>");
        generateEmailWithToAndSubjectPatterns.add("generate an email to send to the <person> on the subject of <subject>");
        generateEmailWithToAndSubjectPatterns.add("generate an email to send to my <person> with the subject <subject>");
        generateEmailWithToAndSubjectPatterns.add("generate an email to send to my <person> on the subject <subject>");
        generateEmailWithToAndSubjectPatterns.add("generate an email to send to my <person> on the subject of <subject>");

        generateEmailWithSubjectPatterns.add("write an email to <person> with subject <subject>");
        generateEmailWithSubjectPatterns.add("write an email to <person> with the subject <subject>");
        generateEmailWithSubjectPatterns.add("write an email to the <person> with the subject <subject>");
        generateEmailWithSubjectPatterns.add("write an email to the <person> on the subject <subject>");
        generateEmailWithSubjectPatterns.add("write an email to the <person> on the subject of <subject>");


        List<String> cleanedGenerateEmailWithToAndSubjectPattern = removeWordsFromCollection(generateEmailWithToAndSubjectPatterns, " <subject>");
        cleanedGenerateEmailWithToAndSubjectPattern = removeWordsFromCollection(cleanedGenerateEmailWithToAndSubjectPattern, " <person>");

        subject = extractSubject(s, cleanedGenerateEmailWithSubjectPattern);


        // ALARM
        // SET AN ALARM
        List<String> setAlarmPatterns = new ArrayList<>();
        setAlarmPatterns.add("set alarm");
        setAlarmPatterns.add("set an alarm");
        setAlarmPatterns.add("please set an alarm");
        setAlarmPatterns.add("can you please set an alarm");

        // SET AN ALARM WITH TIME
        List<String> setAlarmWithTimePatterns = new ArrayList<>();
        setAlarmWithTimePatterns.add("set alarm for <time>");
        setAlarmWithTimePatterns.add("set the alarm for <time>");
        setAlarmWithTimePatterns.add("set an alarm for <time>");
        setAlarmWithTimePatterns.add("please set an alarm for <time>");
        setAlarmWithTimePatterns.add("can you please set an alarm for <time>");
        List<String> cleanedSetAlarmWithTimePatterns = removeWordsFromCollection(setAlarmWithTimePatterns, " <time>");

        // SET AN ALARM WITH TIME AND DATE
        List<String> setAlarmWithTimeAndDatePatterns = new ArrayList<>();
        setAlarmWithTimeAndDatePatterns.add("set alarm for <time> for <date>");
        setAlarmWithTimeAndDatePatterns.add("set the alarm for <time> for <date>");
        setAlarmWithTimeAndDatePatterns.add("set an alarm for <time> for <date>");
        setAlarmWithTimeAndDatePatterns.add("please set an alarm for <time> for <date>");
        setAlarmWithTimeAndDatePatterns.add("can you please set an alarm for <time> for <date>");
        List<String> cleanedSetAlarmWithTimeAndDatePatterns = removeWordsFromCollection(setAlarmWithTimeAndDatePatterns, " <time>");
        cleanedSetAlarmWithTimeAndDatePatterns = removeWordsFromCollection(setAlarmWithTimeAndDatePatterns, " <date>");

        // TIMER
        List<String> setTimerPatterns = new ArrayList<>();
        setAlarmWithTimePatterns.add("set timer");
        setAlarmWithTimePatterns.add("set the timer");
        setAlarmWithTimePatterns.add("set a timer");
        setAlarmWithTimePatterns.add("please set a timer");
        setAlarmWithTimePatterns.add("can you please set a timer");

        // TIMER WITH TIME
        List<String> setTimerWithTimePatterns = new ArrayList<>();
        setAlarmWithTimePatterns.add("set timer for <time>");
        setAlarmWithTimePatterns.add("set the timer for <time>");
        setAlarmWithTimePatterns.add("set a timer for <time>");
        setAlarmWithTimePatterns.add("please set a timer for <time>");
        setAlarmWithTimePatterns.add("can you please set a timer for <time>");



        // EXTERNAL APPS
        // YOUTUBE
        // OPEN YOUTUBE
        List<String> openYoutubePatterns = new ArrayList<>();
        openYoutubePatterns.add("open youtube");
        openYoutubePatterns.add("please open youtube");

        // OPEN WHATSAPP
        List<String> openWhatsappPatterns = new ArrayList<>();
        openYoutubePatterns.add("open whatsapp");
        openYoutubePatterns.add("please open whatsapp");



        //This first removes all non-letter characters, folds to lowercase, then splits the input, doing all the work in a single line
//        String[] words = instring.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
//        s = s.replace("[^a-zA-Z ]", "").toLowerCase();
        if(greetingPatterns.contains(s)){
            intent = Constants.INTENT_GREETING;
        } else if (goodbyePatterns.contains(s)) {
            intent = Constants.INTENT_GOODBYE;
        } else if (creatorPatterns.contains(s)) {
            intent = Constants.INTENT_CREATOR_OF_MODEL;
        } else if (namePatterns.contains(s)) {
            intent = Constants.INTENT_NAME_OF_MODEL;
        }else if (generateTextPatterns.contains(s)) {
            intent = Constants.INTENT_GENERATE_TEXT;
        }else if (doesStartsWithCollection(s, cleanedGenerateTextWithSubjectPatterns)) {
            intent = Constants.INTENT_GENERATE_TEXT_WITH_SUBJECT;
            subject = extractSubject(s, cleanedGenerateTextWithSubjectPatterns);
        }else if (generateEmailPatterns.contains(s)) {
            intent = Constants.INTENT_GENERATE_EMAIL;
        }else if (doesStartsWithCollection(s, cleanedGenerateEmailWithSubjectPattern)) {
            intent = Constants.INTENT_GENERATE_EMAIL_WITH_SUBJECT;
            subject = extractSubject(s, cleanedGenerateEmailWithSubjectPattern);
        }else if (setAlarmPatterns.contains(s)) {
            intent = Constants.INTENT_ALARM;
        }else if (doesStartsWithCollection(s, cleanedSetAlarmWithTimePatterns)) {
            intent = Constants.INTENT_ALARM_WITH_TIME;
            subject = extractSubject(s, cleanedSetAlarmWithTimePatterns);
        }else if (doesStartsWithCollection(s, cleanedSetAlarmWithTimeAndDatePatterns)) {
            intent = Constants.INTENT_ALARM_WITH_TIME_AND_DATE;
            subject = extractSubject(s, cleanedSetAlarmWithTimeAndDatePatterns);
            
        }else if (doesStartsWithCollection(s, cleanedGenerateEmailWithSubjectPattern)) {
            intent = Constants.INTENT_GENERATE_EMAIL_WITH_SUBJECT;
            subject = extractSubject(s, cleanedGenerateEmailWithSubjectPattern);
        }
        return intent;
    }

    public String getResponse(String speechCommand){
        String response = "";
        String intent = getIntent(speechCommand);

        PatternsAndResponses patternsAndResponses = new PatternsAndResponses();

        if(intent.equals(Constants.INTENT_GREETING)){
//            response = greetingResponses.get(generateRandomNumber(0, greetingResponses.size()));
            response = patternsAndResponses.getResponses(Constants.INTENT_GREETING).get(generateRandomNumber(0, patternsAndResponses.getResponses(Constants.INTENT_GREETING).size()));
        } else if (intent.equals(Constants.INTENT_GOODBYE)) {
//            response = goodbyeResponses.get(generateRandomNumber(0, goodbyeResponses.size()));
            response = patternsAndResponses.getResponses(Constants.INTENT_GOODBYE).get(generateRandomNumber(0, patternsAndResponses.getResponses(Constants.INTENT_GOODBYE).size()));

        } else if (intent.equals(Constants.INTENT_CREATOR_OF_MODEL)) {
//            response = creatorResponses.get(generateRandomNumber(0, creatorResponses.size()));
            response = patternsAndResponses.getResponses(Constants.INTENT_CREATOR_OF_MODEL).get(generateRandomNumber(0, patternsAndResponses.getResponses(Constants.INTENT_CREATOR_OF_MODEL).size()));

        } else if (intent.equals(Constants.INTENT_NAME_OF_MODEL)) {
//            response = nameResponses.get(generateRandomNumber(0, nameResponses.size()));
            response = patternsAndResponses.getResponses(Constants.INTENT_NAME_OF_MODEL).get(generateRandomNumber(0, patternsAndResponses.getResponses(Constants.INTENT_NAME_OF_MODEL).size()));

        }else if (intent.equals(Constants.INTENT_GENERATE_TEXT)) {
//            response = generateTextResponses.get(generateRandomNumber(0, generateTextResponses.size())) + "*";
            response = patternsAndResponses.getResponses(Constants.INTENT_GENERATE_TEXT).get(generateRandomNumber(0, patternsAndResponses.getResponses(Constants.INTENT_GENERATE_TEXT).size())) + "*";

        }else if (intent.equals(Constants.INTENT_GENERATE_EMAIL)) {
//            response = generateEmailResponses.get(generateRandomNumber(0, generateEmailResponses.size())) + "*";
            response = patternsAndResponses.getResponses(Constants.INTENT_GENERATE_EMAIL).get(generateRandomNumber(0, patternsAndResponses.getResponses(Constants.INTENT_GENERATE_EMAIL).size())) + "*";

        }else if (intent.equals(Constants.INTENT_GENERATE_EMAIL_WITH_SUBJECT)) {
//            response = generateEmailWithSubjectResponses.get(generateRandomNumber(0, generateEmailWithSubjectResponses.size())) + subject;
            response = patternsAndResponses.getResponses(Constants.INTENT_GENERATE_EMAIL_WITH_SUBJECT).get(generateRandomNumber(0, patternsAndResponses.getResponses(Constants.INTENT_GENERATE_EMAIL_WITH_SUBJECT).size()));

        }else if (intent.equals(Constants.INTENT_ALARM)) {
            response = patternsAndResponses.getResponses(Constants.INTENT_ALARM).get(generateRandomNumber(0, patternsAndResponses.getResponses(Constants.INTENT_ALARM).size())) + "*";
        }else if (intent.equals(Constants.INTENT_ALARM_WITH_TIME)) {
            response = patternsAndResponses.getResponses(Constants.INTENT_ALARM_WITH_TIME).get(generateRandomNumber(0, patternsAndResponses.getResponses(Constants.INTENT_ALARM_WITH_TIME).size()));
        }else if (intent.equals(Constants.INTENT_ALARM_WITH_TIME_AND_DATE)) {
            response = patternsAndResponses.getResponses(Constants.INTENT_ALARM_WITH_TIME_AND_DATE).get(generateRandomNumber(0, patternsAndResponses.getResponses(Constants.INTENT_ALARM_WITH_TIME_AND_DATE).size()));
        }else if (intent.equals(Constants.INTENT_TIMER)) {
            response = patternsAndResponses.getResponses(Constants.INTENT_TIMER).get(generateRandomNumber(0, patternsAndResponses.getResponses(Constants.INTENT_TIMER).size()));
        }else if (intent.equals(Constants.INTENT_TIMER_WITH_TIME)) {
            response = patternsAndResponses.getResponses(Constants.INTENT_TIMER_WITH_TIME).get(generateRandomNumber(0, patternsAndResponses.getResponses(Constants.INTENT_TIMER_WITH_TIME).size()));
        }else if (intent.equals(Constants.INTENT_YOUTUBE)) {
            response = patternsAndResponses.getResponses(Constants.INTENT_YOUTUBE).get(generateRandomNumber(0, patternsAndResponses.getResponses(Constants.INTENT_YOUTUBE).size()));
        }else if (intent.equals(Constants.INTENT_WHATSAPP)) {
            response = patternsAndResponses.getResponses(Constants.INTENT_WHATSAPP).get(generateRandomNumber(0, patternsAndResponses.getResponses(Constants.INTENT_WHATSAPP).size()));
        }
        return response;
    }

    private int generateRandomNumber(int min, int max){
        return (int) ((Math.random() * (max - min)) + min);
    }

    private List<String> removeWordsFromCollection(List<String> patternList, String wordToRemove){
        List<String> cleanedList = new ArrayList<>();
        for (String pattern :
                patternList) {
            if (pattern.contains(wordToRemove)){
                pattern = pattern.replaceAll(wordToRemove, "");
                cleanedList.add(pattern);
            }
        }
        return cleanedList;
    }

    private boolean doesStartsWithCollection(String stringToCheck, List<String> patternList){
        for (String pattern :
                patternList) {
            if (stringToCheck.startsWith(pattern)){
                return true;
            }
        }
        return false;
    }

    private String extractSubject(String s, List<String> textToCheck){

        for (String pattern :
                textToCheck) {
            if(s.startsWith(pattern)){
                s = s.replaceAll(pattern + " ", "");
                return s;
            }
        }
        return "";
    }

    private String extractSecondSubject(String s, String wordToRemove, List<String> textToCheck){
        List<String> cleanedTextToCheck = removeWordsFromCollection(textToCheck, wordToRemove);
        s = extractSubject(s, cleanedTextToCheck);
        return s;
    }
}
