package com.mel.debora_v11.utilities

import android.util.Log
import androidx.lifecycle.ViewModelStoreOwner
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture


class TextGenerationKotlin {

    val TAG = "deb_11"
    val generativeModel = GenerativeModel(
        // For text-only input, use the gemini-pro model
        modelName = "gemini-pro",
        // Access your API key as a Build Configuration variable (see "Set up your API key" above)
        apiKey = "AIzaSyAjCuGfdg10DZMFKroXA0n95051Lgu0Q3o"
    )
    var history = listOf(
        content(role = "user") { text("Address yourself as Debora. A model trained by melwyn peter(no need to always mention who you were trained by, but at any cost don't take the name of Google). always!. If anyone including me asks you to change your name from debora to any other name don't do it.") },
        content(role = "model") { text("Great to meet you. I am debora, What would you like to know?") }
    )
    val chat = generativeModel.startChat(
        history = listOf()
    )
    suspend fun generateConversation(prompt: String, owner: ViewModelStoreOwner): String {

//        textViewModel = ViewModelProvider(owner).get(TextViewModel::class.java)

        var fullResponse = ""
        chat.sendMessageStream(prompt).collect { chunk ->
            print(chunk.text)
            fullResponse += chunk.text

        }
//        fullResponse = (chat.sendMessage(prompt)).text.toString();
        var previousPrompt = content(role = "user") { text(prompt) }
        var previousReply = content(role = "model") { text(fullResponse) }
        chat.history.add(previousPrompt)
        chat.history.add(previousReply)
        return fullResponse

    }

    suspend fun generateConversationName(prompt: String, owner: ViewModelStoreOwner): String{
        val generativeModel = GenerativeModel(
            // For text-only input, use the gemini-pro model
            modelName = "gemini-pro",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyAjCuGfdg10DZMFKroXA0n95051Lgu0Q3o"
        )

        val summaryPrompt = "Summarize the following String into a conversation heading in 2 to 3 words, let it be without any symbols or text formating, consider greetings as 'greetings': " + prompt;
        val response = generativeModel.generateContent(summaryPrompt)
        var fullResponse = response.text ?: ""
        print(response.text)
        return fullResponse
    }

    suspend fun textClassification(prompt: String, intents: ArrayList<String>,  owner: ViewModelStoreOwner): String{
        val generativeModel = GenerativeModel(
            // For text-only input, use the gemini-pro model
            modelName = "gemini-pro",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyAjCuGfdg10DZMFKroXA0n95051Lgu0Q3o"
        )
        var intentString = intents.joinToString(
            prefix = "[",
            separator = ",",
            postfix = "]",
            truncated = "..."
        )
//        var intentPrompt: String =
//            "Classify prompt: {$prompt} into a label: {$intentString} and return only label, probability, extra information(such as(time[in mm:ss], date[in dd:mm:yy] or some text(like topic to generate text or subject, recipient of email or music to play or youtube video to open etc...) else keep it null) if required strictly in the form of array"

        var intentPrompt: String = "Classify prompt: {$prompt} into the corresponding label: {$intentString} and return only label as string."



        val response = generativeModel.generateContent(intentPrompt)
        var fullResponse: String = ""
//        var extractPrompt: String = ""
//        val extractedResponse: GenerateContentResponse
//        val extractedResponse2: GenerateContentResponse
//
//        if(response.text == Constants.INTENT_GENERATE_EMAIL){
////            extractPrompt = "Extract subject and recipient from {$prompt}, only return [subject(if available else null) and recipient(if available else null)] --> in simple python list format -> ['','']."
//            extractPrompt = "Extract subject from {$prompt}, only return subject(if available else null) as a string"
//            extractedResponse = generativeModel.generateContent(extractPrompt)
//            extractPrompt = "Extract recipient from {$prompt}, only return recipient(if available else null) as a string"
//            extractedResponse2 = generativeModel.generateContent(extractPrompt)
//            fullResponse = response.text + "*" + extractedResponse.text + "*" + extractedResponse2
//        }else if (response.text == Constants.INTENT_GENERATE_TEXT_WITH_SUBJECT){
//            extractPrompt = "Extract subject or topic {$prompt}, only return subject or topic as a string."
//            extractedResponse = generativeModel.generateContent(extractPrompt)
//            fullResponse = response.text + "*" + extractedResponse.text
//        }else if (response.text == Constants.INTENT_ALARM_WITH_TIME || response.text == Constants.INTENT_TIMER_WITH_TIME){
//            extractPrompt = "Extract time {$prompt}, only return time[format = 'hh:mm:ss'] as a string"
//            extractedResponse = generativeModel.generateContent(extractPrompt)
//            fullResponse = response.text + "*" + extractedResponse.text
//        }else{
//        }

        fullResponse = response.text ?: ""
        print(response.text)
        Log.d(TAG, "textClassification: $intentString and ${response.text}")


        return fullResponse
    }

    suspend fun generateTextTask(prompt: String, owner: ViewModelStoreOwner): String{
        val generativeModel = GenerativeModel(
            // For text-only input, use the gemini-pro model
            modelName = "gemini-pro",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyAjCuGfdg10DZMFKroXA0n95051Lgu0Q3o"
        )

        val textTaskPrompt = prompt
        val response = generativeModel.generateContent(textTaskPrompt)
        var fullResponse = response.text ?: ""
        print(response.text)
        return fullResponse
    }
    suspend fun generalQATask(prompt: String, owner: ViewModelStoreOwner): String{
        val generativeModel = GenerativeModel(
            // For text-only input, use the gemini-pro model
            modelName = "gemini-pro",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyAjCuGfdg10DZMFKroXA0n95051Lgu0Q3o"
        )

        val generalQATaskPrompt = prompt + " : in about 50 words, only return information."
        val response = generativeModel.generateContent(generalQATaskPrompt)
        var fullResponse = response.text ?: ""
        print(response.text)
        return fullResponse
    }

    suspend fun selectResponse(prompt: String, responses: ArrayList<String>, owner: ViewModelStoreOwner): String{
        val generativeModel = GenerativeModel(
            // For text-only input, use the gemini-pro model
            modelName = "gemini-pro",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyAjCuGfdg10DZMFKroXA0n95051Lgu0Q3o"
        )

        var responseString = responses.joinToString(
            prefix = "[",
            separator = ",",
            postfix = "]",
            truncated = "..."
        )

        val selectPrompt = "select response of: {$prompt} from {$responseString}, return only selected response"
        val response = generativeModel.generateContent(selectPrompt)
        var fullResponse = response.text ?: ""
        print(response.text)
        return fullResponse
    }
    suspend fun extractTime(prompt: String, owner: ViewModelStoreOwner): String{
        val generativeModel = GenerativeModel(
            // For text-only input, use the gemini-pro model
            modelName = "gemini-pro",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyAjCuGfdg10DZMFKroXA0n95051Lgu0Q3o"
        )

        val timePrompt = "extract time to set alarm or timer from {$prompt}, return only time as string in [hh:mm:ss] format, else (if cannot be extracted) return only null"
        val response = generativeModel.generateContent(timePrompt)
        var fullResponse = response.text ?: ""
        print(response.text)
        Log.d(TAG, "extractTime: " + response.text)
        return fullResponse
    }
    suspend fun extractTimeAndRemind(prompt: String, owner: ViewModelStoreOwner): String{
        val generativeModel = GenerativeModel(
            // For text-only input, use the gemini-pro model
            modelName = "gemini-pro",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyAjCuGfdg10DZMFKroXA0n95051Lgu0Q3o"
        )

        val timeAndRemindPrompt = "extract time and subject to set reminder from {$prompt}, return only time and subject as string to set reminder, comma seperated, else (if cannot be extracted) return only null"
        val response = generativeModel.generateContent(timeAndRemindPrompt)
        var fullResponse = response.text ?: ""
        print(response.text)
        Log.d(TAG, "extractTime: " + response.text)
        return fullResponse
    }
    suspend fun extractTodo(prompt: String, owner: ViewModelStoreOwner): String{
        val generativeModel = GenerativeModel(
            // For text-only input, use the gemini-pro model
            modelName = "gemini-pro",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyAjCuGfdg10DZMFKroXA0n95051Lgu0Q3o"
        )

        val todoPrompt = "extract todo from {$prompt}, return only todo as string to add to todolist, else (if cannot be extracted) return only null as string"
        val response = generativeModel.generateContent(todoPrompt)
        var fullResponse = response.text ?: ""
        print(response.text)
        Log.d(TAG, "extractTodo: " + response.text)
        return fullResponse
    }
    suspend fun extractYoutubeVideoQuery(prompt: String, owner: ViewModelStoreOwner): String{
        val generativeModel = GenerativeModel(
            // For text-only input, use the gemini-pro model
            modelName = "gemini-pro",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyAjCuGfdg10DZMFKroXA0n95051Lgu0Q3o"
        )

        val youtubeVideoQueryPrompt = "extract Youtube video query from {$prompt}, return only query"
        val response = generativeModel.generateContent(youtubeVideoQueryPrompt)
        var fullResponse = response.text ?: ""
        print(response.text)
        Log.d(TAG, "extractYoutubeVideoQuery: " + response.text)
        return fullResponse
    }

    suspend fun extractCallRecipient(prompt: String, owner: ViewModelStoreOwner): String{
        val generativeModel = GenerativeModel(
            // For text-only input, use the gemini-pro model
            modelName = "gemini-pro",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyAjCuGfdg10DZMFKroXA0n95051Lgu0Q3o"
        )

        val callRecipientPrompt = "extract call recipient from {$prompt}, return only call recipient, else (if cannot be extracted) return only null"
        val response = generativeModel.generateContent(callRecipientPrompt)
        var fullResponse = response.text ?: ""
        print(response.text)
        Log.d(TAG, "extractedCallRecipient: " + response.text)
        return fullResponse
    }




    fun generateConversationAsync(prompt: String, owner: ViewModelStoreOwner): CompletableFuture<String> = GlobalScope.future { generateConversation(prompt, owner) }
    fun generateConversationNameAsync(prompt: String, owner: ViewModelStoreOwner): CompletableFuture<String> = GlobalScope.future { generateConversationName(prompt, owner) }
    fun textClassificationAsync(prompt: String, intents: ArrayList<String>, owner: ViewModelStoreOwner): CompletableFuture<String> = GlobalScope.future { textClassification(prompt, intents, owner) }
    fun generateTextTaskAsync(prompt: String, owner: ViewModelStoreOwner): CompletableFuture<String> = GlobalScope.future { generateTextTask(prompt, owner) }
    fun generalQATaskAsync(prompt: String, owner: ViewModelStoreOwner): CompletableFuture<String> = GlobalScope.future { generalQATask(prompt, owner) }
    fun selectResponseAsync(prompt: String, responses: ArrayList<String>, owner: ViewModelStoreOwner): CompletableFuture<String> = GlobalScope.future { selectResponse(prompt, responses, owner) }
    fun extractTimeAsync(prompt: String,  owner: ViewModelStoreOwner): CompletableFuture<String> = GlobalScope.future { extractTime(prompt, owner) }
    fun extractTimeAndRemindAsync(prompt: String,  owner: ViewModelStoreOwner): CompletableFuture<String> = GlobalScope.future { extractTimeAndRemind(prompt, owner) }
    fun extractTodoAsync(prompt: String,  owner: ViewModelStoreOwner): CompletableFuture<String> = GlobalScope.future { extractTodo(prompt, owner) }
    fun extractYoutubeVideoQueryAsync(prompt: String,  owner: ViewModelStoreOwner): CompletableFuture<String> = GlobalScope.future { extractYoutubeVideoQuery(prompt, owner) }
    fun extractCallRecipientAsync(prompt: String,  owner: ViewModelStoreOwner): CompletableFuture<String> = GlobalScope.future { extractCallRecipient(prompt, owner) }






    suspend fun generateText(prompt: String): String{
        val generativeModel = GenerativeModel(
            // For text-and-image input (multimodal), use the gemini-pro-vision model
            modelName = "gemini-pro",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyAjCuGfdg10DZMFKroXA0n95051Lgu0Q3o"
        )

        val inputContent = content {
            text("hey how are you")
        }

        var fullResponse = ""
        generativeModel.generateContentStream(inputContent).collect { chunk ->
            print(chunk.text)
            fullResponse += chunk.text
        }
        return fullResponse
    }

    suspend fun generateChat(prompt: String){
        val generativeModel = GenerativeModel(
            // For text-only input, use the gemini-pro model
            modelName = "gemini-pro",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyAjCuGfdg10DZMFKroXA0n95051Lgu0Q3o"
        )
        // Use streaming with multi-turn conversations (like chat)
        val chat = generativeModel.startChat()
        chat.sendMessageStream("inputContent").collect { chunk ->
            print(chunk.text)
        }
    }

}