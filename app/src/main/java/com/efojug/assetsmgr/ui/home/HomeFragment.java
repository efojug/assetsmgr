package com.efojug.assetsmgr.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.efojug.assetsmgr.Expense;
import com.efojug.assetsmgr.R;
import com.efojug.assetsmgr.databinding.FragmentHomeBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Button addExpenseButton;
    private EditText expenseNameEditText;
    private EditText expenseAmountEditText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        expenseNameEditText = root.findViewById(R.id.expense_name_edittext);
        expenseAmountEditText = root.findViewById(R.id.expense_amount_edittext);
        addExpenseButton = root.findViewById(R.id.add_expense_button);

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expenseName = expenseNameEditText.getText().toString();
                String expenseAmount = expenseAmountEditText.getText().toString();

                // Handle adding expense to JSON file here
                addExpenseToJSON(new Expense(expenseName, Double.parseDouble(expenseAmount)));
            }
        });

        return root;
    }

    private void addExpenseToJSON(Expense expense) {
        String jsonString;
        try {
            // Read existing JSON data from file
            FileInputStream inputStream = getActivity().openFileInput("expenses.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            // Convert bytes array into JSON string
            jsonString = new String(buffer, StandardCharsets.UTF_8);

            // Convert JSON string into List of expenses
            Type listType = new TypeToken<List<Expense>>(){}.getType();
            List<Expense> expensesList = new Gson().fromJson(jsonString, listType);

            // Add new expense to the list and save it back to JSON file
            expensesList.add(expense);
            jsonString = new Gson().toJson(expensesList);
            saveDataToFile(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDataToFile(String data) {
        try {
            FileOutputStream outputStream = getActivity().openFileOutput("expenses.json", Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

//
//public class HomeFragment extends Fragment {
//
//    private FragmentHomeBinding binding;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
//
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;
//    }
//

//}