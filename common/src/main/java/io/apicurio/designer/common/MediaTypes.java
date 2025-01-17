package io.apicurio.designer.common;

import javax.ws.rs.core.MediaType;

/**
 * @author eric.wittmann@gmail.com
 */
public final class MediaTypes {

    public static final MediaType JSON = MediaType.APPLICATION_JSON_TYPE;
    public static final MediaType BINARY = new MediaType("application", "octet-stream");
}
