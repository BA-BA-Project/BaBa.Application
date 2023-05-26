package kids.baba.mobile.presentation.view.dialog

import android.animation.ValueAnimator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.app.NotificationCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.DialogFragmentAlbumDetailBinding
import kids.baba.mobile.presentation.adapter.AlbumDetailCommentAdapter
import kids.baba.mobile.presentation.event.AlbumConfigEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.state.AlbumDetailUiState
import kids.baba.mobile.presentation.view.bottomsheet.AlbumConfigBottomSheet
import kids.baba.mobile.presentation.viewmodel.AlbumDetailViewModel
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AlbumDetailDialog : DialogFragment() {

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
        setAlbumConfigBtn()
        setBabyPhoto()
        setDetailStateCollecter()
        fetchData()
    }
    private fun setDetailStateCollecter() {
        repeatOnStarted {
            viewModel.albumDetailUiState.collect {
                when (it) {
                    is AlbumDetailUiState.Loading -> {}
                    is AlbumDetailUiState.Like -> fetchData()
                    is AlbumDetailUiState.AddComment -> fetchData()
                    is AlbumDetailUiState.Error -> fetchData()
                    is AlbumDetailUiState.Failure -> {}
                    else -> {}
                }
            }
        }
    }

    private fun fetchData() {
        viewModel.fetch()
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

    private fun setAlbumConfigBtn() {
        binding.btnAlbumConfig.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(AlbumConfigBottomSheet.NOW_ALBUM_KEY, viewModel.album.value)
            val bottomSheet = AlbumConfigBottomSheet{ event ->
                when(event){
                    is AlbumConfigEvent.DeleteAlbum -> dismiss()
                    is AlbumConfigEvent.ShowDownSuccessNotification -> {
                        createNotification(event.uri)
                    }

                    else -> {}
                }
            }
            bottomSheet.arguments = bundle
            bottomSheet.show(childFragmentManager, AlbumConfigBottomSheet.TAG)
        }
    }

    private fun createNotification(uri: Uri) {
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(notificationChannel)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "image/*")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setContentTitle(getString(R.string.save_photo_noti_title))
            .setSmallIcon(R.drawable.ic_baba_logo)
            .setContentText(getString(R.string.save_photo_noti_body))
            .setContentIntent(pendingIntent)
            .build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }


    private fun setBabyPhoto() {
        binding.cvBabyPhoto.setOnClickListener {
            viewModel.setExpended(true)
        }
    }

    private fun setBinding() {
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewModel = viewModel
        binding.dateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "AlbumDetailDialog"
        const val SELECTED_ALBUM_KEY = "SELECTED_ALBUM_KEY"
        const val CHANNEL_ID = "PHOTO_DOWNLOAD_SUCCESS_ID"
        const val CHANNEL_NAME = "PHOTO_DOWNLOAD_SUCCESS"
        const val NOTIFICATION_ID = 1

    }
}