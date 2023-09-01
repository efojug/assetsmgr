package com.efojug.assetsmgr.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.compose.ui.platform.ComposeView;
import androidx.fragment.app.Fragment;

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
    private PieChart pieChart;
    private TextView totalTextView;

    public void refreshChartData(List<Expense> expenseList) {
        float total = 0f;

        for (Expense expense : expenseList) {
            total += expense.getAmount();
        }

        totalTextView.setText("总支出: " + total + "元");

        List<PieEntry> entries = new ArrayList<>();

        for (Expense.Type type : Expense.Type.values()) {
            AtomicReference<Float> floatReference = new AtomicReference<>(0f);

            expenseList
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
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        pieChart = root.findViewById(R.id.pie_chart);
        totalTextView = root.findViewById(R.id.total_text_view);

        ((ExpenseManager) KoinJavaComponent.get(ExpenseManager.class)).getAllExpensesBlock(assets -> {
            refreshChartData(assets);
            return Unit.INSTANCE;
        });

        ComposeView composeView = (ComposeView) root.findViewById(R.id.expense_list);
        ExpenseListKt.bindView(this, composeView);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}