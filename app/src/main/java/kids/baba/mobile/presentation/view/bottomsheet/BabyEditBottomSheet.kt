package kids.baba.mobile.presentation.view.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kids.baba.mobile.databinding.BottomSheetEditBabyBinding
import kids.baba.mobile.presentation.view.activity.MyPageActivity

class BabyEditBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetEditBabyBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivAddBaby.setOnClickListener {
            requireActivity().startActivity(Intent(requireContext(), MyPageActivity::class.java).putExtra("next","addBaby"))
        }
        binding.ivInputInviteCode.setOnClickListener {
            requireActivity().startActivity(Intent(requireContext(), MyPageActivity::class.java).putExtra("next","invite"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetEditBabyBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "BabyEditBottomSheet"
        const val SELECTED_BABY_KEY = "SELECTED_BABY"
    }
}