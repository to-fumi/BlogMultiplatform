package com.example.blogmultiplatform.styles

import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.style.layer.SilkLayer
import com.varabyte.kobweb.silk.style.layer.add

@InitSilk
fun initSilk(ctx: InitSilkContext) {
    ctx.stylesheet.cssLayers.add("bootstrap", after = SilkLayer.BASE)
}
