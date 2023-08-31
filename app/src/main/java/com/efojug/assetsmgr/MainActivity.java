package com.efojug.assetsmgr;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.efojug.assetsmgr.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        try {
            // 读取 JSON 文件
            String jsonString = readJSONStringFromFile(MainActivity.this, "expenses.json");
            //输出jsonString
///////////////////////////////////////////////////////////////////////////////////////////////
//            mTextView.setText(jsonString);

///////////////////////////////////////////////////////////////////////////////////////////////
            // 修改 JSON 文件
            modifyExpense(MainActivity.this, "expenses.json", "Shopping", 50.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取给定 JSON 文件并将其作为字符串返回
     *
     * @param context  上下文
     * @param filename 文件名
     * @return JSON 字符串
     * @throws IOException
     */
    public static String readJSONStringFromFile(Context context, String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        reader.close();
        return sb.toString();
    }

    /**
     * 修改名为 "Shopping" 的费用的金额，然后写回 JSON 文件
     *
     * @param context     上下文
     * @param filename    文件名
     * @param expenseName 费用名称
     * @param newAmount   修改后的费用金额
     * @throws IOException
     */
    public void modifyExpense(Context context, String filename, String expenseName, double newAmount) throws IOException {
        // 读取 JSON 字符串并将其转换为 List of Expense 对象
        String jsonString = readJSONStringFromFile(context, filename);
        Type listType = new TypeToken<List<Expense>>() {
        }.getType();
        List<Expense> expensesList = new Gson().fromJson(jsonString, listType);

        // 修改 Expense 对象中名为 "Shopping" 的费用的金额
        for (Expense expense : expensesList) {
            if (expense.getName().equals(expenseName)) {
                expense.setAmount(newAmount);
                break;
            }
        }

        // 将 List of Expense 对象转换回 JSON 字符串
        String modifiedJsonString = new Gson().toJson(expensesList);

        // 将修改后的 JSON 字符串写回文件中
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE)));
        writer.write(modifiedJsonString);
        writer.close();
    }
}
