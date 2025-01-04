package com.example.blogmultiplatform.styles

import com.example.blogmultiplatform.models.Theme
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.hover
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgba

val PostPreviewStyle = CssStyle {
    base {
        Modifier
            .scale(100.percent)
            .transition(
                Transition.of(
                    property = TransitionProperty.All,
                    duration = 100.ms,
                    timingFunction = null,
                    delay = null
                )
            )
    }
    hover {
        Modifier
            .boxShadow(
                offsetX = 0.px,
                offsetY = 0.px,
                blurRadius = 8.px,
                spreadRadius = 5.px,
                color = rgba(0, 0, 0, 0.06),
            )
            .scale(102.percent)
    }
}

val MainPostPreviewStyle = CssStyle {
    base {
        Modifier
            .scale(100.percent)
            .transition(
                Transition.of(
                    property = TransitionProperty.All,
                    duration = 100.ms,
                    timingFunction = null,
                    delay = null
                )
            )
    }
    hover {
        Modifier
            .boxShadow(
                offsetX = 0.px,
                offsetY = 0.px,
                blurRadius = 8.px,
                spreadRadius = 5.px,
                color = rgba(0, 162, 255, 0.06),
            )
            .scale(102.percent)
    }
}
