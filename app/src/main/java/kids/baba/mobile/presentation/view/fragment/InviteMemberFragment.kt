package kids.baba.mobile.presentation.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentInviteMemberBinding
import kids.baba.mobile.databinding.FragmentInviteResultBinding
import kids.baba.mobile.presentation.view.activity.MainActivity

class InviteMemberFragment : Fragment() {

    private var _binding: FragmentInviteMemberBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnInvite.setOnClickListener {
            val fragment = InviteMemberResultFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInviteMemberBinding.inflate(inflater, container, false)
        return binding.root
    }
}