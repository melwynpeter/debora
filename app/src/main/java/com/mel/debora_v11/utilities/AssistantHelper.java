package com.mel.debora_v11.utilities;

import java.util.ArrayList;
import java.util.List;

public class AssistantHelper {
    private String speechCommand;
    public AssistantHelper(String speechCommand){
        this.speechCommand = speechCommand;
    }

    public String checkIntent(String speechCommand){
        String intent = "";

        String s = speechCommand.toLowerCase();
        List<String> greetingPatterns = new ArrayList<>();
        greetingPatterns.add("hey");
        greetingPatterns.add("heyy");
        greetingPatterns.add("hello");
        greetingPatterns.add("hey how are you");
        greetingPatterns.add("hi");
        greetingPatterns.add("how are you");
//        String[] greetingPatterns = {"hey", "heyy", "hello", "hi", "how are you", "is anyone there", "good day", "what's up", "how are ya", "whatsup"};
        if(greetingPatterns.contains(s)){
            intent = "greeting";
        }
        return s;
    }
}
