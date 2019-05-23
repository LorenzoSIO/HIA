package fr.lorenzocacciato.hialib.components

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import fr.lorenzocacciato.hialib.R
import kotlinx.android.synthetic.main.component_loading_button.view.*

class LoadingButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : ConstraintLayout(context, attrs, defStyle),
    View.OnClickListener {

    private var loadingButtonListener: LoadingButtonListener = object : LoadingButtonListener {
        override fun onEndAnimation() { }
    }

    /**
     * Loading button state
     */
    var isLoading: Boolean = false
        set(newValue) {
            field = newValue
            when (newValue) {
                true -> {
                    cta.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
                    loading_progress_bar_component.visibility = View.VISIBLE
                }
                else -> {
                    cta.setTextColor(ContextCompat.getColor(context!!, R.color.colorWhite))
                    loading_progress_bar_component.visibility = View.GONE
                }
            }
        }

    /**
     * Enable state
     */
    var isEnable: Boolean = false
        set(newValue) {
            field = newValue
            when (newValue) {
                true -> {
                    cta.isEnabled = true
                    cta.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
                }
                else -> {
                    cta.isEnabled = false
                    cta.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGray))
                }
            }
        }


    init {
        LayoutInflater.from(context).inflate(R.layout.component_loading_button, this, true)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it,
                R.styleable.hia_lb_attr, 0, 0)

            val labelTitle = resources.getText(typedArray
                .getResourceId(R.styleable
                    .hia_lb_attr_lb_title,
                    R.string.common_text_empty))

            isEnable = false
            isLoading = false

            cta.text = labelTitle
            cta.setOnClickListener(this)

            typedArray.recycle()
        }
    }

    override fun onClick(view: View?) {
        if(cta.isEnabled)
        {
            // set to is loading enable state
            isLoading = true

            // prevent post delayed start activity
            Handler().postDelayed({
                loadingButtonListener.onEndAnimation()
            }, 1000)

        }
    }

    fun setText(string: String) {
        cta.text = string
    }

    fun setOnLoadingButtonEndAnimationListener(listener: LoadingButtonListener) {
        this.loadingButtonListener = listener
    }

    interface LoadingButtonListener {
        fun onEndAnimation()
    }


}