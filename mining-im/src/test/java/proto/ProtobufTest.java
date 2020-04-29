package proto;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mine.proto.IMessage;

/**
 * Created by admin on 2020/4/29.
 */
public class ProtobufTest {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        IMessage.Message iMessage = IMessage.Message.newBuilder()
                .setType(1).setSender("1").build();
        byte[] byteArray = iMessage.toByteArray();

        IMessage.Message orginal = IMessage.Message.parseFrom(byteArray);
        System.out.println(orginal.getSender());
    }
}
