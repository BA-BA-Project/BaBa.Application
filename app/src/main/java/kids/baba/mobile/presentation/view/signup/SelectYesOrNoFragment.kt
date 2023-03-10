package kids.baba.mobile.presentation.view.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import kids.baba.mobile.databinding.FragmentSelectYesOrNoBinding
import kids.baba.mobile.presentation.state.InputBabiesInfoUiState
import kids.baba.mobile.presentation.viewmodel.InputBabiesInfoViewModel


class SelectYesOrNoFragment : Fragment() {
    private var _binding: FragmentSelectYesOrNoBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: InputBabiesInfoViewModel by viewModels(
        ownerProducer = {
            var parent = requireParentFragment()
            while (parent is NavHostFragment) {
                parent = parent.requireParentFragment()
            }
            parent
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectYesOrNoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uiState = viewModel.uiState.value
        if(uiState== InputBabiesInfoUiState.CheckInviteCode){
            binding.btnAnswerYes.setOnClickListener {
                viewModel.setHaveInviteCode(true)
            }

            binding.btnAnswerNo.setOnClickListener {
                viewModel.setHaveInviteCode(false)
            }
        } else if( uiState == InputBabiesInfoUiState.CheckMoreBaby){
            binding.btnAnswerYes.setOnClickListener {
                viewModel.inputMoreBaby(true)
            }

            binding.btnAnswerNo.setOnClickListener {
                viewModel.inputMoreBaby(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}