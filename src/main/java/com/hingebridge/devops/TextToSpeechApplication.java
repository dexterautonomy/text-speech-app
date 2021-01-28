package com.hingebridge.devops;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.cloud.texttospeech.v1beta1.AudioConfig;
import com.google.cloud.texttospeech.v1beta1.AudioEncoding;
import com.google.cloud.texttospeech.v1beta1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1beta1.SynthesisInput;
import com.google.cloud.texttospeech.v1beta1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1beta1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1beta1.VoiceSelectionParams;
import com.google.protobuf.ByteString;

@SpringBootApplication
public class TextToSpeechApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TextToSpeechApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception
	{
		String text = "Dear Augustine Enang, we notice your cash transfer of 2 million naira to Mrs. Ifeoma Okorie was not "
				+ "successful. We duly apologize for the inconveniences caused. We are working on it. Thank you for using "
				+ "Union Bank.";
		System.out.println("---->>> :" + text);
		String outputAudioFilePath = "D:/Text-Speech/output4.mp3";

		try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create())
		{
			// Set the text input to be synthesized
			SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
			// Build the voice request; languageCode = "en_us"
			VoiceSelectionParams voice = VoiceSelectionParams.newBuilder().setLanguageCode("en-US")
					.setSsmlGender(SsmlVoiceGender.MALE)
					.build();
			// Select the type of audio file you want returned
			AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3) // MP3 audio.
					.build();
			// Perform the text-to-speech request
			SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
			// Get the audio contents from the response
			ByteString audioContents = response.getAudioContent();
			// Write the response to the output file.

			try (OutputStream out = new FileOutputStream(outputAudioFilePath))
			{
				out.write(audioContents.toByteArray());
				System.out.println("Audio content written to file \"output.mp3\"");
			}
		}
	}
}