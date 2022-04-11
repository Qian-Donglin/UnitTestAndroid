package com.rikoten.testplacetomonokai

import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import java.util.regex.Pattern

object LinkUtils {
    val URL_PATTERN =
        Pattern.compile("((https?|ftp)(:\\/\\/[-_.!~*\\'()a-zA-Z0-9;\\/?:\\@&=+\\$,%#]+))")

    @JvmOverloads
    fun autoLink(
        view: TextView, listener: OnClickListener?,
        patternStr: String? = null
    ) {
        val text = view.text.toString()
        if (TextUtils.isEmpty(text)) {
            return
        }
        val spannable: Spannable = SpannableString(text)
        val pattern: Pattern
        pattern = if (TextUtils.isEmpty(patternStr)) {
            URL_PATTERN
        } else {
            Pattern.compile(patternStr)
        }
        val matcher = pattern.matcher(text)
        while (matcher.find()) {
            val urlSpan = SensibleUrlSpan(matcher.group(1), pattern)
            spannable.setSpan(
                urlSpan, matcher.start(1), matcher.end(1),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        view.setText(spannable, TextView.BufferType.SPANNABLE)
        val method = SensibleLinkMovementMethod()
        view.movementMethod = method
        if (listener != null) {
            view.setOnClickListener {
                if (method.isLinkClicked) {
                    listener.onLinkClicked(method.clickedLink)
                } else {
                    listener.onClicked()
                }
            }
        }
    }

    interface OnClickListener {
        fun onLinkClicked(link: String?)
        fun onClicked()
    }

    internal class SensibleUrlSpan(
        url: String?,
        /** Pattern to match.  */
        private val mPattern: Pattern
    ) :
        URLSpan(url) {
        fun onClickSpan(widget: View?): Boolean {
            val matched = mPattern.matcher(url).matches()
            if (matched) {
                super.onClick(widget!!)
            }
            return matched
        }
    }

    internal class SensibleLinkMovementMethod : LinkMovementMethod() {
        var isLinkClicked = false
            private set
        var clickedLink: String? = null
            private set

        override fun onTouchEvent(
            widget: TextView,
            buffer: Spannable,
            event: MotionEvent
        ): Boolean {
            val action = event.action
            if (action == MotionEvent.ACTION_UP) {
                isLinkClicked = false
                clickedLink = null
                var x = event.x.toInt()
                var y = event.y.toInt()
                x -= widget.totalPaddingLeft
                y -= widget.totalPaddingTop
                x += widget.scrollX
                y += widget.scrollY
                val layout = widget.layout
                val line = layout.getLineForVertical(y)
                val off = layout.getOffsetForHorizontal(line, x.toFloat())
                val link = buffer.getSpans(
                    off, off,
                    ClickableSpan::class.java
                )
                if (link.size != 0) {
                    val span = link[0] as SensibleUrlSpan
                    isLinkClicked = span.onClickSpan(widget)
                    clickedLink = span.url
                    return isLinkClicked
                }
            }
            super.onTouchEvent(widget, buffer, event)
            return false
        }
    }
}