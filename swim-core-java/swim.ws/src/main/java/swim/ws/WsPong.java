// Copyright 2015-2021 Swim Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package swim.ws;

import java.nio.ByteBuffer;
import swim.codec.Binary;
import swim.codec.Debug;
import swim.codec.Encoder;
import swim.codec.Format;
import swim.codec.Output;
import swim.codec.OutputBuffer;
import swim.structure.Data;
import swim.util.Murmur3;

public final class WsPong<P, T> extends WsControl<P, T> implements Debug {

  final P payload;
  final Encoder<?, ?> content;

  WsPong(P payload, Encoder<?, ?> content) {
    this.payload = payload;
    this.content = content;
  }

  @Override
  public WsOpcode opcode() {
    return WsOpcode.PONG;
  }

  @Override
  public P payload() {
    return this.payload;
  }

  @Override
  public Encoder<?, ?> contentEncoder(WsEncoder ws) {
    return this.content;
  }

  @Override
  public Encoder<?, ?> encodeContent(OutputBuffer<?> output, WsEncoder ws) {
    return this.content.pull(output);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    } else if (other instanceof WsPong<?, ?>) {
      final WsPong<?, ?> that = (WsPong<?, ?>) other;
      return (this.payload == null ? that.payload == null : this.payload.equals(that.payload));
    }
    return false;
  }

  private static int hashSeed;

  @Override
  public int hashCode() {
    if (WsPong.hashSeed == 0) {
      WsPong.hashSeed = Murmur3.seed(WsPong.class);
    }
    return Murmur3.mash(Murmur3.mix(WsPong.hashSeed, Murmur3.hash(this.payload)));
  }

  @Override
  public <T> Output<T> debug(Output<T> output) {
    output = output.write("WsPong").write('.').write("create").write('(')
                   .debug(this.payload).write(", ").debug(this.content).write(')');
    return output;
  }

  @Override
  public String toString() {
    return Format.debug(this);
  }

  public static <P, T> WsPong<P, T> empty() {
    return new WsPong<P, T>(null, Encoder.done());
  }

  public static <P, T> WsPong<P, T> create(P payload, Encoder<?, ?> content) {
    return new WsPong<P, T>(payload, content);
  }

  @SuppressWarnings("unchecked")
  public static <P, T> WsPong<P, T> create(P payload) {
    if (payload instanceof Data) {
      return (WsPong<P, T>) WsPong.create((Data) payload);
    } else {
      return new WsPong<P, T>(payload, Encoder.done());
    }
  }

  public static <T> WsPong<ByteBuffer, T> create(ByteBuffer payload) {
    return new WsPong<ByteBuffer, T>(payload.duplicate(), Binary.byteBufferWriter(payload));
  }

  public static <T> WsPong<Data, T> create(Data payload) {
    return new WsPong<Data, T>(payload, payload.writer());
  }

}
