package com.hoteam.sms.driver.core.coder.cmpp;


import com.hoteam.sms.driver.domain.CmppMessageHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码
 *
 * @author weixiang
 * @date 2018/8/2 15:37
 */
public class CMPPEncoder extends MessageToByteEncoder<Object> {

    public CMPPEncoder() {
        super(false);
    }

    @Override
    protected void encode(ChannelHandlerContext context, Object message, ByteBuf out) throws Exception {
        if (message instanceof byte[]) {
            out.writeBytes((byte[]) message);
        } else if (message instanceof Integer) {
            out.writeInt((Integer) message);
        } else if (message instanceof Byte) {
            out.writeByte((Byte) message);
        } else if (message instanceof Long) {
            out.writeLong((Long) message);
        } else if (message instanceof String) {
            out.writeBytes(((String) message).getBytes("UTF-16BE"));
        } else if (message instanceof Character) {
            out.writeChar((Character) message);
        } else if (message instanceof CmppMessageHeader) {
            CmppMessageHeader header = (CmppMessageHeader) message;
            out.writeBytes(header.toByteArray());
        }
    }
}
