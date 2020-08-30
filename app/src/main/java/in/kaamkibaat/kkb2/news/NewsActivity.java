package in.kaamkibaat.kkb2.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import in.kaamkibaat.kkb2.MainActivity;
import in.kaamkibaat.kkb2.R;
import in.kaamkibaat.kkb2.RecyclerDecoration;
import in.kaamkibaat.kkb2.Viewholder.Viewholder;
import in.kaamkibaat.kkb2.member.Member;
import in.kaamkibaat.kkb2.politics.PoliticsActivity;
import in.kaamkibaat.kkb2.politics.PoliticsActivity2;

public class NewsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    RecyclerView mRecyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    String mtitle,mcontent,mimage,mcat,mtitleTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kaam ki Baat - Politics");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Nav coding here
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(NewsActivity.this,drawerLayout,R.string.drw_op,R.string.drw_cl);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelected(item);
                return false;
            }
        });

        // firebase code
        mRecyclerView = findViewById(R.id.RV);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("KKB");
        reference.keepSynced(true);

    }

    // on other than menu item selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //  class for menuitem selected

    public void UserMenuSelected(MenuItem item){
        if (item.getItemId() == R.id.nav_home){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_politics){
            Intent intent = new Intent(this,PoliticsActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_news){
            Intent intent = new Intent(this, NewsActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_bio){
//            Intent intent = new Intent(this, Biography.class);
//            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_vid){
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/channel/UC3dzH7JgfIzm2na8QhIIscg"));
            try {
                NewsActivity.this.startActivity(webIntent);
            } catch (ActivityNotFoundException ignored) {
            }
        }
        else if (item.getItemId() == R.id.exit){
//            System.exit(1);
            this.finishAffinity();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String p = "news";
        Query firebaseQuery = firebaseDatabase.getReference("KKB").orderByChild("cat").equalTo(p);
        FirebaseRecyclerOptions<Member> options =
                new FirebaseRecyclerOptions.Builder<Member>()
                        .setQuery(firebaseQuery, Member.class)
                        .build();


        FirebaseRecyclerAdapter<Member, Viewholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Member, Viewholder>(options) {


                    @Override
                    protected void onBindViewHolder(@NonNull Viewholder viewHandler, int position, @NonNull Member member) {
                        viewHandler.Nsetdetails(getApplication(), member.getTitle(), member.getContent(), member.getImage_url(),member.getCat(),member.getTitleTag());

                        viewHandler.setOnClicklistener(new Viewholder.ClickListener() {
                            @Override
                            public void onItemclick(View view, int position) {
                                mtitle = getItem(position).getTitle();
                                mcontent = getItem(position).getContent();
                                mcat = getItem(position).getCat();
                                mtitleTag = getItem(position).getTitleTag();
                                mimage = getItem(position).getImage_url();

                                Intent intent = new Intent(NewsActivity.this, PoliticsActivity2.class);
                                intent.putExtra("title", mtitle);
                                intent.putExtra("content", mcontent);
                                intent.putExtra("cat", mcat);
                                intent.putExtra("titleTag", mtitleTag);
                                intent.putExtra("image", mimage);
                                startActivity(intent);
                            }
                            @Override
                            public void onItemLongclick(View view, int position) {
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.newsitems, parent, false);
                        return new Viewholder(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        int sidePadding = getResources().getDimensionPixelSize(R.dimen.sidePadding);
        int topPadding = getResources().getDimensionPixelSize(R.dimen.topPadding);
        mRecyclerView.addItemDecoration(new RecyclerDecoration(sidePadding,topPadding));
    }
}