package kids.baba.mobile.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentMypageBinding
import kids.baba.mobile.presentation.viewmodel.MyPageViewModel

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private var _binding: FragmentMypageBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    val viewModel: MyPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setBottomSheet()
    }

    private fun initView() {
        binding.viewmodel = viewModel
    }

    private fun setBottomSheet() {
        binding.ivEditKids.setOnClickListener {
            val bundle = Bundle()
    //            bundle.putParcelable(BabyListBottomSheet.SELECTED_BABY_KEY, viewModel.selectedBaby.value)
            val bottomSheet = BabyEditBottomSheet()
            bottomSheet.arguments = bundle
            bottomSheet.show(childFragmentManager, BabyEditBottomSheet.TAG)
        }
    }
}