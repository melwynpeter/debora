package com.mel.debora_v11.utilities;

import java.util.ArrayList;
import java.util.List;

public class AssistantHelper {
    private String speechCommand;

    private String getIntent(String speechCommand){
        String intent = "";

        String s = speechCommand.toLowerCase();

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

        // EMAIL
        List<String> emailPatterns = new ArrayList<>();
        emailPatterns.add("generate email");
        emailPatterns.add("write email");
        emailPatterns.add("please generate email");
        emailPatterns.add("please write email");
        emailPatterns.add("generate an email");
        emailPatterns.add("generate an email for me");
        emailPatterns.add("can you generate an email");
        emailPatterns.add("can you generate an email for me");
        emailPatterns.add("please generate an email");
        emailPatterns.add("please generate an email for me");
        emailPatterns.add("write an email");
        emailPatterns.add("please write an email");
        emailPatterns.add("can you write an email for me");
        emailPatterns.add("can you please wite an email for me");
        emailPatterns.add("please generate an electronic mail");
        emailPatterns.add("generate an electronic mail");
        emailPatterns.add("write an electronic mail for");
        emailPatterns.add("please write an electronic mail");
        emailPatterns.add("write a mail for me");
        emailPatterns.add("generate a mail for me");


        //This first removes all non-letter characters, folds to lowercase, then splits the input, doing all the work in a single line
//        String[] words = instring.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
//        s = s.replace("[^a-zA-Z ]", "").toLowerCase();
        if(greetingPatterns.contains(s)){
            intent = Constants.INTENT_GREETING;
        } else if (goodbyePatterns.contains(s)) {
            intent = Constants.INTENT_GOODBYE;
        } else if (creatorPatterns.contains(s)) {
            intent = Constants.INTENT_CREATOR;
        } else if (namePatterns.contains(s)) {
            intent = Constants.INTENT_NAME;
        }else if (emailPatterns.contains(s)) {
            intent = Constants.INTENT_EMAIL;
        }
        return intent;
    }

    public String getResponse(String speechCommand){
        String response = "";
        String intent = getIntent(speechCommand);

        // GREETING
        List<String> greetingResponses = new ArrayList<>();
        greetingResponses.add("hello!");
        greetingResponses.add("good to see you again!");
        greetingResponses.add("hi there how can i help?");
        greetingResponses.add("hello, how can i assist?");
        greetingResponses.add("hi, how can i assist you?");

        // GOODBYE
        List<String> goodbyeResponses = new ArrayList<>();
        goodbyeResponses.add("talk to you later");
        goodbyeResponses.add("see you soon");
        greetingResponses.add("goodbye!");

        // CREATOR
        List<String> creatorResponses = new ArrayList<>();
        creatorResponses.add("I was developed by Melwyn Peter.");
        creatorResponses.add("I was developed by Melwyn Peter, an excellent Machine Learnng Expert.");
        creatorResponses.add("I was developed by Melwyn Peter, an excellent Machine Learning Engineer.");

        // NAME
        List<String> nameResponses = new ArrayList<>();
        nameResponses.add("you can call me debora.");
        nameResponses.add("Im debora");
        nameResponses.add("I am a virtual assistant, you can call me debora");
        nameResponses.add("im debora, your assisant");
        nameResponses.add("im debora, your assisant, how can i assist you?");

        // EMAIL
        List<String> emailResponses = new ArrayList<>();
        emailResponses.add("sure what topic would you like me to generate an email on");
        emailResponses.add("sure what topic would you like me to write an email on");
        emailResponses.add("what subject would you like me to generate an email on");
        emailResponses.add("what subject would you like me to write an email on");
        emailResponses.add("sure, please specify the subject");
        emailResponses.add("sure please specify the topic");
        emailResponses.add("sure please specify the subject for me to generate an email");
        emailResponses.add("sure please specify the topic for me to generate an email");



        if(intent.equals(Constants.INTENT_GREETING)){
            response = greetingResponses.get(generateRandomNumber(0, greetingResponses.size()));
        } else if (intent.equals(Constants.INTENT_GOODBYE)) {
            response = goodbyeResponses.get(generateRandomNumber(0, goodbyeResponses.size()));
        } else if (intent.equals(Constants.INTENT_CREATOR)) {
            response = creatorResponses.get(generateRandomNumber(0, creatorResponses.size()));
        } else if (intent.equals(Constants.INTENT_NAME)) {
            response = nameResponses.get(generateRandomNumber(0, nameResponses.size()));
        }else if (intent.equals(Constants.INTENT_EMAIL)) {
            response = emailResponses.get(generateRandomNumber(0, emailResponses.size())) + "*";
        }
        return response;
    }

    private int generateRandomNumber(int min, int max){
        return (int) ((Math.random() * (max - min)) + min);
    }
}
