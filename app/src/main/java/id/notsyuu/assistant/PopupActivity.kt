package id.notsyuu.assistant

import android.os.Bundle

class PopupActivity : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setLayout((resources.displayMetrics.widthPixels*0.94).toInt(),(resources.displayMetrics.heightPixels*0.76).toInt())
        window.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT))
    }
}
