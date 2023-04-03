package kids.baba.mobile.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.BottomSheetBabyListBinding
import kids.baba.mobile.presentation.adapter.BabyAdapter
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.BabyListViewModel

@AndroidEntryPoint
class BabyListBottomSheet : BottomSheetDialogFragment() {
    private var _binding : BottomSheetBabyListBinding? = null
    private val binding
        get() = checkNotNull(_binding){ "binding was accessed outside of view lifecycle" }

    private val viewModel: BabyListViewModel by viewModels()

    private lateinit var myBabyAdapter: BabyAdapter
    private lateinit var othersBabyAdapter: BabyAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetBabyListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBabyList()
    }

    private fun setBabyList() {
        myBabyAdapter = BabyAdapter()
        othersBabyAdapter = BabyAdapter()

        binding.rvMyBabies.adapter = myBabyAdapter
        binding.rvOthersBabies.adapter = othersBabyAdapter
        viewLifecycleOwner.repeatOnStarted {
            viewModel.myBabyList.collect{
                myBabyAdapter.submitList(it)
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.othersBabyList.collect{
                othersBabyAdapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        const val TAG = "BabyListBottomSheet"
    }
}