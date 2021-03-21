/*
 *  Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.uom.idp.utils;

/**
 * This class contains required constants.
 *
 * @since 1.0.0
 */
public final class Constants {
    public static final String PUBLIC_KEY = "certs/public_key.der";
    public static final String ALGORITHM_RSA = "RSA";
    public static final String ISSUER = "uom.lk";
    public static final String API_CODES_CLAIM = "apiCodes";
    public static final String LICENSE_KEY_PATH = "/license.key";

    /**
     * Configuration constants.
     */
    public static class Configurations {

        public static final String CONFIGURATION_YAML = "keyvalidator.yaml";
    }
}
