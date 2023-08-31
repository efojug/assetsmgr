package com.efojug.assetsmgr.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.efojug.assetsmgr.R;
import com.efojug.assetsmgr.databinding.FragmentDashboardBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private TextView tvTotalExpense;
    private PieChart pieChart;

    private static final String EXPENSE_DATA = "expense_data";

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tvTotalExpense = root.findViewById(R.id.total_text_view);
        pieChart = root.findViewById(R.id.pie_chart);

        // 设置饼状图的样式和数据
        setupPieChart();

        // 从SharedPreferences中读取支出数据并更新总支出和饼状图
        updateDashboard();

        return root;
    }

    private void setupPieChart() {
        // 配置饼状图的样式
        pieChart.setUsePercentValues(true);
        Description description = new Description();
        description.setText("Expense by Category");
        pieChart.setDescription(description);
        pieChart.setExtraOffsets(5, 5, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.animateY(1000);

        // 配置饼状图的数据
        ///////////////////////////////////////////////////////////////////
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(25.0f, "Food"));
        entries.add(new PieEntry(28.0f, "Housing"));
        entries.add(new PieEntry(17.0f, "Transportation"));
        entries.add(new PieEntry(15.0f, "Entertainment"));
        entries.add(new PieEntry(5.0f, "Healthcare"));
        entries.add(new PieEntry(10.0f, "Other"));
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(Color.rgb(255, 183, 132),
                Color.rgb(255, 148, 94),
                Color.rgb(255, 102, 102),
                Color.rgb(255, 220, 105),
                Color.rgb(148, 159, 177),
                Color.rgb(208, 205, 255));
        dataSet.setValueTextColor(Color.WHITE);
        IPieDataSet iDataSet = dataSet;
        PieData data = new PieData(iDataSet);

        // 将数据设置到饼状图上
        pieChart.setData(data);
    }

    private void updateDashboard() {
        // 从SharedPreferences中读取支出数据
        String expenseDataString = getExpenseDataFromSharedPreferences();

        // 计算并更新总支出
        double totalExpense = calculateTotalExpense(expenseDataString);
        tvTotalExpense.setText(String.format(Locale.getDefault(), "%.2f", totalExpense));

        // 计算并更新饼状图的数据
        Map<String, Double> expenseByCategory = calculateExpenseByCategory(expenseDataString);
        updatePieChartData(expenseByCategory);
    }

    private String getExpenseDataFromSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getString(EXPENSE_DATA, null);
    }

    private void saveExpenseDataToSharedPreferences(String expenseDataJsonString) {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EXPENSE_DATA, expenseDataJsonString);
        editor.apply();
    }

    private double calculateTotalExpense(String expenseDataString) {
        double totalExpense = 0;
        if (expenseDataString != null) {
            try {
                JSONArray expenseData = new JSONArray(expenseDataString);
                for (int i = 0; i < expenseData.length(); i++) {
                    JSONObject expense = expenseData.getJSONObject(i);
                    double amount = expense.getDouble("amount");
                    totalExpense += amount;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return totalExpense;
    }

    private Map<String, Double> calculateExpenseByCategory(String expenseDataString) {
        Map<String, Double> expenseByCategory = new HashMap<>();
        if (expenseDataString != null) {
            try {
                JSONArray expenseData = new JSONArray(expenseDataString);
                for (int i = 0; i < expenseData.length(); i++) {
                    JSONObject expense = expenseData.getJSONObject(i);
                    String category = expense.getString("category");
                    Double amount = expense.getDouble("amount");
                    if (expenseByCategory.containsKey(category)) {
                        amount += expenseByCategory.get(category);
                    }
                    expenseByCategory.put(category, amount);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return expenseByCategory;
    }

    private void updatePieChartData(Map<String, Double> expenseByCategory) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (String category : expenseByCategory.keySet()) {
            Double amount = expenseByCategory.get(category);
            entries.add(new PieEntry(amount.floatValue(), category));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[]{
                Color.rgb(255, 183, 132),
                Color.rgb(255, 148, 94),
                Color.rgb(255, 102, 102),
                Color.rgb(255, 220, 105),
                Color.rgb(148, 159, 177),
                Color.rgb(208, 205, 255),
        });
        dataSet.setValueTextColor(Color.WHITE);
        IPieDataSet iDataSet = dataSet;
        PieData data = new PieData(iDataSet);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    public void addExpense(JSONObject expense) {
        // 读取旧的支出数据
        String expenseDataString = getExpenseDataFromSharedPreferences();

        // 将新的支出数据添加到 JSONArray 中
        JSONArray expenseData;
        if (expenseDataString == null) {
            expenseData = new JSONArray();
        } else {
            try {
                expenseData = new JSONArray(expenseDataString);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Failed to add expense", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        expenseData.put(expense);

        // 将最新的支出数据保存到 SharedPreferences 中
        String newExpenseDataString = expenseData.toString();
        saveExpenseDataToSharedPreferences(newExpenseDataString);

        // 更新总支出和饼状图
        updateDashboard();
        Toast.makeText(getActivity(), "Expense added successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}