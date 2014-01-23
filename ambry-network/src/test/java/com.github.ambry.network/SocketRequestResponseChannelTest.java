package com.github.ambry.network;


import com.github.ambry.utils.ByteBufferInputStream;
import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.io.InputStream;
import java.nio.channels.WritableByteChannel;
import java.util.Random;

public class SocketRequestResponseChannelTest {

  class ResponseListenerMock implements ResponseListener {
    public int call = 0;

    @Override
    public void onResponse(int processorId) {
      call++;
    }
  }

  class MockSend implements Send {
    public int sendcall = 1;

    @Override
    public void writeTo(WritableByteChannel channel) throws IOException {
      // no implementation
    }

    @Override
    public boolean isSendComplete() {
      return false;
    }

    @Override
    public long sizeInBytes() {
      return 10;
    }
  }

  @Test
  public void testSocketRequestResponseChannelTest() {
    try {
      SocketRequestResponseChannel channel = new SocketRequestResponseChannel(2, 10);
      Integer key = new Integer(5);
      ByteBuffer buffer = ByteBuffer.allocate(1000);
      new Random().nextBytes(buffer.array());
      channel.sendRequest(new SocketServerRequest(0, key, new ByteBufferInputStream(buffer)));
      SocketServerRequest request = (SocketServerRequest)channel.receiveRequest();
      Assert.assertEquals(request.getProcessor(), 0);
      Assert.assertEquals((Integer)request.getRequestKey(), key);
      InputStream stream = request.getInputStream();
      for (int i = 0; i < 1000; i++) {
        Assert.assertEquals((byte)stream.read(), buffer.array()[i]);
      }

      ResponseListenerMock mock = new ResponseListenerMock();
      channel.addResponseListener(mock);
      MockSend mocksend = new MockSend();
      channel.sendResponse(mocksend, request, null, null);
      Assert.assertEquals(mock.call, 1);
      SocketServerResponse response = (SocketServerResponse)channel.receiveResponse(0);
      Assert.assertEquals(response.getProcessor() , 0);
    }
    catch (Exception e) {
      Assert.assertEquals(true, false);
    }

  }
}