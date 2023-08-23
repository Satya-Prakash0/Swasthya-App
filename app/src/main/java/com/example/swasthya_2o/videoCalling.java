package com.example.swasthya_2o;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class videoCalling extends AppCompatActivity{

    EditText meetingId;
    Button joinMeetbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_calling);

        meetingId=findViewById(R.id.meetingId);
        joinMeetbtn=findViewById(R.id.JoinMeetBtn);

        joinMeetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=meetingId.getText().toString();
                if(id.isEmpty())
                    Toast.makeText(videoCalling.this, "Please enter the Meeting Id", Toast.LENGTH_SHORT).show();
                else{
                    try {

                        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                                .setServerURL(new URL("https://meet.jit.si"))
                                .setRoom(id)
                                .setAudioMuted(false)
                                .setAudioOnly(true)
                                .setConfigOverride("requireDisplayName", true)
                                .build();

                        JitsiMeetActivity.launch(videoCalling.this,options);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }
}