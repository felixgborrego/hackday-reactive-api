package com.github.reactiveapi.server;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felixgarcia on 04/04/2014.
 */
public class GCMClient {
    private static String SENDER_ID = "AIzaSyDfukCAhkEaX8BA_b2gvxqZGVydmCFhZyI";

    private List<String> androidTargets = new ArrayList<String>();

    // This is a *cheat*  It is a hard-coded registration ID from an Android device
    // that registered itself with GCM using the same project id shown above.
    private static final String DEVICE_DEBUG = "APA91bHyoq90dnYNj7GVqkYaJyCMKWzac6tZFG9mqefWGVdJ-sXW-nJSTNxHhyz0M0k7ODce9vmMj-ZUvR6psxoPn7sVMUZlPy9GDhtDlGLoUVm_W95_6pp9FLVqHq8z5fCMm7xW2V9U3v5dEGJps7kw_l-xITsmoAJ6oedrtRLaJ2xwMoC9Vmg";


    public GCMClient() {
        androidTargets.add(DEVICE_DEBUG);
    }

    public void push(String json) {
        // We'll collect the "CollapseKey" and "Message" values from our JSP page
        String collapseKey = "";
        String userMessage = "";

        try {
            userMessage = json;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Instance of com.android.gcm.server.Sender, that does the
        // transmission of a Message to the Google Cloud Messaging service.
        Sender sender = new Sender(SENDER_ID);

        // This Message object will hold the data that is being transmitted
        // to the Android client devices.  For this demo, it is a simple text
        // string, but could certainly be a JSON object.
        Message message = new Message.Builder()

                // If multiple messages are sent using the same .collapseKey()
                // the android target device, if it was offline during earlier message
                // transmissions, will only receive the latest message for that key when
                // it goes back on-line.
                .collapseKey(collapseKey)
                .timeToLive(30)
                .delayWhileIdle(true)
                .addData("message", userMessage)
                .build();

        try {
            // use this for multicast messages.  The second parameter
            // of sender.send() will need to be an array of register ids.
            MulticastResult result = sender.send(message, androidTargets, 1);

            if (result.getResults() != null) {
                int canonicalRegId = result.getCanonicalIds();
                if (canonicalRegId != 0) {

                }
            } else {
                int error = result.getFailure();
                System.out.println("Broadcast failure: " + error);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
