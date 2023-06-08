package kids.baba.mobile.presentation.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentDeleteUserBinding
import kids.baba.mobile.presentation.adapter.DeleteReasonAdapter
import kids.baba.mobile.presentation.binding.ComposableTopViewData
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.DeleteMemberViewModel

@AndroidEntryPoint
class DeleteMemberFragment : Fragment() {

    private var _binding: FragmentDeleteUserBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: DeleteMemberViewModel by viewModels()

    private lateinit var deleteReasonAdapter: DeleteReasonAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeleteUserBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
        setDeleteMemberBtn()
        setDeleteReasonAdapter()
        collectDeleteReason()
    }




    private fun setBinding() {
        binding.viewmodel = viewModel
        binding.topViewData = ComposableTopViewData {
            findNavController().navigateUp()
        }
        binding.lifecycleOwner = viewLifecycleOwner
    }


    private fun setDeleteReasonAdapter() {
        deleteReasonAdapter = DeleteReasonAdapter {
            viewModel.itemClick(it)
        }
        binding.rvAccountDeleteReason.adapter = deleteReasonAdapter
    }

    private fun collectDeleteReason() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.deleteReasonList.collect{
                deleteReasonAdapter.submitList(it)
            }
        }
    }

    private fun setDeleteMemberBtn() {
        binding.btnDeleteAccount.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/baba-agree/%EA%B3%84%EC%A0%95-%EB%B0%8F-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%82%AD%EC%A0%9C-%EC%9A%94%EC%B2%AD"))
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}