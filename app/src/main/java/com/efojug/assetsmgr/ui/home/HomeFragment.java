package com.efojug.assetsmgr.ui.home;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.efojug.assetsmgr.R;
import com.efojug.assetsmgr.databinding.FragmentHomeBinding;
import com.efojug.assetsmgr.manager.AssetsManager;

import org.koin.java.KoinJavaComponent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private EditText expenseNameEditText;
    private EditText expenseAmountEditText;

    private void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    private String getDate(String dateType) {
        Calendar cal = Calendar.getInstance();
        switch (dateType) {
            case "year":
                return String.valueOf(cal.get(Calendar.YEAR));
            case "month":
                return String.valueOf(cal.get(Calendar.MONTH) + 1);
            case "day":
                return String.valueOf(cal.get(Calendar.DATE));
            case "hour":
                return String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
            case "minute":
                return String.valueOf(cal.get(Calendar.MINUTE));
            default:
                throw new IllegalArgumentException("Invalid Input Type");
        }
    }

    private File getExpenseDataFile() {
        String time = getDate("year") + " - " + getDate("month");
        File expenseDataFile = new File(Environment.getExternalStorageDirectory(), time);
        if (!expenseDataFile.exists()) {
            try {
                expenseDataFile.createNewFile();
            } catch (IOException e) {
                showToast("创建文件失败， " + e);
            }
        }
        return expenseDataFile;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        expenseNameEditText = root.findViewById(R.id.expense_name_edittext);
        expenseAmountEditText = root.findViewById(R.id.expense_amount_edittext);
        root.findViewById(R.id.add_expense_button).setOnClickListener(this);
//        getExpenseDataFile();
        return root;
    }

    public void saveExpenseData(String name, String amount) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(getDate("year") + " - " + getDate("month"), true));
        // 2-16:43 [消费] <20.2>
        try {
            Double.parseDouble(amount);
            pw.println(getDate("day") + "-" + getDate("hour") + ":" + getDate("minute") + " [" + name + "] <" + amount + ">");
            pw.flush();
        } catch (NumberFormatException e) {
            showToast("请输入正确金额");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        // Get the title and content from the EditText views
        String expenseName = expenseNameEditText.getText().toString();
        String expenseAmount = expenseAmountEditText.getText().toString();

        // Save the title and content to the database
//        saveExpenseData(expenseName, expenseAmount);
        try {
            ((AssetsManager) KoinJavaComponent.get(AssetsManager.class)).addExpenses(expenseName, Float.parseFloat(expenseAmount));
            showToast("添加成功");
        } catch (NumberFormatException e) {
            showToast("请输入正确金额");
        }
    }
}