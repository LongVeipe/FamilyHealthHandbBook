package com.example.familyhealthhandbook.View.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.familyhealthhandbook.Adapter.MainViewPagerAdapter;
import com.example.familyhealthhandbook.Adapter.RecyclerViewNotificationAdapter;
import com.example.familyhealthhandbook.Model.MyNotification;
import com.example.familyhealthhandbook.Model.PusherResponse;
import com.example.familyhealthhandbook.R;
import com.example.familyhealthhandbook.Utils;
import com.example.familyhealthhandbook.View.Login.LoginActivity;
import com.example.familyhealthhandbook.View.Personal.PersonalActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_VIEWPAGER_ITEM = "view_pager_item";
    public final static int VIEWPAGER_ITEM_HOME = 0, VIEWPAGER_ITEM_GROUPS = 1, VIEWPAGER_ITEM_COVID = 2;

    public static final int ACTION_CREATE_POST = 0, ACTION_JOIN_GROUP = 1, ACTION_LEAVE_GROUP = 2;
    public static final int TYPE_PUSHER_NOTIFICATION = 0, TYPE_PUSHER_SOS_= 1;

    private BottomNavigationView bottomNav;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private CircleImageView avatar_iv;
    private CircleImageView notification_iv;
    private CircleImageView sos_iv;

    private Pusher pusher;
    private PopupWindow popup;

    private List<MyNotification> myNotifications;
    private RecyclerView notification_rv;
    private RecyclerViewNotificationAdapter notificationAdapter;
    private View layoutPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        init();
    }

    private void mapping()
    {
        viewPager = findViewById(R.id.main_ViewPager);
        bottomNav = findViewById(R.id.bottom_nav);
        toolbar = findViewById(R.id.toolbar);
        notification_iv = findViewById(R.id.notification_iv);
        avatar_iv = findViewById(R.id.avatar_ImageView);
        sos_iv = findViewById(R.id.sos_iv);
    }
    private void init()
    {
        initToolBar();
        initViewPager();
        initBottomNav();
        initPusher();
        initPopup();
        getNotification();
        initRecyclerViewNotification();
    }

    private void initToolBar() {
        Picasso.with(this).load(getAvatar()).into(avatar_iv);
        avatar_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PersonalActivity.class);
                startActivity(intent);
            }
        });

        sos_iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Thông báo")
                        .setMessage("Đang thực hiện cuộc gọi khẩn cấp")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(R.drawable.sos)
                        .show();
                Utils.getApi().CallSOS(getTokenFromSharedPreferences()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
                return false;
            }
        });

        sos_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Giữ \"SOS\" để tạo cuộc gọi khẩn cấp", Toast.LENGTH_SHORT).show();
            }
        });

        notification_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showPopup(v);
            }
        });
    }

    void initPopup()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        popup = new PopupWindow(MainActivity.this);
        layoutPopup = getLayoutInflater().inflate(R.layout.popup_notification, null);
        popup.setContentView(layoutPopup);
        // Set content width and height
        popup.setHeight(height - 500);
        popup.setWidth(width - 200);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        popup.setBackgroundDrawable(new BitmapDrawable());
    }

    void initRecyclerViewNotification()
    {
        notification_rv = layoutPopup.findViewById(R.id.notification_rv);
        notificationAdapter = new RecyclerViewNotificationAdapter(MainActivity.this, myNotifications);
        notification_rv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        notification_rv.setClipToPadding(false);
        notification_rv.setAdapter(notificationAdapter);
        notificationAdapter.notifyDataSetChanged();
    }

    private void showPopup(View v) {

        popup.showAtLocation(layoutPopup, Gravity.NO_GRAVITY, 150,200);
        //popup.showAsDropDown(v);

    }

    private void getNotification()
    {
        Utils.getApi().GetMyNotification(getTokenFromSharedPreferences()).enqueue(new Callback<List<MyNotification>>() {
            @Override
            public void onResponse(Call<List<MyNotification>> call, Response<List<MyNotification>> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    myNotifications = response.body();
                    initRecyclerViewNotification();
                }
            }

            @Override
            public void onFailure(Call<List<MyNotification>> call, Throwable t) {
                Log.e("Error connect to server", t.toString());
                t.printStackTrace();
            }
        });
    }
    private void initViewPager() {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        int currentItem = getIntent().getIntExtra(EXTRA_VIEWPAGER_ITEM, 0);
        viewPager.setCurrentItem(currentItem);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position)
                {
                    case 0:
                        bottomNav.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;
                    case 1:
                        bottomNav.getMenu().findItem(R.id.action_groups).setChecked(true);
                        break;
                    case 2:
                        bottomNav.getMenu().findItem(R.id.action_covid).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initBottomNav()
    {
        bottomNav = findViewById(R.id.bottom_nav);
        int currentItem = getIntent().getIntExtra(EXTRA_VIEWPAGER_ITEM, 0);
        bottomNav.getMenu().getItem(currentItem).setChecked(true);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case  R.id.action_groups:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.action_covid:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
    }

    void initPusher()
    {
        PusherOptions options = new PusherOptions();
        options.setCluster("ap1");

        this.pusher = new Pusher("faec3c297e4ac6c6ba75", options);

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.i("Pusher", "State changed from " + change.getPreviousState() +
                        " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.i("Pusher", "There was a problem connecting! " +
                        "\ncode: " + code +
                        "\nmessage: " + message +
                        "\nException: " + e
                );
            }
        }, ConnectionState.ALL);

        Channel channel = pusher.subscribe("group-channel");

        channel.bind("joinGroup-event", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                receivePusher(event.getData());
            }
        });

        channel.bind("leaveGroup-event", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                Log.e("Pusher", "Received event with data: " + event.toString());
                receivePusher(event.getData());
            }
        });

        channel.bind("createPost-event", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                Log.e("Pusher", "Received event with data: " + event.toString());
                receivePusher(event.getData());
            }
        });

        channel.bind("sos-event", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                Log.e("Pusher", "Received event with data: " + event.toString());
                receivePusher(event.getData());
            }
        });
    }


    private void callSOS(PusherResponse pusherResponse) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.android_logo);
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.rington);

        Notification notification = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setContentTitle("Cuộc gọi khẩn cấp")
                .setContentText(pusherResponse.getInformation().getUser().getName() + " đang thực hiện cuộc gọi khẩn cấp")
                .setSmallIcon(R.drawable.sos)
                .setLargeIcon(bitmap)
                .setSound(sound)
                .build();
        notification.flags = Notification.FLAG_INSISTENT;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null)
            notificationManager.notify(getNotificationId(), notification);
    }

    private int getNotificationId() {
        return (int) new Date().getTime();
    }

    private PusherResponse createPusherResponseFromJson(String json)
    {
        Gson gson = new Gson();
        PusherResponse response = gson.fromJson(json, PusherResponse.class);
        return response;
    }

    private void receivePusher(String data) {
        PusherResponse pusherResponse = createPusherResponseFromJson(data);
        if(pusherResponse.getInformation().getUser().get_id().matches(getIdUserFromSharedPreferences()))
            return;
        if(pusherResponse.getType() == TYPE_PUSHER_NOTIFICATION)
        {
            getNotification();
        }
        else if(pusherResponse.getType() == TYPE_PUSHER_SOS_)
        {
            for(String item: pusherResponse.getInformation().getMembers())
            {
                if(item.matches(getIdUserFromSharedPreferences()))
                    return;
            }
            callSOS(pusherResponse);
        }
    }


    String getAvatar()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LoginActivity.AVATAR, "");
    }


    String getTokenFromSharedPreferences()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LoginActivity.TOKEN, "");
    }

    String getIdUserFromSharedPreferences()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LoginActivity.ID_USER, "");
    }
}