package com.efojug.assetsmgr.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.efojug.assetsmgr.R;
import com.efojug.assetsmgr.databinding.FragmentDashboardBinding;
import com.efojug.assetsmgr.manager.Expenses;
import com.efojug.assetsmgr.manager.AssetsManager;
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

    public void refreshChartData(List<Expenses> expensesList) {
        float total = 0f;

        for (Expenses expenses : expensesList) {
            total += expenses.getAmount();
        }

        totalTextView.setText("总支出: " + total + "元");

        List<PieEntry> entries = new ArrayList<>();

        for (Expenses.Type type : Expenses.Type.values()) {
            AtomicReference<Float> floatReference = new AtomicReference<>(0f);

            expensesList
                    .stream()
                    .filter(expenses -> expenses.getType() == type)
                    .forEach(t -> {
                        floatReference.updateAndGet(v -> v + t.getAmount());
                    });

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

        ((AssetsManager) KoinJavaComponent.get(AssetsManager.class)).getAllExpensesBlock(assets -> {
            refreshChartData(assets);
            return Unit.INSTANCE;
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}