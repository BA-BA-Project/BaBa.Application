package kids.baba.mobile.presentation.view

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.DialogFragmentAlbumDetailBinding
import kids.baba.mobile.domain.model.Album
import kids.baba.mobile.presentation.adapter.AlbumDetailCommentAdapter
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.state.AlbumDetailUiState
import kids.baba.mobile.presentation.viewmodel.AlbumDetailViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AlbumDetailDialog(private val album: Album) : DialogFragment() {

    private var _binding: DialogFragmentAlbumDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private val viewModel: AlbumDetailViewModel by viewModels()

    private lateinit var commentAdapter: AlbumDetailCommentAdapter

    private val imageWidth by lazy {
        binding.cvBabyPhoto.width
    }
    private val imageHeight by lazy {
        binding.cvBabyPhoto.height
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.BABA_AlbumDialogStyle)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
        setCloseBtn()
        setCommentRecyclerView()
        setImgScaleAnim()
        setBabyPhoto()
        setDetailStateCollecter()
        viewModel.album.value = album.toAlbumUiModel()
    }

    private fun setDetailStateCollecter() {
        repeatOnStarted {
            viewModel.albumDetailUiState.collect {
                when (it) {
                    is AlbumDetailUiState.Loading -> {}
                    is AlbumDetailUiState.Like -> {}
                    is AlbumDetailUiState.AddComment -> {}
                    is AlbumDetailUiState.GetLikeDetail -> {}
                    is AlbumDetailUiState.LoadComments -> {}
                    is AlbumDetailUiState.Error -> {}
                    is AlbumDetailUiState.Failure -> {}
                    else -> {}
                }
            }
        }
    }


    private fun setCommentRecyclerView() {
        commentAdapter = AlbumDetailCommentAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvAlbumComment.apply {
            adapter = commentAdapter
            this.layoutManager = layoutManager
            var isReachedTop = true
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0 || (isReachedTop.not() && dy < 0)) {
                        viewModel.setExpended(false)
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (isReachedTop && canScrollVertically(-1).not() && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        viewModel.setExpended(true)
                    }
                    isReachedTop = canScrollVertically(-1).not()
                }
            })
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.albumDetail.collect {
                commentAdapter.submitList(it?.comments)
            }
        }

    }

    private fun setImgScaleAnim() {
        val scaleUpAnim = ValueAnimator.ofFloat(1f, 4f)

        scaleUpAnim.addUpdateListener { animation ->
            val layoutParams = binding.cvBabyPhoto.layoutParams
            val value = animation.animatedValue as Float
            binding.cvBabyPhoto.layoutParams.width = (imageWidth * value / 4).toInt()
            binding.cvBabyPhoto.layoutParams.height = (imageHeight * value / 4).toInt()
            binding.cvBabyPhoto.layoutParams = layoutParams
        }
        scaleUpAnim.duration = 500 // 애니메이션 지속 시간 설정 (1000ms = 1초)
        scaleUpAnim.doOnEnd {
            binding.cbAlbumLike.visibility = View.VISIBLE
        }
        val scaleDownAnim = ValueAnimator.ofFloat(1f, 0.25f)
        scaleDownAnim.addUpdateListener { animation ->
            val layoutParams = binding.cvBabyPhoto.layoutParams
            val value = animation.animatedValue as Float
            layoutParams.width = (imageWidth * value).toInt()
            layoutParams.height = (imageHeight * value).toInt()
            binding.cvBabyPhoto.layoutParams = layoutParams
        }
        scaleDownAnim.duration = 500 // 애니메이션 지속 시간 설정 (1000ms = 1초)
        scaleDownAnim.doOnStart {
            binding.cbAlbumLike.visibility = View.GONE
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.isPhotoExpended.collect {
                if (it != null) {
                    if (it) {
                        scaleUpAnim.start()
                    } else {
                        scaleDownAnim.start()
                    }
                }
            }
        }
    }

    private fun setCloseBtn() {
        binding.btnDialogClose.setOnClickListener {
            dismiss()
        }
    }

    private fun setBabyPhoto() {
        binding.cvBabyPhoto.setOnClickListener {
            viewModel.setExpended(true)
        }
    }

    private fun setBinding() {
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewModel = viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}