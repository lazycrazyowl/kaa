/*
 * Copyright 2014 CyberVision, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kaaproject.kaa.server.operations.service.akka.messages.core.user.verification;

import akka.actor.ActorRef;

public class UserVerificationRequestMessage {

    private final ActorRef originator;
    private final int verifierId;
    private final String userId;
    private final String accessToken;

    public UserVerificationRequestMessage(ActorRef originator, int verifierId, String userId, String accessToken) {
        super();
        this.originator = originator;
        this.verifierId = verifierId;
        this.userId = userId;
        this.accessToken = accessToken;
    }

    public int getVerifierId() {
        return verifierId;
    }

    public String getUserId() {
        return userId;
    }

    public String getAccessToken() {
        return accessToken;
    }
    
    public ActorRef getOriginator() {
        return originator;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UserVerificationRequestMessage [verifierId=");
        builder.append(verifierId);
        builder.append(", userId=");
        builder.append(userId);
        builder.append(", accessToken=");
        builder.append(accessToken);
        builder.append("]");
        return builder.toString();
    }
}
