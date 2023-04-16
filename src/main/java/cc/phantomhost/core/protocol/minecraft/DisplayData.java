package cc.phantomhost.core.protocol.minecraft;

public enum DisplayData {
    WARNING_LINE,

    PROTOCOL_VERSION,

    /**
     * Not as simple as just storing lines of a message, this has
     * to be a compiled message according to minecraft protocol.
     * <a href="https://wiki.vg/Server_List_Ping#Status_Response">...</a>
     *
     * In this case, each line has to accompany an uuid
     */
    HOVER_MESSAGE,
    MOTD,

    /**
     * Image in base64 format, note that this key is optional;
     * The string related to this has to include the relevant
     * protocol stuff for minecraft
     * <a href="https://wiki.vg/Server_List_Ping#Status_Response">...</a>
     */
    IMAGE_DATA,

    LOGIN_MESSAGE
}
