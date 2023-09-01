package com.efojug.assetsmgr.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.efojug.assetsmgr.R;
import com.efojug.assetsmgr.databinding.FragmentHomeBinding;
import com.efojug.assetsmgr.manager.AssetsManager;
import com.efojug.assetsmgr.manager.Expenses;

import org.koin.java.KoinJavaComponent;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private EditText expenseAmountEditText;
    private EditText expenseNameEditText;
    private Spinner expenseTypeSpinner;
    private List<String> mExpenseType;
    private ArrayAdapter<String> mAdapter;
    private Expenses.Type currentSelectedType = Expenses.Type.SchoolSupplies;

    private void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //初始化页面
        expenseAmountEditText = root.findViewById(R.id.expense_amount_edittext);
        expenseTypeSpinner = root.findViewById(R.id.expense_type_spinner);
        //创建列表
        mExpenseType = new ArrayList<>();
        for (Expenses.Type type : Expenses.Type.values()) {
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
                currentSelectedType = Expenses.Type.fromChinese(mAdapter.getItem(position));
                expenseNameEditText.setVisibility(currentSelectedType == Expenses.Type.Other ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //什么也不做，正常用户理论上不会触发这个方法
            }
        });
        //添加onClickListener
        root.findViewById(R.id.add_expense_button).setOnClickListener(v -> {
            // Get the title and content from the EditText views
            String expenseAmount = expenseAmountEditText.getText().toString();

            //保存数据
            try {
                float amount = Float.parseFloat(expenseAmount);
                String remark = expenseNameEditText.getText().toString();
                ((AssetsManager) KoinJavaComponent.get(AssetsManager.class))
                        .addExpenses(new Expenses(amount, currentSelectedType, remark, System.currentTimeMillis()));
                showToast("添加" + currentSelectedType.getChinese() + amount + "元");
                expenseNameEditText.setText("");
                expenseAmountEditText.setText("");
            } catch (NumberFormatException e) {
                showToast("请输入正确金额");
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