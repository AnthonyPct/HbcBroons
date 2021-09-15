package com.foyer.hbc.presentation.dashboard.order

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.foyer.hbc.R
import com.foyer.hbc.databinding.OrderComponentBinding
import com.foyer.hbc.domain.data.dashboard.ProductType
import com.foyer.hbc.presentation.dashboard.IDashboardContract

class OrderComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    //    private var listener: NavigationBarListener? = null
    private var binding: OrderComponentBinding
    private var productName: String? = null
    private var productImg: Drawable? = null
    private var shouldShowCheckbox: Boolean = false
    private var productType: ProductType = ProductType.BEER
    private var listener: IDashboardContract.ViewEvent.Order? = null

    ///////////////////////////////////////////////////////////////////////////
    // FACTORY
    ///////////////////////////////////////////////////////////////////////////

    init {
        binding = OrderComponentBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
        getAttributes(attrs)
        initComponentUI()
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private fun getAttributes(attrs: AttributeSet?) {
        // Get attributes
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.OrderComponent,
            0,
            0
        ).apply {
            try {
                productName = getString(R.styleable.OrderComponent_title)
                productImg = getDrawable(R.styleable.OrderComponent_img)
                shouldShowCheckbox = getBoolean(R.styleable.OrderComponent_showCheckbox, false)
                productType =
                    ProductType.values()[getInt(R.styleable.OrderComponent_productType, 0)]
            } finally {
                recycle()
            }
        }
    }

    private fun initComponentUI() {
        binding.orderProductTitle.text = productName
        binding.orderProductImage.setImageDrawable(productImg)
        binding.orderProductAdd.setOnClickListener {
            when (productType) {
                ProductType.BEER -> listener?.onClickAddBeer()
                ProductType.SOFT -> listener?.onClickAddSoft()
                ProductType.EAT -> listener?.onClickAddEat()
            }
        }

        binding.orderProductLess.setOnClickListener {
            when (productType) {
                ProductType.BEER -> listener?.onClickRemoveBeer()
                ProductType.SOFT -> listener?.onClickRemoveSoft()
                ProductType.EAT -> listener?.onClickRemoveEat()
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun bind(orderListener: IDashboardContract.ViewEvent.Order) {
        this.listener = orderListener
    }

    fun updateAmount(newAmount: Double) {
        binding.orderProductPrice.text = newAmount.toString()
    }

    fun reset() {
        binding.orderProductPrice.text = ""
    }
}
