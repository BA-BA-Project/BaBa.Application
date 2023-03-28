package kids.baba.mobile.presentation.view.film

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.Orientation
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentSelectCardBinding
import kids.baba.mobile.presentation.adapter.CardStyleAdapter
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.SelectCardViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class SelectCardFragment @Inject constructor(

) : Fragment() {

    private var _binding: FragmentSelectCardBinding? = null
    private val binding

        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: SelectCardViewModel by viewModels()

    private lateinit var cardAdapter: CardStyleAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSelectCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewModel.setPreviewImg(binding.ivBabyFrameBaby)
        viewModel.setTitle(binding.tvBabyFrameTitle)

        binding.tbSelectCard.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        cardAdapter = CardStyleAdapter()
        val layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
        binding.rvSelectCards.apply {
            adapter = cardAdapter
            this.layoutManager = layoutManager
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.cardState.collect{
                cardAdapter.submitList(it?.cardStyles)
            }
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}