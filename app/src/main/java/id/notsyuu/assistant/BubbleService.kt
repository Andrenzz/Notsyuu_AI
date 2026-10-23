package id.notsyuu.assistant

import android.app.*
import android.content.*
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.GradientDrawable
import android.os.IBinder
import android.provider.Settings
import android.view.*
import android.widget.TextView
import kotlin.math.abs

class BubbleService : Service() {
    private var bubble: TextView? = null
    private lateinit var wm: WindowManager
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onCreate() {
        super.onCreate(); if (!Settings.canDrawOverlays(this)) { stopSelf(); return }
        val nm = getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(NotificationChannel("bubble", "Tombol mengambang", NotificationManager.IMPORTANCE_LOW))
        val stop = PendingIntent.getService(this, 1, Intent(this, BubbleService::class.java).setAction("stop"), PendingIntent.FLAG_IMMUTABLE)
        val open = PendingIntent.getActivity(this, 1, Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE)
        startForeground(7, Notification.Builder(this, "bubble").setContentTitle("Notsyuu siap dipanggil").setContentText("Mikrofon mati • Ketuk N untuk bicara").setSmallIcon(R.drawable.ic_notsyuu).setContentIntent(open).addAction(Notification.Action.Builder(null, "Stop & sembunyikan", stop).build()).build())
        wm = getSystemService(WindowManager::class.java)
        val size = (52 * resources.displayMetrics.density).toInt()
        val prefs = Data(this).prefs
        val p = WindowManager.LayoutParams(size, size, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT).apply { gravity = Gravity.TOP or Gravity.LEFT; x = prefs.getInt("bubbleX", 0); y = prefs.getInt("bubbleY", 350) }
        val view = TextView(this).apply {
            text = "N"; textSize = 23f; gravity = Gravity.CENTER; setTextColor(Color.WHITE); alpha = 0.85f
            contentDescription = "Notsyuu. Ketuk untuk bicara. Tekan lama untuk Stop. Geser untuk pindah."
            background = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(0xEE6070C5.toInt(), 0xDD26374F.toInt())).apply { cornerRadius = size.toFloat(); setStroke(2, 0xBBAADFFF.toInt()) }
            elevation = 12f
        }
        var downX = 0f; var downY = 0f; var baseX = 0; var baseY = 0; var started = 0L
        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> { downX = event.rawX; downY = event.rawY; baseX = p.x; baseY = p.y; started = System.currentTimeMillis(); view.alpha = 1f }
                MotionEvent.ACTION_MOVE -> { p.x = baseX + (event.rawX-downX).toInt(); p.y = (baseY + (event.rawY-downY).toInt()).coerceIn(0, resources.displayMetrics.heightPixels-size); wm.updateViewLayout(view,p) }
                MotionEvent.ACTION_UP -> {
                    if (abs(event.rawX-downX) < 12 && abs(event.rawY-downY) < 12) {
                        if (System.currentTimeMillis()-started > 650) { LocalBrain.stop(); sendBroadcast(Intent("id.notsyuu.assistant.STOP").setPackage(packageName)); Data(this).log("Stop dari tombol mengambang"); view.text = "■"; view.postDelayed({ view.text = "N" }, 900) }
                        else startActivity(Intent(this, PopupActivity::class.java).putExtra("voice", true).putExtra("screen",ScreenService.current?.snapshot() ?: "").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP))
                    }
                    p.x = if (p.x + size/2 < resources.displayMetrics.widthPixels/2) 0 else resources.displayMetrics.widthPixels-size
                    wm.updateViewLayout(view,p); prefs.edit().putInt("bubbleX",p.x).putInt("bubbleY",p.y).apply(); view.alpha = 0.65f
                }
            }; true
        }
        bubble = view; wm.addView(view,p)
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int { if (intent?.action == "stop") { LocalBrain.stop(); stopSelf() }; return START_NOT_STICKY }
    override fun onDestroy() { bubble?.let { wm.removeView(it) }; super.onDestroy() }
}
