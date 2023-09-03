package com.efojug.assetsmgr.ui.dashboard;

import static com.efojug.assetsmgr.util.Utils.showSnackbar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.compose.ui.platform.ComposeView;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.efojug.assetsmgr.R;
import com.efojug.assetsmgr.databinding.FragmentDashboardBinding;
import com.efojug.assetsmgr.manager.ExpenseManager;
import com.efojug.assetsmgr.manager.Expense;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.koin.java.KoinJavaComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import kotlin.Unit;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private float total = 0f;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ProgressBar money_progress = root.findViewById(R.id.money_progress);
        PieChart pieChart = root.findViewById(R.id.pie_chart);
        TextView totalTextView = root.findViewById(R.id.total_text_view);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        ((ExpenseManager) KoinJavaComponent.get(ExpenseManager.class)).getAllExpensesBlock(assets -> {
            for (Expense expense : assets) {
                total += expense.getAmount();
            }

            totalTextView.setText("支出" + total + "元");
            if (sharedPreferences.getBoolean("calc_money", false)) {
                if (!sharedPreferences.getString("month_money", "0").equals("0")) {
                    try {
                        totalTextView.setText("总额: " + Integer.parseInt(sharedPreferences.getString("month_money", "0")) + "元  " + totalTextView.getText() + "  剩余" + (Integer.parseInt(sharedPreferences.getString("month_money", "0")) - total) + "元");
                    } catch (Exception e) {
                        showSnackbar(getView(), "未正确配置生活费");
                    }
                } else showSnackbar(getView(), "未正确配置生活费");
            }

            List<PieEntry> entries = new ArrayList<>();

            for (Expense.Type type : Expense.Type.values()) {
                AtomicReference<Float> floatReference = new AtomicReference<>(0f);

                assets
                        .stream()
                        .filter(expense -> expense.getType() == type)
                        .forEach(t -> floatReference.updateAndGet(v -> v + t.getAmount()));

                if (floatReference.get() > 0f) {
                    entries.add(new PieEntry((floatReference.get() / total) * 100, type.getChinese()));
                }
            }

            // 设置饼图样式
            PieDataSet pieDataSet = new PieDataSet(entries, "资源比例");
            pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            PieData pieData = new PieData(pieDataSet);
            pieData.setValueFormatter(new PercentFormatter(pieChart));
            pieData.setValueTextSize(12f);
            pieData.setValueTextColor(Color.WHITE);
            pieChart.setData(pieData);
            pieChart.getDescription().setEnabled(false);
            pieChart.getLegend().setEnabled(false);
            pieChart.setUsePercentValues(true);
            pieChart.invalidate();
            pieChart.setDrawHoleEnabled(false);

            return Unit.INSTANCE;
        });
        ComposeView composeView = root.findViewById(R.id.expense_list);
        ExpenseListKt.bindView(this, composeView);
        if (sharedPreferences.getBoolean("calc_money", false)) {
            new Handler().postDelayed(() -> {
                if (!sharedPreferences.getString("month_money", "0").equals("0")) {
                    try {
                        money_progress.setMax(Integer.parseInt(sharedPreferences.getString("month_money", "")) * 100);
                        money_progress.setVisibility(View.VISIBLE);
                        money_progress.setProgress((int) (total * 100), true);
                    } catch (Exception e) {
                        showSnackbar(getView(), "未正确配置生活费");
                    }
                } else {
                    money_progress.setVisibility(View.GONE);
                    showSnackbar(getView(), "未正确配置生活费");
                }
            }, 50); // 延时获取
        } else {
            money_progress.setVisibility(View.GONE);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}