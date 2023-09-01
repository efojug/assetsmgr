package com.efojug.assetsmgr.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.efojug.assetsmgr.R
import com.efojug.assetsmgr.databinding.FragmentDashboardBinding

@Composable
fun ComposeView() {
    Text(text = "SB")
}
class ExpenseList : Fragment(){
    private var binding: FragmentDashboardBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view: View = binding!!.root
        val myComposeView = view.findViewById<ComposeView>(R.id.expenseList)
        myComposeView.setContent {
            ComposeView()
        }
        super.onCreateView(inflater, container, savedInstanceState)
        return view
    }
}