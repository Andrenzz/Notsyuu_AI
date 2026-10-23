package id.notsyuu.assistant

import android.os.Bundle

class PopupActivity : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val density=resources.displayMetrics.density
        val width=minOf((320*density).toInt(),resources.displayMetrics.widthPixels-(24*density).toInt())
        val height=minOf((350*density).toInt(),(resources.displayMetrics.heightPixels*0.48).toInt())
        window.setLayout(width,height)
        window.setGravity(android.view.Gravity.BOTTOM or android.view.Gravity.CENTER_HORIZONTAL)
        window.attributes=window.attributes.apply { y=(88*density).toInt() }
        window.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT))
    }
}
