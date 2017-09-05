package online.himakeit.skylarkdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import online.himakeit.skylarkdemo.fragment.ShapeFragment;
import online.himakeit.skylarkdemo.fragment.SQLiteFragment;
import online.himakeit.skylarkdemo.fragment.SearchFragment;
import online.himakeit.skylarkdemo.fragment.TestFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    ArrayList<Fragment> fragmentArrayList;
    ShapeFragment shapeFragment;
    SQLiteFragment SQLiteFragment;
    SearchFragment searchFragment;
    TestFragment testFragment;

    Toolbar toolbar;
    int preIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO: 2017/8/11 ToolBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // TODO: 2017/8/11 初始化fargment
        initFragment();
        setToolBarTitle("Search");
        // TODO: 2017/8/11 默认加载一个fragment
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.fl_main_container, fragmentArrayList.get(0));
//        fragmentTransaction.commit();
        // TODO: 2017/8/11 第一次加载不能使用loadFragment方法 不知为何?
        // TODO: 2017/9/5  第一次加载不能使用loadFragment()方法是因为，在第一次时index和preIndex都为0，不能先hide在add同一个fragment，现在已修复
        loadFragment(0);
    }

    private void setToolBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentArrayList = new ArrayList<Fragment>();
        shapeFragment = new ShapeFragment();
        SQLiteFragment = new SQLiteFragment();
        searchFragment = new SearchFragment();
        testFragment = new TestFragment();

        fragmentArrayList.add(searchFragment);
        fragmentArrayList.add(SQLiteFragment);
        fragmentArrayList.add(shapeFragment);
        fragmentArrayList.add(testFragment);
    }

    public void loadFragment(int index) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment = fragmentArrayList.get(index);
        Fragment preFragment = fragmentArrayList.get(preIndex);

        if (!fragment.isAdded()) {
            // TODO: 2017/9/5 修复第一次不能使用loadFragment方法加载fragment
            if (index == preIndex) {
                fragmentTransaction.add(R.id.fl_main_container, fragment);
            } else {
                fragmentTransaction.hide(preFragment).add(R.id.fl_main_container, fragment);
            }
        } else {
            fragmentTransaction.hide(preFragment).show(fragment);
        }

        fragmentTransaction.commit();
        preIndex = index;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            loadFragment(0);
            setToolBarTitle("Search");
        } else if (id == R.id.nav_gallery) {
            loadFragment(1);
            setToolBarTitle("SQLite");
        } else if (id == R.id.nav_slideshow) {
            loadFragment(2);
            setToolBarTitle("Shape");
        } else if (id == R.id.nav_manage) {
            loadFragment(3);
            setToolBarTitle("Tools");
        } else if (id == R.id.nav_share) {
            loadFragment(3);
            setToolBarTitle("Share");
        } else if (id == R.id.nav_send) {
            loadFragment(3);
            setToolBarTitle("Send");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
