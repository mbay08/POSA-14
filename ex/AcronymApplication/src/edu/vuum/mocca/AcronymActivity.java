package edu.vuum.mocca;

import java.util.List;

<<<<<<< HEAD
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

/**
 * The main Activity that launches the screen users will see.
 */
public class AcronymActivity extends Activity {
    /**
     * Used for logging purposes.
     */
    static private String TAG = AcronymActivity.class.getCanonicalName();

    /**
     * Reference to the Acronym Service (after it's bound).
     */
    AcronymRequest mAcronymRequest;

    /**
     * The ListView that will display the results to the user.
     */
    ListView mListView;

    /**
     * A custom ArrayAdapter used to display the list of AcronymData objects.
     */
    AcronymDataArrayAdapter adapter;
    
    /**
     * Acronym entered by the usre.
     */
    EditText mEditText;

    /**
     * The implementation of the AcronymResults AIDL
     * Interface. Should be passed to the WebService using the
     * AcronymRequest.expandAcronym() method.
     * 
     * This implementation of AcronymResults.Stub plays the role of
     * Invoker in the Broker Pattern.
     */
    AcronymResults.Stub mAcronymResults = new AcronymResults.Stub() {
            /**
             * This method is called back by the Service to return the
             * results.
             */
            @Override
		public void sendResults(final List<AcronymData> acronymDataList)
                throws RemoteException {
                // This method runs in a separate Thread as per the
                // behavior of the Android Binder framework, so we
                // need to explicitly post a runnable containing the
                // results back to the UI Thread.
=======
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

/**
 * @class AcronymActivity
 *
 * @brief The main Activity that handles communicate with the
 *        AcronymService(s).
 */
public class AcronymActivity extends AcronymActivityBase {
    /**
     * The implementation of the AcronymResults AIDL Interface, which
     * will be passed to the Acronym Web service using the
     * AcronymRequest.expandAcronym() method.
     * 
     * This implementation of AcronymResults.Stub plays the role of
     * Invoker in the Broker Pattern since it dispatches the upcall to
     * sendResults().
     */
    private AcronymResults.Stub mAcronymResults = new AcronymResults.Stub() {
            /**
             * This method is invoked by the AcronymServiceAsync to
             * return the results back to the DownloadActivity.
             */
            @Override
            public void sendResults(final List<AcronymData> acronymDataList)
                throws RemoteException {
                // Since the Android Binder framework dispatches this
                // method in a separate Thread we need to explicitly
                // post a runnable containing the results to the UI
                // Thread, where it's displayed.
>>>>>>> upstream/master
                runOnUiThread(new Runnable() {
                        public void run() {
                            displayResults(acronymDataList);
                        }
                    });
            }
	};

    /**
<<<<<<< HEAD
     * This ServiceConnection is used to receive results after binding
     * to the AcronymServiceAsync Service using bindService().
     */
    ServiceConnection mServiceConnectionAsync = new ServiceConnection() {
            /**
             * Cast the returned IBinder object to the AcronymRequest
             * AIDL Interface and store it for later use in
             * mAcronymRequest.
             */
            @Override
		public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected(), ComponentName: " + name);
                mAcronymRequest = AcronymRequest.Stub.asInterface(service);
            }

            /**
             * Called if the remote service crashes and is no longer
             * available. The ServiceConnection will remain bound, but
             * the service will not respond to any requests.
             */
            @Override
		public void onServiceDisconnected(ComponentName name) {
                mAcronymRequest = null;
            }

	};

    /**
     * Called when the activity is starting. This is where most initialization
     * should go.
     */
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get references to the UI components.
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.editText1);
        mListView = (ListView) findViewById(R.id.listView1);
    }

    /*
     * Start the Asynchronous lookup.
     */
    public void expandAcronym(View v) {
        if (mAcronymRequest != null) {
            // Get the acronym entered by the user.
            String acronym = mEditText.getText().toString();
=======
     * This GenericServiceConnection is used to receive results after
     * binding to the AcronymServiceAsync Service using bindService().
     */
    private GenericServiceConnection<AcronymRequest> mServiceConnectionAsync =
        new GenericServiceConnection<AcronymRequest>(AcronymRequest.class);

    /**
     * This GenericServiceConnection is used to receive results after
     * binding to the AcronymServiceSync Service using bindService().
     */
    private GenericServiceConnection<AcronymCall> mServiceConnectionSync =
        new GenericServiceConnection<AcronymCall>(AcronymCall.class);

    /*
     * Initiate the asynchronous acronym lookup when the user presses
     * the "Look Up Async" button.
     */
    public void expandAcronymAsync(View v) {
        AcronymRequest acronymRequest = 
            mServiceConnectionAsync.getInterface();

        if (acronymRequest != null) {
            // Get the acronym entered by the user.
            final String acronym = mEditText.getText().toString();
>>>>>>> upstream/master

            hideKeyboard();

            try {
                // Invoke a one-way AIDL call, which does not block
<<<<<<< HEAD
                // the client.
                mAcronymRequest.expandAcronym(mAcronymResults,
                                              acronym);
=======
                // the client.  The results are returned via the
                // sendResults() method of the mAcronymResults
                // callback object, which runs in a Thread from the
                // Thread pool managed by the Binder framework.
                acronymRequest.expandAcronym(mAcronymResults,
                                             acronym);
>>>>>>> upstream/master
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException:" + e.getMessage());
            }
        } else {
<<<<<<< HEAD
            Log.d(TAG, "mAcronymRequest was null.");
        }
    }

    /**
     * Display the results to the screen.
     * 
     * @param results
     *            List of Resultes to be displayed.
     */
    protected void displayResults(List<AcronymData> results) {
        // Create custom ListView Adapter and fill it with our data.
        if (adapter == null) {
            // Create a local instance of our custom Adapter for our
            // ListView.
            adapter = new AcronymDataArrayAdapter(this, results);
        } else {
            // If adapter already existed, then change data set.
            adapter.clear();
            adapter.addAll(results);
            adapter.notifyDataSetChanged();
        }

        // Set the adapter to the ListView.
        mListView.setAdapter(adapter);
    }

    /**
     * Hook method called when the MainActivity becomes visible to bind the
     * Activity to the Services.
=======
            Log.d(TAG, "acronymRequest was null.");
        }
    }

    /*
     * Initiate the synchronous acronym lookup when the user presses
     * the "Look Up Sync" button.
     */
    public void expandAcronymSync(View v) {
        final AcronymCall acronymCall = 
            mServiceConnectionSync.getInterface();

        if (acronymCall != null) {
            // Get the acronym entered by the user.
            final String acronym = mEditText.getText().toString();

            hideKeyboard();

            // Use an AsyncTask to download the Acronym data in a
            // separate thread and then display it in the UI thread.
            new AsyncTask<String, Void, List<AcronymData>> () {

                // Download the expanded acronym via a synchronous
                // two-way method call, which runs in a background
                // thread to avoid blocking the UI thread.
                protected List<AcronymData> doInBackground(String... acronyms) {
                    try {
                        return acronymCall.expandAcronym(acronyms[0]);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                // Display the results in the UI Thread.
                protected void onPostExecute(List<AcronymData> acronymDataList) {
                    displayResults(acronymDataList);
                }
            }.execute(acronym);
        } else {
            Log.d(TAG, "mAcronymCall was null.");
        }
    }

    /**
     * Hook method called when the AcronymActivity becomes visible to
     * bind the Activity to the Services.
>>>>>>> upstream/master
     */
    @Override
    public void onStart() {
        super.onStart();

<<<<<<< HEAD
        // Launch the designated Bound Service(s) if they aren't
        // already running via a call to bindService(), which binds
        // this activity to the Acronym* Services if they aren't
        // already bound.
        if (mAcronymRequest == null) {
            bindService(AcronymServiceAsync.makeIntent(this),
                        mServiceConnectionAsync, BIND_AUTO_CREATE);
        }
    }

    /**
     * Hook method called when the MainActivity becomes completely hidden to
     * unbind the Activity from the Services.
     */
    @Override
	public void onStop() {
        super.onStop();

        // Unbind the Async Service if it is connected.
        if (mAcronymRequest != null) {
            unbindService(mServiceConnectionAsync);
        }
    }

    /**
     * Hide the keyboard after a user has finished typing the url.
     */
    private void hideKeyboard() {
        InputMethodManager mgr =
            (InputMethodManager) getSystemService
            (Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(mEditText.getWindowToken(),
                                    0);
=======
        // Launch the Bound Services if they aren't already running
        // via a call to bindService(), which binds this activity to
        // the Acronym* Services if they aren't already bound.
        if (mServiceConnectionAsync.getInterface() == null) 
            bindService(AcronymServiceAsync.makeIntent(this),
                        mServiceConnectionAsync,
                        BIND_AUTO_CREATE);
        
        if (mServiceConnectionSync.getInterface() == null) 
            bindService(AcronymServiceSync.makeIntent(this),
                        mServiceConnectionSync,
                        BIND_AUTO_CREATE);
    }

    /**
     * Hook method called when the AcronymActivity becomes completely
     * hidden to unbind the Activity from the Services.
     */
    @Override
    public void onStop() {
        // Unbind the Async Service if it is connected.
        if (mServiceConnectionAsync.getInterface() != null)
            unbindService(mServiceConnectionAsync);

        // Unbind the Sync Service if it is connected.
        if (mServiceConnectionSync.getInterface() != null)
            unbindService(mServiceConnectionSync);

        super.onStop();
>>>>>>> upstream/master
    }
}
