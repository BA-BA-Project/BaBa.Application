package kids.baba.mobile.presentation.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kids.baba.mobile.databinding.FragmentMemberInviteResultBinding
import kids.baba.mobile.presentation.view.activity.MainActivity

class InviteMemberResultFragment : Fragment() {

    private var _binding: FragmentMemberInviteResultBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnComplete.setOnClickListener {
            binding.btnComplete.setOnClickListener {
                requireActivity().startActivity(
                    Intent(
                        requireContext(),
                        MainActivity::class.java
                    ).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    })
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberInviteResultBinding.inflate(inflater, container, false)
        return binding.root
    }
}