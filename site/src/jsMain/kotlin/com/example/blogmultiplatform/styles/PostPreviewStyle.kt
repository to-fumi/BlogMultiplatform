package com.example.blogmultiplatform.styles

import com.example.blogmultiplatform.models.Theme
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.hover
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

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
            .scale(102.percent)
    }
}