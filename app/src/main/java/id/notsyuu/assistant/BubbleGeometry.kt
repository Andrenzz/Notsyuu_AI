package id.notsyuu.assistant

object BubbleGeometry {
    fun snapX(x:Int,size:Int,width:Int,inset:Int):Int {
        val max=(width-size-inset).coerceAtLeast(0)
        return if(x+size/2<width/2) inset.coerceAtMost(max) else max
    }
    fun inDismissZone(x:Float,y:Float,targetX:Float,targetY:Float,radius:Float):Boolean {
        val dx=x-targetX;val dy=y-targetY
        return dx*dx+dy*dy<=radius*radius
    }
}
