package com.rakesh.voicerecognitionexample;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class VoiceRecognitionActivity extends Activity {
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;

	private EditText metTextHint;
	private ListView mlvTextMatches;
	private Spinner msTextMatches;
	private Button mbtSpeak;
	
	private ArrayList<String> badWords = new ArrayList<String>();
	private ArrayList<String> goodWords = new ArrayList<String>();
	
	private String badWordsDetected = "";
	private String goodWordsDetected = "";
	
	private Integer badWordsCounter =0;
	private Integer goodWordsCounter =0;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_recognition);
		metTextHint = (EditText) findViewById(R.id.etTextHint);
		mlvTextMatches = (ListView) findViewById(R.id.lvTextMatches);
		msTextMatches = (Spinner) findViewById(R.id.sNoOfMatches);
		mbtSpeak = (Button) findViewById(R.id.btSpeak);
		
		badWords.add("never");
		badWords.add("always");
		badWords.add("idiot");
		badWords.add("shut");
		badWords.add("fuck");
		badWords.add("suck");
		badWords.add("stupid");
		badWords.add("motherfucker");
		badWords.add("again");
		
		goodWords.add("apologize");
		goodWords.add("sorry");
		goodWords.add("thanks");
		goodWords.add("thank");
		goodWords.add("love");
		goodWords.add("like");
		goodWords.add("nice");
	
		
	}

	public void checkVoiceRecognition() {
		// Check if voice recognition is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() == 0) {
			mbtSpeak.setEnabled(false);
			Toast.makeText(this, "Voice recognizer not present",
					Toast.LENGTH_SHORT).show();
		}
		if (activities.size() > 0) {
			mbtSpeak.setEnabled(false);
			Toast.makeText(this, "Loading Voice recognize",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void speak(View view) {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 50000);
		intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 10000);
		
		intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 10000);
				
		badWordsDetected = "";
		goodWordsDetected = "";
		
		badWordsCounter =0;
		goodWordsCounter =0;
		
		// Specify the calling package to identify your application
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
				.getPackage().getName());

		// Display an hint to the user about what he should say.
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, metTextHint.getText()
				.toString());

		// Given an hint to the recognizer about what the user is going to say
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

		// If number of Matches is not selected then return show toast message
		if (msTextMatches.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
			Toast.makeText(this, "Please select No. of Matches from spinner",
					Toast.LENGTH_SHORT).show();
			return;
		}

		int noOfMatches = Integer.parseInt(msTextMatches.getSelectedItem()
				.toString());
		// Specify how many results you want to receive. The results will be
		// sorted where the first result is the one with higher confidence.

		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, noOfMatches);

		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}

	@Override
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)

			//If Voice recognition is successful then it returns RESULT_OK
			if(resultCode == RESULT_OK) {

				ArrayList<String> textMatchList = data
				.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				System.out.print("\n\nRecognized:"+textMatchList.get(0)+"\n\n");
				
				countWords(textMatchList.get(0));
				textMatchList.remove(0);
				if (goodWordsCounter>1)
				textMatchList.add("Plus " + goodWordsCounter + " points\t for: " + goodWordsDetected);
				else 
					if (goodWordsCounter==1)
						textMatchList.add("Plus " + goodWordsCounter + " point\t for: " + goodWordsDetected);
				
				if (badWordsCounter>1)
					textMatchList.add("Minus " + badWordsCounter + " points\t for: " + badWordsDetected);
				else 
					if (badWordsCounter==1)
						textMatchList.add("Minus " + badWordsCounter + " point\t for: " + badWordsDetected);
				
				
					
				if (!textMatchList.isEmpty()) {
					// If first Match contains the 'search' word
					// Then start web search.
					System.out.print("Recognized:"+textMatchList.get(0));
					
					if (textMatchList.get(0).contains("search")) {

						String searchQuery = textMatchList.get(0).replace("search",
						" ");
						Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
						search.putExtra(SearchManager.QUERY, searchQuery);
						startActivity(search);
					} else {
						// populate the Matches
						mlvTextMatches
						.setAdapter(new ArrayAdapter<String>(this,
								android.R.layout.simple_list_item_1,
								textMatchList));
						
					}

				}
			//Result code for various error.	
			}else if(resultCode == RecognizerIntent.RESULT_AUDIO_ERROR){
				showToastMessage("Audio Error");
			}else if(resultCode == RecognizerIntent.RESULT_CLIENT_ERROR){
				showToastMessage("Client Error");
			}else if(resultCode == RecognizerIntent.RESULT_NETWORK_ERROR){
				showToastMessage("Network Error");
			}else if(resultCode == RecognizerIntent.RESULT_NO_MATCH){
				showToastMessage("No Match");
			}else if(resultCode == RecognizerIntent.RESULT_SERVER_ERROR){
				showToastMessage("Server Error");
			}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	protected void countWords(String text)
	{
		String[] words = text.split("\\s");
	
		for (String w: words)
		{
			if(goodWords.contains(w)) {
				goodWordsCounter++;
				goodWordsDetected = goodWordsDetected + " " + w;
				
			}
			if(badWords.contains(w))
			{
				badWordsCounter++;
				badWordsDetected = badWordsDetected + " " + w;
			}
		}
		
	}
	void showToastMessage(String message){
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}
