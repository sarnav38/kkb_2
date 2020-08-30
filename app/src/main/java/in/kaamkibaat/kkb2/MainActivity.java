package in.kaamkibaat.kkb2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import in.kaamkibaat.kkb2.Viewholder.Viewholder;
import in.kaamkibaat.kkb2.member.Member;
import in.kaamkibaat.kkb2.news.NewsActivity;
import in.kaamkibaat.kkb2.politics.PoliticsActivity;
import in.kaamkibaat.kkb2.latest.latestActivity2;
import in.kaamkibaat.kkb2.search.searchActivity;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle actionBarDrawerToggle;
    CardView btn_P,btn_N,btn_B,btn_V;
    ProgressDialog pd;
    NavigationView navigationView;
    ImageView searchBtn;
    RecyclerView.LayoutManager manager;
    RecyclerView mRecyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    String mtitle,mcontent,mimage,mcat,mtitleTag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // create progress dialog
        pd = new ProgressDialog(MainActivity.this);

        //Nav coding here
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,R.string.drw_op,R.string.drw_cl);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView = findViewById(R.id.navigation_view);

        btn_P =findViewById(R.id.cardView1);
        btn_N =findViewById(R.id.cardView2);
        btn_B =findViewById(R.id.cardView);
        btn_V =findViewById(R.id.cardView3);

        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(item -> {
            UserMenuSelected(item);
            return false;
        });
        btn_V.setOnClickListener(view -> {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/channel/UC3dzH7JgfIzm2na8QhIIscg"));
            try {
                MainActivity.this.startActivity(webIntent);
            } catch (ActivityNotFoundException ignored) {
            }
        });
        // render to politics pages.
        btn_P.setOnClickListener(view -> {
//            pd.show();
//            pd.setContentView(R.layout.pd_lo);
//            pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Intent Politics_page = new Intent(MainActivity.this, PoliticsActivity.class);
            startActivity(Politics_page);
        });
        // render to News pages.
        btn_N.setOnClickListener(view -> {
//            pd.show();
//            pd.setContentView(R.layout.pd_lo);
//            pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Intent News_page = new Intent(MainActivity.this, NewsActivity.class);
            startActivity(News_page);

        });
        // render to Biography pages.
        btn_B.setOnClickListener(view -> {
//            pd.show();
//            pd.setContentView(R.layout.pd_lo);
//            pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//            Intent Bio_page = new Intent(MainActivity.this, Biography.class);
//            startActivity(Bio_page);
        });
        // Search button
        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, searchActivity.class);
                startActivity(i);
            }
        });
        mRecyclerView = findViewById(R.id.recycler_latest);
        mRecyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,true);
        mRecyclerView.setLayoutManager(manager);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("KKB");
        reference.keepSynced(true);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void UserMenuSelected(MenuItem item){
        if (item.getItemId() == R.id.nav_home){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_politics){
//            pd.show();
//            pd.setContentView(R.layout.pd_lo);
//            pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Intent intent = new Intent(this,PoliticsActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_news){
//            pd.show();
//            pd.setContentView(R.layout.pd_lo);
//            pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Intent intent = new Intent(this,NewsActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_bio){
//            pd.show();
//            pd.setContentView(R.layout.pd_lo);
//            pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//            Intent intent = new Intent(this,Biography.class);
//            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_vid){
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/channel/UC3dzH7JgfIzm2na8QhIIscg"));
            try {
                MainActivity.this.startActivity(webIntent);
            } catch (ActivityNotFoundException ignored) {
            }
        }
        else if (item.getItemId() == R.id.nav_share){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT,"open this app to study beutyfull post http:/play.google.com");
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent,"share via"));
            startActivity(intent);
        }

        else if (item.getItemId() == R.id.exit){
//            System.exit(1);
            this.finishAffinity();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Query firebaseQuery = firebaseDatabase.getReference("KKB").orderByChild("titleTag").limitToLast(2);
        FirebaseRecyclerOptions<Member> options =
                new FirebaseRecyclerOptions.Builder<Member>()
                        .setQuery(firebaseQuery, Member.class)
                        .build();


        FirebaseRecyclerAdapter<Member, Viewholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Member, Viewholder>(options) {


                    @Override
                    protected void onBindViewHolder(@NonNull Viewholder viewHandler, int position, @NonNull Member member) {
                        viewHandler.Lsetdetails(getApplication(), member.getTitle(), member.getContent(), member.getImage_url(),member.getCat(),member.getTitleTag());

                        viewHandler.setOnClicklistener(new Viewholder.ClickListener() {
                            @Override
                            public void onItemclick(View view, int position) {
                                mtitle = getItem(position).getTitle();
                                mcontent = getItem(position).getContent();
                                mcat = getItem(position).getCat();
                                mtitleTag = getItem(position).getTitleTag();
                                mimage = getItem(position).getImage_url();

                                Intent intent = new Intent(MainActivity.this,latestActivity2.class);
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
                                .inflate(R.layout.latestitems, parent, false);
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