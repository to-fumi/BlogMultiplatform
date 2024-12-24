package com.example.blogmultiplatform.styles

import com.example.blogmultiplatform.models.Theme
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.focus
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.px

val NewsletterInputStyle = CssStyle {
    base {
        Modifier
            .outline(
                width = 1.px,
                style = LineStyle.Solid,
                color = Colors.Transparent,
            )
            .border(
                width = 1.px,
                style = LineStyle.Solid,
                color = Colors.Transparent,
            )
            .transition(
                Transition.of(
                    property = TransitionProperty.All,
                    duration = 300.ms,
                    timingFunction = null,
                    delay = null
                )
            )
    }
    focus {
        Modifier
            .outline(
                width = 1.px,
                style = LineStyle.Solid,
                color = Theme.Primary.rgb,
            )
            .border(
                width = 1.px,
                style = LineStyle.Solid,
                color = Theme.Primary.rgb,
            )
    }
}
