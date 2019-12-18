/*
 * Copyright 2019-Present paseto.dev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.paseto.jpaseto.impl;

import com.google.auto.service.AutoService;
import dev.paseto.jpaseto.PasetoV1PublicBuilder;
import dev.paseto.jpaseto.impl.crypto.V1PublicCryptoProvider;

import java.security.PrivateKey;

@AutoService(PasetoV1PublicBuilder.class)
public class DefaultPasetoV1PublicBuilder extends AbstractPasetoBuilder<PasetoV1PublicBuilder> implements PasetoV1PublicBuilder {

    private static final String HEADER = "v1.public.";

    private PrivateKey privateKey;

    private final V1PublicCryptoProvider cryptoProvider;

    public DefaultPasetoV1PublicBuilder() {
        this(CryptoProviders.v1PublicCryptoProvider());
    }

    private DefaultPasetoV1PublicBuilder(V1PublicCryptoProvider cryptoProvider) {
        this.cryptoProvider = cryptoProvider;
    }

    @Override
    public PasetoV1PublicBuilder setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    @Override
    public String compact() {

        byte[] payload = payloadAsBytes();
        byte[] footer = footerAsBytes();

        byte[] signature = cryptoProvider.sign(payload, footer, privateKey);

        return HEADER + noPadBase64(payload, signature) + footerToString(footer);
    }
}
