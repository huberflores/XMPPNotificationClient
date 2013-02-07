package org.apache.android.xmpp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler; 
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;

public class XMPPClient extends Activity {

    private ArrayList<String> messages = new ArrayList();
    private Handler mHandler = new Handler();
    private SettingsDialog mDialog;
    private EditText mRecipient;
    private EditText mSendText;
    private ListView mList;
    private XMPPConnection connection; 
    
    private final Context CONTEXT = this;

    private AccelerometerDatabase DbMotion = new AccelerometerDatabase(this);
    
    private int batteryLevel;
    
    //Battery level
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
	    @Override
	    public void onReceive(Context arg0, Intent intent) {
	      // TODO Auto-generated method stub
	      int level = intent.getIntExtra("level", 0);
	      batteryLevel= level;
	    }
	  };
    
    
    /**
     * Called with the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);

        mRecipient = (EditText) this.findViewById(R.id.recipient);
        mSendText = (EditText) this.findViewById(R.id.sendText);
        mList = (ListView) this.findViewById(R.id.listMessages);
        setListAdapter();

        // Dialog for getting the xmpp settings
        mDialog = new SettingsDialog(this);
        
        //Registering intent for battery
        this.registerReceiver(this.mBatInfoReceiver, 
        	    new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        // Set a listener to show the settings dialog
        Button setup = (Button) this.findViewById(R.id.setup);
        setup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mHandler.post(new Runnable() {
                    public void run() {
                        mDialog.show();
                    }
                });
            }
        });

        // Extract database from local application space to the external/internal sdcard
        Button database = (Button) this.findViewById(R.id.database);
        database.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	cloneDatabase base = new cloneDatabase();
        		try {
        			if (base.fileToCopy()){
        				base.copyDataBase();
        			}
        			} catch (IOException e) {
        				e.printStackTrace();
        		}
        		finish();
            }
        });

        // Set a listener to send a chat text message
        Button send = (Button) this.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String to = mRecipient.getText().toString();
                String text = mSendText.getText().toString();

                Message msg = new Message(to, Message.Type.chat);
                msg.setBody(text);
                connection.sendPacket(msg);
                messages.add(connection.getUser() + ":");
                messages.add(text);
                setListAdapter();
            }
        });
    }
 
    /**
     * Called by Settings dialog when a connection is established with the XMPP server
     *
     * @param connection
     */
    public void setConnection
            (XMPPConnection
                    connection) {
        this.connection = connection;
        if (connection != null) {
            // Add a packet listener to get messages sent to us
            PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
            connection.addPacketListener(new PacketListener() {
                public void processPacket(Packet packet) {
                    Message message = (Message) packet;
                    if (message.getBody() != null) {  
                    	                   	
                        String fromName = StringUtils.parseBareAddress(message.getFrom());
                        
                        messages.add(fromName + ":");
                        messages.add(message.getBody());
                        // Add the incoming message to the list view
                        mHandler.post(new Runnable() {
                            public void run() {
                                setListAdapter();
                            }
                        });
                        
                        
                        //Store in database
                        DbMotion.open();
        				DbMotion.createEntry(fromName, message.getBody(), 0, System.currentTimeMillis(), batteryLevel);
        				DbMotion.close();
                        
                        
                        //Android Notification (Graphic)
                        NotificationManager notificationManager = (NotificationManager) CONTEXT
        				.getSystemService(Context.NOTIFICATION_SERVICE);
                    	
                    	Notification notification = new Notification(R.drawable.icon,
                				"Message received", System.currentTimeMillis());
                		// Hide the notification after its selected
                		notification.flags |= Notification.FLAG_AUTO_CANCEL;

                		Intent intent = new Intent(CONTEXT, MessageReceivedActivity.class);
                		//intent.putExtra("payload", payload);
                		intent.putExtra("msg", message.getBody());
                		PendingIntent pendingIntent = PendingIntent.getActivity(CONTEXT, 0,
                				intent, 0);
                		notification.setLatestEventInfo(CONTEXT, "Message",
                				"New message received", pendingIntent);
                		notificationManager.notify(0, notification);
                        
                    }
                }
            }, filter);
        }
    }

    private void setListAdapter
            () {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.multi_line_list_item,
                messages);
        mList.setAdapter(adapter);
    }
    
    
    
}
