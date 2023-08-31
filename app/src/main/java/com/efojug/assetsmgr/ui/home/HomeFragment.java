package com.efojug.assetsmgr.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private EditText expenseAmountEditText;
    private EditText remarkEditText;
    private Spinner expenseTypeSpinner;
    private List<String> mExpenseType;
    private ArrayAdapter<String> mAdapter;

    private Assets.Type currentSelectedType = Assets.Type.SchoolSupplies;

    private void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        expenseAmountEditText = root.findViewById(R.id.expense_amount_edittext);
        expenseTypeSpinner = root.findViewById(R.id.expense_type_spinner);
        remarkEditText = root.findViewById(R.id.expense_name_edittext);

        mExpenseType = new ArrayList<>();
        for (Assets.Type type : Assets.Type.getEntries()) {
            mExpenseType.add(type.getChinese());
        }

        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mExpenseType);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseTypeSpinner.setAdapter(mAdapter);
        expenseTypeSpinner.setSelection(0, true);
        mAdapter.notifyDataSetChanged();

        expenseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String item = mAdapter.getItem(position);
                currentSelectedType = Assets.Type.fromChinese(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });

//        on add expense button click
        root.findViewById(R.id.add_expense_button).setOnClickListener(v -> {
            // Get the title and content from the EditText views
            String expenseAmount = expenseAmountEditText.getText().toString();

            if (currentSelectedType == null) {
                showToast("请选择要添加的类型");
                return;
            }

            // Save the title and content to the database
            try {
                ((AssetsManager) KoinJavaComponent.get(AssetsManager.class))
                        .addExpenses(new Assets(Float.parseFloat(expenseAmount), currentSelectedType, "", System.currentTimeMillis()));
                showToast("添加成功");
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