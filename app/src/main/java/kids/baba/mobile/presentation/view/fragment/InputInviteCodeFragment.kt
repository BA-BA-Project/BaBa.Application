package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentInputInvitecodeBinding
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.state.InputCodeState
import kids.baba.mobile.presentation.view.activity.MyPageActivity
import kids.baba.mobile.presentation.viewmodel.InputInviteCodeViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class InputInviteCodeFragment : Fragment() {

    private var _binding: FragmentInputInvitecodeBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: InputInviteCodeViewModel by viewModels()
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputInvitecodeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNavController()
        collectState()
        binding.topAppBar.ivBackButton.setOnClickListener {
            MyPageActivity.instance.bottomNavOn()
            navController.navigateUp()
        }
        binding.btnAddInviteUser.setOnClickListener {
            val inviteCode = binding.inputView.etInput.text.toString()
            viewModel.add(inviteCode)
        }
    }

    private fun collectState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.uiState.collect{
                when(it){
                    is InputCodeState.Idle -> {}
                    is InputCodeState.LoadInfo -> {
                        val response = it.data
                        Log.e("response","$response")
//                        val action = InputInviteCodeFragmentDirections.actionInputInviteFragmentToInputInviteResultFragment(response)
//                        navController.navigate(action)

                    }
                }
            }
        }
    }

    private fun setNavController() {
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
    }
}