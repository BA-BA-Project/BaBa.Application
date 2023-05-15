package kids.baba.mobile.presentation.view.film

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentSelectCardBinding
import kids.baba.mobile.presentation.adapter.CardStyleAdapter
import kids.baba.mobile.presentation.event.PostAlbumEvent
import kids.baba.mobile.presentation.extension.CardDetailsLookup
import kids.baba.mobile.presentation.extension.RecyclerViewIdKeyProvider
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.FilmViewModel
import kids.baba.mobile.presentation.viewmodel.SelectCardViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SelectCardFragment @Inject constructor(

) : Fragment(), CardStyleAdapter.OnItemClickListener {

    private val tag = "SelectCardFragment"
    private var _binding: FragmentSelectCardBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: SelectCardViewModel by viewModels()
    private val activityViewModel: FilmViewModel by activityViewModels()

    private lateinit var cardAdapter: CardStyleAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSelectCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.nowDate.value = activityViewModel.nowDate.value

        binding.tbSelectCard.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        cardAdapter = CardStyleAdapter(this)
        val layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
        val recyclerView = binding.rvSelectCards
        recyclerView.apply {
            adapter = cardAdapter
            this.layoutManager = layoutManager
        }

        val cardSelectionTracker = SelectionTracker.Builder(
            "cardSelection",
            recyclerView,
            RecyclerViewIdKeyProvider(recyclerView),
            CardDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectSingleAnything()).build()

        cardAdapter.setSelectionTracker(cardSelectionTracker)

        collectState()

    }

    private fun collectState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.cardState.collect {
                cardAdapter.submitList(it.toList())
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is PostAlbumEvent.ShowSnackBar -> showSnackBar(event.text)
                    is PostAlbumEvent.MoveToMain -> activityViewModel.isPostAlbum()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int) {
        viewModel.onCardSelected(position)
    }

    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

}