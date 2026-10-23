package id.notsyuu.assistant

import android.animation.ValueAnimator
import android.app.*
import android.content.*
import android.graphics.*
import android.os.IBinder
import android.provider.Settings
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import kotlin.math.abs

class BubbleService : Service() {
    private lateinit var wm:WindowManager
    private var bubble:ClayBubble?=null
    private var target:ClayBubble?=null
    private val retiring=mutableSetOf<View>()
    private var motion:ValueAnimator?=null
    private var params:WindowManager.LayoutParams?=null
    private var targetParams:WindowManager.LayoutParams?=null
    private val density get()=resources.displayMetrics.density
    private fun dp(n:Float)=(n*density).toInt()
    private fun animationDuration(ms:Long)=if(ValueAnimator.areAnimatorsEnabled()) ms else 0L
    override fun onBind(intent:Intent?):IBinder?=null
    override fun onCreate() {
        super.onCreate();wm=getSystemService(WindowManager::class.java)
        getSystemService(NotificationManager::class.java).createNotificationChannel(NotificationChannel("bubble","Kontrol bubble Andrea AI",NotificationManager.IMPORTANCE_LOW))
        startForeground(7,notification())
    }
    private fun action(name:String,id:Int)=PendingIntent.getService(this,id,Intent(this,BubbleService::class.java).setAction(name),PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    private fun notification():Notification {
        val open=PendingIntent.getActivity(this,1,Intent(this,MainActivity::class.java),PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val hidden=bubble==null
        return Notification.Builder(this,"bubble").setContentTitle("Andrea AI").setContentText(if(hidden) "Bubble disembunyikan • ketuk Munculkan bubble" else "Mikrofon mati saat siaga • seret ke × untuk sembunyikan")
            .setSmallIcon(R.drawable.ic_notsyuu).setOngoing(true).setContentIntent(open)
            .addAction(Notification.Action.Builder(null,if(hidden) "Munculkan bubble" else "Sembunyikan bubble",action(if(hidden) "show" else "hide",2)).build())
            .addAction(Notification.Action.Builder(null,"Matikan asisten",action("stop",3)).build()).build()
    }
    private fun updateNotification() { getSystemService(NotificationManager::class.java).notify(7,notification()) }
    private fun screen()=wm.currentWindowMetrics.bounds
    private fun layout(size:Int)=WindowManager.LayoutParams(size,size,WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,PixelFormat.TRANSLUCENT).apply { gravity=Gravity.TOP or Gravity.LEFT }
    private fun showBubble() {
        if(bubble!=null)return
        if(!Settings.canDrawOverlays(this)) { updateNotification();return }
        val size=dp(56f);val bounds=screen();val prefs=Data(this).prefs
        val p=layout(size).apply { x=prefs.getInt("bubbleX",dp(4f)).coerceIn(0,(bounds.width()-size).coerceAtLeast(0));y=prefs.getInt("bubbleY",bounds.height()/3).coerceIn(dp(32f),(bounds.height()-size-dp(32f)).coerceAtLeast(dp(32f))) }
        val view=ClayBubble(false).apply { contentDescription="Andrea AI. Ketuk untuk bicara. Geser ke tanda silang bawah untuk sembunyikan. Tekan lama untuk Stop." }
        var downX=0f;var downY=0f;var baseX=0;var baseY=0;var started=0L;var dragging=false
        view.setOnTouchListener { _,event ->
            when(event.actionMasked) {
                MotionEvent.ACTION_DOWN -> { motion?.cancel();view.animate().cancel();downX=event.rawX;downY=event.rawY;baseX=p.x;baseY=p.y;started=System.currentTimeMillis();dragging=false;view.animate().alpha(1f).scaleX(0.96f).scaleY(0.96f).setDuration(animationDuration(100)).start() }
                MotionEvent.ACTION_MOVE -> {
                    val dx=event.rawX-downX;val dy=event.rawY-downY
                    if(!dragging && (abs(dx)>dp(6f)||abs(dy)>dp(6f))) { dragging=true;showTarget() }
                    if(dragging) {
                        p.x=(baseX+dx.toInt()).coerceIn(0,(screen().width()-size).coerceAtLeast(0));p.y=(baseY+dy.toInt()).coerceIn(0,(screen().height()-size).coerceAtLeast(0))
                        wm.updateViewLayout(view,p)
                        val hovering=inTarget(p,size)
                        target?.highlight=hovering
                        target?.animate()?.scaleX(if(hovering)1.12f else 1f)?.scaleY(if(hovering)1.12f else 1f)?.setDuration(animationDuration(120))?.start()
                        view.animate().scaleX(if(hovering)0.78f else 1.02f).scaleY(if(hovering)0.78f else 1.02f).setDuration(animationDuration(100)).start()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if(dragging && inTarget(p,size)) { hideBubble(true) }
                    else {
                        hideTarget()
                        view.animate().scaleX(1f).scaleY(1f).alpha(0.92f).setDuration(animationDuration(180)).setInterpolator(OvershootInterpolator(0.5f)).start()
                        if(!dragging) {
                            if(System.currentTimeMillis()-started>650) { LocalBrain.stop();sendBroadcast(Intent("id.notsyuu.assistant.STOP").setPackage(packageName));Data(this).log("Stop dari bubble Andrea AI") }
                            else { view.performClick();startActivity(Intent(this,PopupActivity::class.java).putExtra("voice",true).putExtra("screen",ScreenService.current?.snapshot() ?: "").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)) }
                        }
                        snap(view,p,size)
                    }
                }
                MotionEvent.ACTION_CANCEL -> { hideTarget();view.animate().scaleX(1f).scaleY(1f).setDuration(animationDuration(140)).start();snap(view,p,size) }
            };true
        }
        bubble=view;params=p;wm.addView(view,p)
        view.scaleX=0.5f;view.scaleY=0.5f;view.alpha=0f
        view.animate().scaleX(1f).scaleY(1f).alpha(0.92f).setDuration(animationDuration(230)).setInterpolator(OvershootInterpolator(0.7f)).start()
        prefs.edit().putBoolean("bubbleHidden",false).apply();updateNotification()
    }
    private fun showTarget() {
        val existing=target
        if(existing!=null) { existing.animate().cancel();existing.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(animationDuration(130)).start();return }
        val size=dp(72f);val bottom=wm.currentWindowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars()).bottom
        val p=layout(size).apply { x=(screen().width()-size)/2;y=screen().height()-bottom-size-dp(22f) }
        val view=ClayBubble(true).apply { contentDescription="Sembunyikan bubble";alpha=0f;scaleX=0.7f;scaleY=0.7f }
        target=view;targetParams=p;wm.addView(view,p)
        view.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(animationDuration(160)).setInterpolator(DecelerateInterpolator()).start()
    }
    private fun inTarget(p:WindowManager.LayoutParams,size:Int):Boolean {
        val t=targetParams ?: return false
        return BubbleGeometry.inDismissZone(p.x+size/2f,p.y+size/2f,t.x+t.width/2f,t.y+t.height/2f,dp(55f).toFloat())
    }
    private fun hideTarget() {
        val view=target ?: return
        view.animate().cancel();view.animate().alpha(0f).scaleX(0.7f).scaleY(0.7f).setDuration(animationDuration(140)).withEndAction { if(target===view) { runCatching { wm.removeView(view) };target=null;targetParams=null } }.start()
    }
    private fun snap(view:ClayBubble,p:WindowManager.LayoutParams,size:Int) {
        motion?.cancel();val from=p.x;val to=BubbleGeometry.snapX(from,size,screen().width(),dp(4f))
        motion=ValueAnimator.ofFloat(0f,1f).apply { duration=animationDuration(220);interpolator=DecelerateInterpolator(1.7f)
            addUpdateListener { a -> if(bubble===view) { p.x=(from+(to-from)*(a.animatedValue as Float)).toInt();runCatching { wm.updateViewLayout(view,p) } } }
            addListener(object:android.animation.AnimatorListenerAdapter(){ override fun onAnimationEnd(a:android.animation.Animator) { if(bubble===view)Data(this@BubbleService).prefs.edit().putInt("bubbleX",p.x).putInt("bubbleY",p.y).apply() } });start()
        }
    }
    private fun hideBubble(animated:Boolean) {
        motion?.cancel();val view=bubble
        if(view==null) { hideTarget();updateNotification();return }
        bubble=null;Data(this).prefs.edit().putBoolean("bubbleHidden",true).apply()
        view.animate().cancel()
        if(animated) { retiring.add(view);view.animate().alpha(0f).scaleX(0.15f).scaleY(0.15f).setDuration(animationDuration(160)).withEndAction { runCatching { wm.removeView(view) };retiring.remove(view) }.start() } else runCatching { wm.removeView(view) }
        hideTarget();updateNotification()
    }
    override fun onStartCommand(intent:Intent?,flags:Int,startId:Int):Int {
        when(intent?.action) { "stop" -> { LocalBrain.stop();sendBroadcast(Intent("id.notsyuu.assistant.STOP").setPackage(packageName));stopSelf() };"hide" -> hideBubble(true);else -> showBubble() }
        return START_NOT_STICKY
    }
    override fun onDestroy() { motion?.cancel();bubble?.let { it.animate().cancel();runCatching { wm.removeView(it) } };target?.let { it.animate().cancel();runCatching { wm.removeView(it) } };retiring.toList().forEach { it.animate().cancel();runCatching { wm.removeView(it) } };retiring.clear();bubble=null;target=null;super.onDestroy() }
    private inner class ClayBubble(val dismiss:Boolean):View(this@BubbleService) {
        private val paint=Paint(Paint.ANTI_ALIAS_FLAG)
        var highlight=false
            set(value) { if(field!=value) { field=value;invalidate() } }
        init { setLayerType(LAYER_TYPE_SOFTWARE,null);isClickable=true }
        override fun performClick():Boolean { super.performClick();return true }
        override fun onDraw(c:Canvas) {
            val cx=width/2f;val cy=height/2f;val r=if(dismiss)width*0.34f else width*0.38f
            paint.shader=null;paint.color=if(highlight)Color.rgb(250,205,210) else if(dismiss)Color.rgb(246,229,231) else Color.rgb(230,226,255)
            paint.setShadowLayer(dp(5f).toFloat(),dp(2f).toFloat(),dp(4f).toFloat(),0x3370698C);c.drawCircle(cx,cy,r,paint)
            paint.clearShadowLayer();paint.shader=LinearGradient(cx-r,cy-r,cx+r,cy+r,intArrayOf(Color.WHITE,if(dismiss)Color.rgb(244,217,223) else Color.rgb(208,200,244)),null,Shader.TileMode.CLAMP);c.drawCircle(cx,cy,r-dp(2f),paint)
            paint.shader=null;paint.style=Paint.Style.STROKE;paint.strokeWidth=dp(2f).toFloat();paint.color=0xCCFFFFFF.toInt();c.drawArc(cx-r+dp(4f),cy-r+dp(4f),cx+r-dp(4f),cy+r-dp(4f),200f,110f,false,paint)
            paint.style=Paint.Style.FILL;paint.color=if(dismiss)Color.rgb(170,74,92) else Color.rgb(94,76,154);paint.textAlign=Paint.Align.CENTER;paint.typeface=Typeface.create("sans-serif-medium",Typeface.NORMAL);paint.textSize=dp(if(dismiss)26f else 22f).toFloat()
            c.drawText(if(dismiss)"×" else "A",cx,cy-(paint.ascent()+paint.descent())/2,paint)
        }
    }
}
