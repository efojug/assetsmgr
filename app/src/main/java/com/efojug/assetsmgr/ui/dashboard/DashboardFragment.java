package com.efojug.assetsmgr.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.efojug.assetsmgr.R;
import com.efojug.assetsmgr.databinding.FragmentDashboardBinding;
import com.efojug.assetsmgr.manager.Assets;
import com.efojug.assetsmgr.manager.AssetsManager;
import com.efojug.assetsmgr.ui.notifications.NotificationsViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.koin.java.KoinJavaComponent;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private PieChart pieChart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        pieChart = root.findViewById(R.id.pie_chart);

        List<Assets> assetsList = ((AssetsManager) KoinJavaComponent.get(AssetsManager.class)).getAllExpensesBlock();
        float total = 0f;

        for (Assets assets : assetsList) {
            total += assets.getAmount();
        }

        List<PieEntry> entries = new ArrayList<>();

        for (Assets assets : assetsList) {
            entries.add(new PieEntry((assets.getAmount() / total) * 100, assets.getType().getChinese()));
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
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}