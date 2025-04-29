//package com.example.wavesoffood.Fragment
//
//import android.content.ActivityNotFoundException
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import com.example.wavesoffood.databinding.FragmentSearchBinding
//
//class SearchFragment : Fragment() {
//
//    private var _binding: FragmentSearchBinding? = null
//    private val binding get() = _binding!!  // Ensure binding is not null when accessed
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentSearchBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.buttonAdminDashboard.setOnClickListener {
//                val intent = Intent()
//                intent.setClassName(
//                    "com.example.adminfoodies",
//                    "com.example.adminfoodies.SplashScreenActivity" // Add full package name
//                )
//
//                try {
//                    startActivity(intent)
//                } catch (e: ActivityNotFoundException) {
//                    Toast.makeText(requireContext(), "Admin app is not installed!", Toast.LENGTH_SHORT).show()
//                }
//
//            }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null  // Prevent memory leaks
//    }
//}
