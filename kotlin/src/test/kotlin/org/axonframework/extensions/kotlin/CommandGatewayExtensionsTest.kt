/*
 * Copyright (c) 2010-2020. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.axonframework.extensions.kotlin

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.axonframework.commandhandling.CommandCallback
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.MetaData
import java.util.concurrent.TimeUnit
import kotlin.test.Test

/**
 * Tests Command Gateway extensions.
 *
 * @author Stefan Andjelkovic
 */
class CommandGatewayExtensionsTest {
    private val subjectGateway = mockk<CommandGateway>()

    private val exampleCommand = ExampleCommand("1")
    private val timeoutInterval: Long = 30
    private val defaultTimeUnit = TimeUnit.MILLISECONDS

    @Test
    fun `Send extension should invoke correct method on the gateway`() {
        every { subjectGateway.send(exampleCommand, any<CommandCallback<ExampleCommand, Any>>()) } just Runs

        subjectGateway.send<ExampleCommand, Any>(command = exampleCommand, onError = { _, _, _ -> }, onSuccess = { _, _, _ -> })

        verify { subjectGateway.send(exampleCommand, any<CommandCallback<ExampleCommand, Any>>()) }
    }

    @Test
    fun `Send extension should invoke correct method on the gateway without explicit generic parameters`() {
        every { subjectGateway.send(exampleCommand, any<CommandCallback<ExampleCommand, Any>>()) } just Runs

        subjectGateway.send(
            command = exampleCommand,
            onError = { _: Any, _: Throwable, _: MetaData -> },
            onSuccess = { _: CommandMessage<out ExampleCommand>, _: Any, _: MetaData -> }
        )

        verify { subjectGateway.send(exampleCommand, any<CommandCallback<ExampleCommand, Any>>()) }
    }
}
