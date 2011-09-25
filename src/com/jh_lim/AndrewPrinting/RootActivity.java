package com.jh_lim.AndrewPrinting;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Root Activity
 * TODO: store andrew id
 * @author Jiunn Haur Lim
 */
public class RootActivity extends Activity implements Button.OnClickListener {
	
	/** TAG for logcat */
	public static final String TAG = "Andrew Printing";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        // check if we are started by a send action
        String action = getIntent().getAction();
        if (!Intent.ACTION_SEND.equals (action)) finish();
        
        setContentView(R.layout.main);
        registerListeners ();
        
    }
    
    /** Called when the activity is resumed */
    @Override
	public void onResume (){
    	super.onResume ();
    	Log.d (TAG, "called on resume!");
    }
    
    /** Called when activity is destroyed */
    @Override
    public void onDestroy (){
    	unregisterListeners ();
    	super.onDestroy();
    }

    /** Called when a button is clicked */
	@Override
	public void onClick (View v) {
		
		// dubious click, do nothing
		if (null == v) return;
		
		int id = v.getId();
		
		// determine action
		switch (id){
			case R.id.button_ok:
				// process click and return
				mailToPrinter ();
				return;
			default:
				// dubious click, do nothing
				Log.e (TAG, "Unknown click landed here. ID was " + id + ".");
				return;
		}
		
	}
	
	/**
	 * Formats an email to the printer
	 * @throws IllegalStateException
	 * 				if the layout is invalid
	 */
	private void mailToPrinter (){
		
		// retrieve inputs
		int numberOfCopies = 0;
		String andrewId = null;
		Uri imageUri = null;
		try {
			numberOfCopies = retrieveNCopies ();
			andrewId = retrieveAndrewId ();
			imageUri = retrieveImage ();
		}catch (NumberFormatException e){
			// TODO
			Log.d (TAG, "Invalid number of copies.");
			return;
		}catch (InvalidAndrewIdException e){
			// TODO
			Log.d (TAG, "Invalid Andrew id.");
			return;
		}catch (InvalidDataException e){
			// TODO
			Log.d (TAG, "Invalid image data.");
			return;
		}
		
		// send to mail app
		Intent email = new Intent (Intent.ACTION_SEND_MULTIPLE);
		String recipients[] = { "user@fakehost.com","user2@fakehost.com" };
		ArrayList<Uri> uris = new ArrayList<Uri>();
		uris.add(imageUri);
		
		email.setType ("plain/text");
		email.putExtra(Intent.EXTRA_EMAIL, recipients);
		email.putExtra(Intent.EXTRA_SUBJECT, formatJson (numberOfCopies, andrewId));
		email.putExtra(Intent.EXTRA_TEXT, "Brought to you by the CMU Mobile Apps Club.");  
		email.putParcelableArrayListExtra (Intent.EXTRA_STREAM, uris);
		
	    startActivity (Intent.createChooser (email, "Send email using"));  
		
		// close activity
		finish ();
		
	}
	
	/**
	 * Formats the parameters into a JSON string
	 * @param numberOfCopies
	 * @param andrewId
	 * @return json
	 */
	private String formatJson (int numberOfCopies, String andrewId){
		
		return "{copies:" + numberOfCopies + ", id:" + andrewId + "}";
		
	}
	
	/**
	 * Retrieves and validates image path
	 * @throws InvalidDataException
	 * 		if input data is invalid
	 */
	private Uri retrieveImage() throws InvalidDataException {
		
		Intent i = getIntent ();
		Bundle extras = i.getExtras();
		
		if (null == extras) throw new InvalidDataException ("Missing data.");
		if (!extras.containsKey(Intent.EXTRA_STREAM)) throw new InvalidDataException ("Missing data.");
		
		// get uri and open stream
		Uri uri = (Uri) extras.getParcelable (Intent.EXTRA_STREAM);
		
		if (null == uri) throw new InvalidDataException ("Missing data.");
		return uri;
		
//		ContentResolver cr = getContentResolver ();
//		InputStream is = null;
//		
//		// decode image path
//		byte[] data = null;
//		try {
//			is = cr.openInputStream(uri);
//			data = IOUtils.toByteArray(is);
//		}catch (IOException e){
//			throw new InvalidDataException ("Undecodable data.");
//		}
//		
//		Log.d (TAG, new String (data));
		
	}

	/**
	 * Retrieves and validates number of copies
	 * @return number of copies
	 * @throws IllegalStateException
	 * 				if the layout is invalid
	 * @throws NumberFormatException
	 * 				if input number is invalid
	 */
	private int retrieveNCopies () throws NumberFormatException {
		
		// get edit text
		EditText inputNCopies = (EditText) findViewById (R.id.edit_n_copies);
		
		if (null == inputNCopies)
			throw new IllegalStateException ("Invalid layout: input fields missing.");
		
		// retrieve and validate number
		int numberOfCopies = Integer.parseInt (inputNCopies.getText().toString());
		if (numberOfCopies < 1) throw new NumberFormatException ("Number of copies must be positive.");
		
		return numberOfCopies;
		
	}
	
	/**
	 * Retrieves and validates andrew id
	 * @return Andrew ID
	 * @throws IllegalStateException
	 * 				if the layout is invalid
	 * @throws InvalidAndrewIdException
	 * 				if andrew id is invalid
	 * TODO: better exceptions
	 */
	private String retrieveAndrewId () throws InvalidAndrewIdException {
		
		// get edit text
		EditText inputAndrewId = (EditText) findViewById (R.id.edit_andrew_id);
		
		if (null == inputAndrewId)
			throw new IllegalStateException ("Invalid layout: input fields missing.");
		
		// retrieve and validate string
		String andrewId = inputAndrewId.getText().toString();
		if (null == andrewId || andrewId.length() < 1)
			throw new InvalidAndrewIdException ("Number of copies must be positive.", andrewId);
		
		return andrewId;
		
	}
	
	/**
	 * Registers listeners - call this onCreate
	 * @throws IllegalStateException
	 * 				if the layout is invalid
	 */
	private void registerListeners (){
		// register button listener
        Button ok = (Button) findViewById (R.id.button_ok);
        if (null == ok) throw new IllegalStateException ("Invalid layout: OK button missing.");
        ok.setOnClickListener (this);
	}
	
	/**
	 * Unregister listeners - call this onDestroy
	 */
	private void unregisterListeners (){
		// unregister button listener
        Button ok = (Button) findViewById (R.id.button_ok);
        if (null == ok) throw new IllegalArgumentException ("Invalid layout: OK button missing.");
        ok.setOnClickListener(null);
	}
    
}