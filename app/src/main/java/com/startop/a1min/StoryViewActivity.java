package com.startop.a1min;

// Import required Android libraries and classes.
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.databinding.DataBindingUtil;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.startop.a1min.databinding.ActivityStoryViewBinding;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jp.shts.android.storiesprogressview.StoriesProgressView;

// Define the StoryViewActivity class that extends MainActivity and implements necessary interfaces.
public class StoryViewActivity extends MainActivity implements StoriesProgressView.StoriesListener, View.OnClickListener {

    // Declare necessary variables.
    int counter = 0;
    int desccounter = 0;
    int titlecounter = 0;
    long pressTime = 0L;
    final long limit = 500L;
    ActivityStoryViewBinding binding;
    StoriesProgressView storiesProgressView;
    ImageView share;
    List<String> images;
    List<String> storyids;
    List<String> desclist;
    List<String> titlelist;
    private final Map<String, List<String>> cachedStories = new HashMap<>();
    private final Map<String, Long> cachedTimestamps = new HashMap<>();
    private static final long CACHE_EXPIRATION_TIME = 3600000; // 1 Hour

    // Define a touch listener to handle touch events for story progression.
    private final View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the activity to fullscreen mode.
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize the binding for the activity.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_story_view);

        // Get and display the stories.
        getStories();

        // Set up buttons and the StoriesProgressView for story navigation.
        View reverse = findViewById(R.id.reverse);
        View skip = findViewById(R.id.skip);
        storiesProgressView = findViewById(R.id.stories);
        share = findViewById(R.id.share);

        // Set up the click listener for the share button.
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to share the app link.
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this Awesome 1 minute App! - https://play.google.com/store/apps/details?id=com.startop.a1min");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        // Set up click listeners for reverse and skip buttons, along with the touch listener.
        reverse.setOnClickListener(v -> storiesProgressView.reverse());
        reverse.setOnTouchListener(onTouchListener);
        skip.setOnClickListener(v -> storiesProgressView.skip());
        skip.setOnTouchListener(onTouchListener);
    }

    // Implement the onNext(), onPrev(), and onComplete() methods from the StoriesListener interface.
    @Override
    public void onNext() {
        Glide.with(getApplicationContext()).load(images.get(++counter)).into(binding.image);
        binding.desc.setText(desclist.get(++desccounter));
        binding.title.setText(titlelist.get(++titlecounter));
    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;
        Glide.with(getApplicationContext()).load(images.get(--counter)).into(binding.image);
        binding.desc.setText(desclist.get(--desccounter));
        binding.title.setText(titlelist.get(--titlecounter));
    }

    @Override
    public void onComplete() {
        finish();
    }

    @Override
    protected void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        storiesProgressView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        storiesProgressView.resume();
        super.onResume();
    }

    // Method to retrieve stories from Firestore.
    private void getStories() {
        // Check if cached stories are available and still valid.
        if (cachedStories.containsKey("news")) {
            List<String> cachedImages = cachedStories.get("news");
            long cacheTimestamp = cachedTimestamps.get("news");

            // Check if cache has expired.
            if (System.currentTimeMillis() - cacheTimestamp < CACHE_EXPIRATION_TIME) {
                // Cache is still valid, use the cached images.
                setupProgressView(cachedImages);
                return;
            } else {
                // Cache has expired, remove the expired cache.
                cachedStories.remove("news");
                cachedTimestamps.remove("news");
            }
        }

        // Initialize lists to store story information.
        images = new ArrayList<>();
        storyids = new ArrayList<>();
        desclist = new ArrayList<>();
        titlelist = new ArrayList<>();

        // Get a reference to the Firestore database.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("news").orderBy("storyid").limitToLast(24)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Loop through the Firestore documents and extract story information.
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.exists()) {
                                ModelStory modelstory = document.toObject(ModelStory.class);

                                // Check if the current time is within the story's time range.
                                long timeCurrent = System.currentTimeMillis();
                                if (timeCurrent > modelstory.getTimestart() && timeCurrent < modelstory.getTimeend()) {
                                    images.add(modelstory.getImageUri());
                                    storyids.add(modelstory.getStoryid());
                                    desclist.add(modelstory.getDescription());
                                    titlelist.add(modelstory.getTitle());
                                }
                            }
                        }
                        if (!images.isEmpty()) {
                            // Cache the retrieved stories and setup the StoriesProgressView.
                            cachedStories.put("news", images);
                            cachedTimestamps.put("news", System.currentTimeMillis());
                            setupProgressView(images);
                        } else {
                            // Show a toast message if no valid stories are found.
                            Toast.makeText(StoryViewActivity.this, "No latest news found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Show a toast message if there's an error retrieving stories.
                        Toast.makeText(StoryViewActivity.this, "Failed to retrieve news", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to set up the StoriesProgressView with the retrieved stories.
    private void setupProgressView(List<String> images) {
        // Reverse the order of images, titles, and descriptions to match the StoriesProgressView behavior.
        Collections.reverse(images);
        Collections.reverse(titlelist);
        Collections.reverse(desclist);

        // Set the count, duration, listener, and starting index for the StoriesProgressView.
        storiesProgressView.setStoriesCount(images.size());
        storiesProgressView.setStoryDuration(6000L); // 6 seconds per story
        storiesProgressView.setStoriesListener(StoryViewActivity.this);
        storiesProgressView.startStories(counter);

        // Display the title, description, and image for the current story.
        binding.title.setText(titlelist.get(titlecounter));
        binding.desc.setText(desclist.get(desccounter));
        Glide.with(StoryViewActivity.this).load(images.get(counter)).into(binding.image);
    }

    @Override
    public void onClick(View v) {
        // Empty implementation (no action on click).
    }

}
