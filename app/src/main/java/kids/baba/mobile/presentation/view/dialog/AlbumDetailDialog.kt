package kids.baba.mobile.presentation.view.dialog

import android.animation.ValueAnimator
import android.content.DialogInterface
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.StringRes
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.DialogFragmentAlbumDetailBinding
import kids.baba.mobile.presentation.adapter.AlbumDetailCommentAdapter
import kids.baba.mobile.presentation.adapter.LikeUsersAdapter
import kids.baba.mobile.presentation.event.AlbumConfigEvent
import kids.baba.mobile.presentation.event.AlbumDetailEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.util.notification.DownLoadNotificationManager
import kids.baba.mobile.presentation.view.bottomsheet.AlbumConfigBottomSheet
import kids.baba.mobile.presentation.viewmodel.AlbumDetailViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AlbumDetailDialog(
    private val dismissLister: () -> Unit,
    private val deleteListener: () -> Unit
) : DialogFragment() {

    private var _binding: DialogFragmentAlbumDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private val viewModel: AlbumDetailViewModel by viewModels()

    private lateinit var commentAdapter: AlbumDetailCommentAdapter
    private lateinit var likeUsersAdapter: LikeUsersAdapter

    @Inject
    lateinit var downloadNotificationManager: DownLoadNotificationManager

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
        setCommentRecyclerView()
        setLikeUsersRecyclerView()
        setImgScaleAnim()
        collectAlbumDetail()
        collectEvent()
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect{ event ->
                when(event) {
                    is AlbumDetailEvent.ShowSnackBar -> {
                        showSnackBar(event.message)
                    }
                    is AlbumDetailEvent.ShowAlbumConfig -> {
                        showAlbumConfig()
                    }
                    is AlbumDetailEvent.DismissAlbumDetail -> {
                        dismiss()
                    }
                }

            }
        }
    }

    private fun showSnackBar(@StringRes message: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }


    private fun collectAlbumDetail() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.albumDetailUiState.collect {
                commentAdapter.submitList(it.albumDetail.comments)
                likeUsersAdapter.submitList(it.albumDetail.likeDetail.likeUsersPreview)
            }
        }
    }


    private fun setCommentRecyclerView() {
        binding.btnSend.setOnClickListener {
            viewModel.addComment {
                binding.rvAlbumComment.apply {
                    postDelayed({ smoothScrollToPosition(commentAdapter.itemCount - 1) }, 500)
                }
            }
        }
        commentAdapter = AlbumDetailCommentAdapter(
            itemClick = { comment ->
                viewModel.setTag(comment.name, comment.memberId)
            },
            itemLongClick = { comment, itemView ->
                if (viewModel.checkMyComment(comment)) {
                    val popupMenu = PopupMenu(requireContext(), itemView)
                    popupMenu.inflate(R.menu.comment_menu)
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.comment_delete -> {
                                viewModel.deleteComment(comment.commentId)
                                true
                            }

                            else -> false
                        }
                    }
                    popupMenu.show()
                }
            }
        )
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
    }

    private fun setLikeUsersRecyclerView() {
        likeUsersAdapter = LikeUsersAdapter()
        binding.rvLikeUsers.adapter = likeUsersAdapter
        val overlapWidth = resources.getDimensionPixelSize(R.dimen.overlap_width)
        binding.rvLikeUsers.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    return
                }
                outRect.left = -overlapWidth
            }
        })
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
            binding.btnAlbumLike.visibility = View.VISIBLE
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
            binding.btnAlbumLike.visibility = View.GONE
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


    private fun showAlbumConfig() {
        val bundle = Bundle()
        bundle.putParcelable(
            AlbumConfigBottomSheet.NOW_ALBUM_KEY,
            viewModel.albumDetailUiState.value.albumDetail.album
        )
        val bottomSheet = AlbumConfigBottomSheet { event ->
            when (event) {
                is AlbumConfigEvent.DeleteAlbum -> {
                    dismiss()
                    deleteListener.invoke()
                }

                is AlbumConfigEvent.ShowDownSuccessNotification -> {
                    downloadNotificationManager.showNotification(event.uri)
                }

                else -> {}
            }
        }
        bottomSheet.arguments = bundle
        bottomSheet.show(childFragmentManager, AlbumConfigBottomSheet.TAG)
    }



    private fun setBinding() {
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewModel = viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissLister.invoke()
    }

    companion object {
        const val TAG = "AlbumDetailDialog"
        const val SELECTED_ALBUM_KEY = "SELECTED_ALBUM_KEY"
    }
}