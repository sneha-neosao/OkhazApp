package ae.okhaz.boss.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ae.okhaz.admin.R;

import java.util.ArrayList;

import ae.okhaz.boss.Adapter.DeckAdapter;
import ae.okhaz.boss.Model.CourseModal;
import ae.okhaz.boss.view.activities.MainDrawerBackActivity;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

public class TutorialActivity extends AppCompatActivity implements CardStackListener {

    private CardStackView cardStack;
    private CardStackLayoutManager manager;
    private ArrayList<CourseModal> courseModalArrayList;
    private TextView btn_continue;

    private ImageView[] ivArrayDotsPager;
    private LinearLayout llPagerDots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        courseModalArrayList = new ArrayList<>();
        cardStack = findViewById(R.id.swipe_deck);
        btn_continue = findViewById(R.id.btn_continue);
        llPagerDots = (LinearLayout) findViewById(R.id.pager_dots);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TutorialActivity.this, MainDrawerBackActivity.class));
                finish();
            }
        });

        courseModalArrayList.add(new CourseModal("C++", "30 days", "20 Tracks", "C++ Self Paced Course", R.drawable.image1));
        courseModalArrayList.add(new CourseModal("Java", "30 days", "20 Tracks", "Java Self Paced Course", R.drawable.image2));
        courseModalArrayList.add(new CourseModal("Python", "30 days", "20 Tracks", "Python Self Paced Course", R.drawable.image3));
        courseModalArrayList.add(new CourseModal("DSA", "30 days", "20 Tracks", "DSA Self Paced Course", R.drawable.image4));
        courseModalArrayList.add(new CourseModal("PHP", "30 days", "20 Tracks", "PHP Self Paced Course", R.drawable.image5));

        manager = new CardStackLayoutManager(this, this);
        manager.setStackFrom(StackFrom.Top);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());

        final DeckAdapter adapter = new DeckAdapter(courseModalArrayList, this);
        cardStack.setLayoutManager(manager);
        cardStack.setAdapter(adapter);

        setupPagerIndidcatorDots(courseModalArrayList.size());
        ivArrayDotsPager[0].setImageResource(R.drawable.page_indicator_selected2);
    }

    private void setupPagerIndidcatorDots(int size) {
        ivArrayDotsPager = new ImageView[size];
        for (int i = 0; i < ivArrayDotsPager.length; i++) {
            ivArrayDotsPager[i] = new ImageView(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 5, 0);
            ivArrayDotsPager[i].setLayoutParams(params);
            ivArrayDotsPager[i].setImageResource(R.drawable.page_ndicator_unselected2);
            ivArrayDotsPager[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setAlpha(1);
                }
            });
            llPagerDots.addView(ivArrayDotsPager[i]);
            llPagerDots.bringToFront();
        }
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {}

    @Override
    public void onCardSwiped(Direction direction) {
        int position = manager.getTopPosition();
        if (position <= ivArrayDotsPager.length) {
            for (int i = 0; i < ivArrayDotsPager.length; i++) {
                ivArrayDotsPager[i].setImageResource(R.drawable.page_ndicator_unselected2);
            }
            if (position < ivArrayDotsPager.length) {
                ivArrayDotsPager[position].setImageResource(R.drawable.page_indicator_selected2);
            }
        }

        if (position == courseModalArrayList.size()) {
            startActivity(new Intent(TutorialActivity.this, MainDrawerBackActivity.class));
            finish();
        }
    }

    @Override
    public void onCardRewound() {}

    @Override
    public void onCardCanceled() {}

    @Override
    public void onCardAppeared(View view, int position) {}

    @Override
    public void onCardDisappeared(View view, int position) {}
}