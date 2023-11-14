package com.efojug.assetsmgr.ui.home;

import static com.efojug.assetsmgr.util.Utils.showSnackbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.efojug.assetsmgr.R;
import com.efojug.assetsmgr.databinding.FragmentHomeBinding;
import com.efojug.assetsmgr.manager.Expense;
import com.efojug.assetsmgr.manager.ExpenseManager;
import com.google.android.material.textfield.TextInputEditText;

import org.koin.java.KoinJavaComponent;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TextInputEditText expenseAmountEditText;
    private TextInputEditText incomeAmountEditText;
    private EditText expenseNameEditText;
    private Spinner expenseTypeSpinner;
    private List<String> mExpenseType;
    private ArrayAdapter<String> mAdapter;
    private Expense.Type currentSelectedType = Expense.Type.Study;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //初始化页面
        expenseAmountEditText = root.findViewById(R.id.expense_amount_edittext);
        incomeAmountEditText = root.findViewById(R.id.income_amount_edittext);
        expenseTypeSpinner = root.findViewById(R.id.expense_type_spinner);
        //创建列表
        mExpenseType = new ArrayList<>();
        for (Expense.Type type : Expense.Type.values()) {
            mExpenseType.add(type.getChinese());
        }
        //创建适配器
        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mExpenseType);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseTypeSpinner.setAdapter(mAdapter);
        expenseTypeSpinner.setSelection(0, true);
        mAdapter.notifyDataSetChanged();
        expenseNameEditText = root.findViewById(R.id.expense_name_edittext);
        expenseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                currentSelectedType = Expense.Type.fromChinese(mAdapter.getItem(position));
                if (currentSelectedType == Expense.Type.Other) {
                    expenseNameEditText.setVisibility(View.VISIBLE);
                } else {
                    expenseNameEditText.setText("");
                    expenseNameEditText.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //什么也不做，正常用户理论上不会触发这个方法
            }
        });
        //添加onClickListener 支出部分
        root.findViewById(R.id.add_expense_button).setOnClickListener(v -> {
            // Get the title and content from the EditText views
            String expenseAmount = expenseAmountEditText.getText().toString();
            //保存数据
            try {
                float amount = Float.parseFloat(expenseAmount);
                if ((float) ((int) (amount * 100)) / 100f <= 0) throw new Exception();
                String remark = expenseNameEditText.getText().toString();
                ((ExpenseManager) KoinJavaComponent.get(ExpenseManager.class)).addExpenses(new Expense(amount, currentSelectedType, remark, System.currentTimeMillis()));
                showSnackbar(getView(), "支出" + currentSelectedType.getChinese() + amount + "元");
                expenseNameEditText.setText("");
                expenseAmountEditText.setText("");
            } catch (Exception e) {
                showSnackbar(getView(), "请输入正确金额");
            }
        });

        //添加点击监听器 收入部分
        root.findViewById(R.id.add_income_button).setOnClickListener(view -> {
            String incomeAmount = incomeAmountEditText.getText().toString();
            try {
                float amount = Float.parseFloat(incomeAmount);
                if ((float) ((int) (amount * 100)) / 100f <= 0) throw new Exception();
                ((ExpenseManager) KoinJavaComponent.get(ExpenseManager.class)).addExpenses(new Expense(amount, Expense.Type.Income, "", System.currentTimeMillis()));
                showSnackbar(getView(), "收入" + currentSelectedType.getChinese() + amount + "元");
                incomeAmountEditText.setText("");
            } catch (Exception e) {
                showSnackbar(getView(), "请输入正确金额");
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}