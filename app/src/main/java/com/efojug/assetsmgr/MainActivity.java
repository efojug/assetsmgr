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
    Dashboard 右上角调整月份，以月份显示图表 -> efojug
    Dashboard ExpenseList 左滑修改和删除 -> DeSu, efojug
    Dashboard 优化 ExpenseList 的margin -> DeSu
    Dashboard 添加上月结转 -> efojug
    Settings 完善结转 -> efojug, DeSu，修改开支类型 -> efojug, DeSu，清除生活费记录 -> efojug
    Main 月初提醒获取生活费 -> efojug
    Main 数据导出
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.efojug.assetsmgr.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        SharedPreferences sharedPreferences = ((Application) getApplication()).getSharedPreferences();

        // 读取深色模式选项的值
        boolean isDarkModeEnabled = sharedPreferences.getBoolean("dark_mode", false);
        boolean isAutoDarkModeEnabled = sharedPreferences.getBoolean("auto_dark_mode", false);

        // 根据深色模式选项的值设置应用程序的主题
        AppCompatDelegate.setDefaultNightMode(isAutoDarkModeEnabled ?
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM :
                (isDarkModeEnabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO));
    }
}
