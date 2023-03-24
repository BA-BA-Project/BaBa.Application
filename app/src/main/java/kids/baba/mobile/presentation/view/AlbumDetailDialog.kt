package kids.baba.mobile.presentation.view

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.DialogFragmentAlbumDetailBinding
import kids.baba.mobile.presentation.adapter.AlbumDetailCommentAdapter
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.AlbumDetailViewModel

@AndroidEntryPoint
class AlbumDetailDialog : DialogFragment() {

    private var _binding: DialogFragmentAlbumDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private val viewModel: AlbumDetailViewModel by viewModels()

    private lateinit var commentAdapter: AlbumDetailCommentAdapter

    private var imageWidth: Int = 0
    private var imageHeight: Int = 0

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
    }

    private fun getImgSize() {
        imageWidth = binding.cvBabyPhoto.width
        imageHeight = binding.cvBabyPhoto.height
    }

    private fun setCommentRecyclerView() {
        commentAdapter = AlbumDetailCommentAdapter()
        binding.rvAlbumComment.adapter = commentAdapter

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
            binding.cvBabyPhoto.layoutParams.width = (imageWidth * value).toInt()
            binding.cvBabyPhoto.layoutParams.height = (imageHeight * value).toInt()
            binding.cvBabyPhoto.layoutParams = layoutParams
        }
        scaleUpAnim.duration = 500 // 애니메이션 지속 시간 설정 (1000ms = 1초)

        val scaleDownAnim = ValueAnimator.ofFloat(1f, 0.25f)
        scaleDownAnim.addUpdateListener { animation ->
            val layoutParams = binding.cvBabyPhoto.layoutParams
            val value = animation.animatedValue as Float
            layoutParams.width = (imageWidth * value).toInt()
            layoutParams.height = (imageHeight * value).toInt()
            binding.cvBabyPhoto.layoutParams = layoutParams
        }
        scaleDownAnim.duration = 500 // 애니메이션 지속 시간 설정 (1000ms = 1초)


        viewLifecycleOwner.repeatOnStarted {
            viewModel.isPhotoExpended.collect {
                getImgSize()
                if (it) {
                    scaleUpAnim.start()
                    binding.cbAlbumLike.visibility = View.VISIBLE
                } else {
                    scaleDownAnim.start()
                    binding.cbAlbumLike.visibility = View.GONE
                }
            }
        }
    }

    private fun setCloseBtn() {
        binding.btnDialogClose.setOnClickListener {
            dismiss()
        }
        binding.tvAlbumDate.setOnClickListener {
            viewModel.setExpended()
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