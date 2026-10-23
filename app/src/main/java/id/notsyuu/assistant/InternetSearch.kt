package id.notsyuu.assistant

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.net.URLEncoder

object InternetSearch {
    data class Result(val title:String,val url:String,val description:String)
    fun search(query:String):List<Result> {
        val url="https://www.bing.com/search?format=rss&q="+URLEncoder.encode(query.take(350),"UTF-8")
        val connection=URL(url).openConnection() as HttpURLConnection
        connection.connectTimeout=10000; connection.readTimeout=10000; connection.instanceFollowRedirects=false
        connection.setRequestProperty("User-Agent","Andrea AI/0.1 (personal Android assistant)")
        try {
            require(connection.responseCode==200) { "Penyedia pencarian tidak merespons. Coba lagi nanti." }
            val bytes=connection.inputStream.use { it.readBytesLimited(500000) }
            val parser=Xml.newPullParser(); parser.setInput(bytes.inputStream(),"UTF-8")
            val results=mutableListOf<Result>(); var inside=false; var tag=""; var title=""; var link=""; var description=""; var event=parser.eventType
            while(event!=XmlPullParser.END_DOCUMENT && results.size<5) {
                when(event) {
                    XmlPullParser.START_TAG -> { tag=parser.name; if(tag=="item") { inside=true;title="";link="";description="" } }
                    XmlPullParser.TEXT -> if(inside) when(tag) { "title" -> title+=parser.text; "link" -> link+=parser.text; "description" -> description+=parser.text }
                    XmlPullParser.END_TAG -> { if(parser.name=="item") { inside=false; val uri=runCatching { URI(link.trim()) }.getOrNull(); if(uri?.scheme=="https" && !uri.host.isNullOrBlank()) results.add(Result(clean(title).take(200),link.trim(),clean(description).take(1200))) }; tag="" }
                }; event=parser.next()
            }
            return results
        } finally { connection.disconnect() }
    }
    private fun clean(s:String)=s.replace(Regex("<[^>]*>")," ").replace("&quot;","\"").replace("&amp;","&").replace("&#39;","'").replace(Regex("\\s+")," ").trim()
    private fun java.io.InputStream.readBytesLimited(limit:Int):ByteArray { val out=java.io.ByteArrayOutputStream();val buffer=ByteArray(8192);while(out.size()<limit){val n=read(buffer,0,minOf(buffer.size,limit-out.size()));if(n<0)break;out.write(buffer,0,n)};return out.toByteArray() }
}
