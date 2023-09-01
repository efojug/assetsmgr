package com.efojug.assetsmgr;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.efojug.assetsmgr.databinding.ActivityMainBinding;

/* TODO List
    数据导出
    完善设置页面
    Dashboard左滑删除/编辑
 */

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean isDarkModeEnabled;
    private boolean isAutoDarkModeEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        SharedPreferences sharedPreferences = ((Application) getApplication()).getSharedPreferences();

        // 读取深色模式选项的值
        isDarkModeEnabled = sharedPreferences.getBoolean("dark_mode", false);
        isAutoDarkModeEnabled = sharedPreferences.getBoolean("auto_dark_mode", false);

        // 根据深色模式选项的值设置应用程序的主题
        AppCompatDelegate.setDefaultNightMode(isAutoDarkModeEnabled ?
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM :
                (isDarkModeEnabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO));
    }
}
