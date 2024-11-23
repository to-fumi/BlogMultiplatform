package com.example.blogmultiplatform.styles

import com.example.blogmultiplatform.models.Theme
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.style.CssStyle
import org.jetbrains.compose.web.css.ms

val NavigationItemStyle = CssStyle {
    cssRule(" > #svgParent > #vectorIcon") {
        Modifier
            .transition(
                Transition.of(
                    property = TransitionProperty.All,
                    duration = 300.ms,
                    timingFunction = null,
                    delay = null
                )
            )
            .styleModifier {
                property("stroke", Theme.White.hex)
            }
    }
    cssRule(":hover > #svgParent > #vectorIcon") {
        Modifier
            .transition(
                Transition.of(
                    property = TransitionProperty.All,
                    duration = 300.ms,
                    timingFunction = null,
                    delay = null
                )
            )
            .styleModifier {
            property("stroke", Theme.Primary.hex)
        }
    }
    cssRule(" > #navigationText") {
        Modifier
            .transition(
                Transition.of(
                    property = TransitionProperty.All,
                    duration = 300.ms,
                    timingFunction = null,
                    delay = null
                )
            )
            .color(Theme.White.rgb)
    }
    cssRule(":hover > #navigationText") {
        Modifier.color(Theme.Primary.rgb)
    }
}
