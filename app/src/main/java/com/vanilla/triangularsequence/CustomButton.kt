package com.vanilla.triangularsequence

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button


/**
 * Created by Ram Mandal on 21/03/2023
 * @System: Apple M1 Pro
 */
class CustomButton : Button, View.OnClickListener {
    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        setOnClickListener(this)
    }

    private var mUserOnClickListener: OnClickListener? = null

    override fun setOnClickListener(l: OnClickListener?) {
        if (l == this)
            super.setOnClickListener(l)
        else mUserOnClickListener = l
    }

    override fun onClick(p0: View?) {
        Log.v("TAG", "Clicked")
        if (mUserOnClickListener != null) {
            mUserOnClickListener!!.onClick(p0)
        }

    }


}