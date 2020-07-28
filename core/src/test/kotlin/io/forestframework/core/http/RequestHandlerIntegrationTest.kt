package io.forestframework.core.http

import com.google.inject.Injector
import io.forestframework.core.ForestApplication
import io.forestframework.core.http.routing.RequestHandler
import io.forestframework.testfixtures.AbstractForestIntegrationTest
import io.forestframework.testsupport.ForestExtension
import io.forestframework.testsupport.ForestTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import javax.inject.Inject

@ForestApplication
class TestApp

@ExtendWith(ForestExtension::class)
@ForestTest(appClass = TestApp::class)
class RequestHandlerIntegrationTest : AbstractForestIntegrationTest() {
    @Inject
    lateinit var injector: Injector

    @Test
    fun `RequestHandler is singleton`() {
        Assertions.assertSame(injector.getInstance(RequestHandler::class.java), injector.getInstance(RequestHandler::class.java))
    }
}