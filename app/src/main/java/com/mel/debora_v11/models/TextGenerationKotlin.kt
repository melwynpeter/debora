package com.mel.debora_v11.models

import androidx.lifecycle.ViewModelStoreOwner
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.mel.debora_v11.activities.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture


class TextGenerationKotlin {
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
            truncated = "...",
            transform = { it.lowercase() }
        )
        var intentPrompt: String =
            "Classify the given prompt: {$prompt} into the given labels: {$intentString} and return only the label along with the probability and some extra information if required such as(time[in mm:ss], date[in dd:mm:yy] or some text) in the form of an array"


        val response = generativeModel.generateContent(intentPrompt)
        var fullResponse = response.text ?: ""
        print(response.text)
        return fullResponse
    }




    fun generateConversationAsync(prompt: String, owner: ViewModelStoreOwner): CompletableFuture<String> = GlobalScope.future { generateConversation(prompt, owner) }
    fun generateConversationNameAsync(prompt: String, owner: ViewModelStoreOwner): CompletableFuture<String> = GlobalScope.future { generateConversationName(prompt, owner) }
    fun textClassificationAsync(prompt: String, intents: ArrayList<String>, owner: ViewModelStoreOwner): CompletableFuture<String> = GlobalScope.future { textClassification(prompt, intents, owner) }






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