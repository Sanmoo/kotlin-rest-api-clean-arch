package com.mercadolivro.configuration

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext

class FunctionalBeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(applicationContext: GenericApplicationContext) {
        return beans.initialize(applicationContext)
    }
}