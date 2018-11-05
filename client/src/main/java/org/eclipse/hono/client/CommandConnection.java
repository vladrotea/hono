/*******************************************************************************
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package org.eclipse.hono.client;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.eclipse.hono.client.impl.CommandConnectionImpl;
import org.eclipse.hono.config.ClientConfigProperties;

/**
 * A bidirectional connection between a <em>Protocol Adapter</em> and the
 * <em>AMQP 1.0 Messaging Network</em> to receive commands and send
 * responses.
 */
public interface CommandConnection extends HonoClient {

    /**
     * Gets a command consumer for a device.
     * <p>
     * Implementations may choose to cache consumers for devices and
     * return a cached instance instead of creating a new consumer on
     * each invocation.
     * 
     * @param tenantId The tenant to consume commands from.
     * @param deviceId The device for which the consumer will be created.
     * @param commandHandler The handler to invoke with every command received.
     * @param closeHandler The handler invoked when the peer detaches the link.
     * @return A future that will complete with the consumer once the link has been established. The future will fail if
     *         the link cannot be established, e.g. because this client is not connected.
     * @throws NullPointerException if tenantId, deviceId or messageConsumer is {@code null}.
     */
    Future<MessageConsumer> getOrCreateCommandConsumer(
            String tenantId,
            String deviceId,
            Handler<CommandContext> commandHandler,
            Handler<Void> closeHandler);

    /**
     * Closes the command consumer for a given device.
     *
     * @param tenantId The tenant to consume commands from.
     * @param deviceId The device for which the consumer will be created.
     * @return A future indicating the outcome of the operation.
     * @throws NullPointerException if tenantId or deviceId are {@code null}.
     */
    Future<Void> closeCommandConsumer(String tenantId, String deviceId);

    /**
     * Gets a sender for sending command responses to a business application.
     * 
     * @param tenantId The ID of the tenant to send the command responses for.
     * @param replyId The ID used to build the reply address as {@code control/tenantId/replyId}.
     * @return A future that will complete with the sender once the link has been established. The future will fail if
     *         the link cannot be established, e.g. because this client is not connected.
     * @throws NullPointerException if any of the parameters are {@code null}.
     */
    Future<CommandResponseSender> getCommandResponseSender(String tenantId, String replyId);

    /**
     * Creates a new client for a set of configuration properties.
     *
     * @param vertx The Vert.x instance to execute the client on, if {@code null} a new Vert.x instance is used.
     * @param clientConfigProperties The configuration properties to use.
     *
     * @return CommandConnection The client that was created.
     * @throws NullPointerException if clientConfigProperties is {@code null}
     */
    static CommandConnection newConnection(final Vertx vertx, final ClientConfigProperties clientConfigProperties) {
        return new CommandConnectionImpl(vertx, clientConfigProperties);
    }
}
