package kids.baba.mobile.presentation.view.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentInputEndBinding
import kids.baba.mobile.presentation.event.CreateProfileEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.CreateProfileViewModel

class InputEndFragment : Fragment() {

    private var _binding: FragmentInputEndBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: CreateProfileViewModel by viewModels(
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
        _binding = FragmentInputEndBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repeatOnStarted {
            viewModel.userProfile.collect { userProfile ->
                if (userProfile != null) {
                    binding.btnInputEnd.apply {
                        text = context.getString(R.string.create_profile_complete)
                        setOnClickListener {
                            viewModel.setEvent(CreateProfileEvent.MoveToInputChildInfo(userProfile))
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}