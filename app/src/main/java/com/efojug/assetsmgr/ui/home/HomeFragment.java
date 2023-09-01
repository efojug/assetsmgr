package com.efojug.assetsmgr.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.efojug.assetsmgr.R;
import com.efojug.assetsmgr.databinding.FragmentHomeBinding;
import com.efojug.assetsmgr.manager.Assets;
import com.efojug.assetsmgr.manager.AssetsManager;

import org.koin.java.KoinJavaComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private EditText expenseAmountEditText;
    private EditText expenseNameEditText;
    private EditText remarkEditText;
    private Spinner expenseTypeSpinner;
    private List<String> mExpenseType;
    private ArrayAdapter<String> mAdapter;
    private int mSelectedPosition = 0;
    //    private String mSelectedData = mExpenseType.get(0);
    //他妈的为什么这里会空指针 我操你妈
    private String mSelectedData = mExpenseType != null && !mExpenseType.isEmpty() ? mExpenseType.get(0) : "";
    private Assets.Type currentSelectedType = Assets.Type.SchoolSupplies;

    private void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        //初始化页面
        View root = binding.getRoot();
        expenseAmountEditText = root.findViewById(R.id.expense_amount_edittext);
        expenseTypeSpinner = root.findViewById(R.id.expense_type_spinner);
        remarkEditText = root.findViewById(R.id.expense_name_edittext);
        //创建列表
        mExpenseType = new ArrayList<>();
        for (Assets.Type type : Assets.Type.getEntries()) {
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
                mSelectedPosition = position;
                mSelectedData = mAdapter.getItem(position);
                if (Objects.equals(mSelectedData, "其他")) {
                    expenseNameEditText.setVisibility(View.VISIBLE);
                } else {
                    expenseNameEditText.setVisibility(View.GONE);
                }
                //mSelectedData不可能为null，除非你的傻逼list出问题了
                currentSelectedType = Assets.Type.fromChinese(mSelectedData);
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
            //在onClick时获取 :)
            //默认选择第一项，始终不为null，除非你的傻逼list出问题了
//            if (currentSelectedType == null) {
//                showToast("请选择要添加的类型");
//                return;
//            }

            //保存数据
            try {
                ((AssetsManager) KoinJavaComponent.get(AssetsManager.class))
                        .addExpenses(new Assets(Float.parseFloat(expenseAmount), currentSelectedType, remarkEditText.getText().toString(), System.currentTimeMillis()));
                showToast(mSelectedData);
            } catch (NumberFormatException e) {
                showToast("请输入正确金额");
            }
        });
        return root;
    }

    private void animateView(View v){
        TranslateAnimation ani = new TranslateAnimation(0, 0, 0, 100);
        ani.setDuration(500);
        v.startAnimation(ani);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}