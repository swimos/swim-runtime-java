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

package swim.mqtt;

import swim.codec.Debug;
import swim.codec.Encoder;
import swim.codec.Format;
import swim.codec.Output;
import swim.codec.OutputBuffer;
import swim.util.Murmur3;

public final class MqttPingResp extends MqttPacket<Object> implements Debug {

  final int packetFlags;

  MqttPingResp(int packetFlags) {
    this.packetFlags = packetFlags;
  }

  @Override
  public int packetType() {
    return 13;
  }

  @Override
  public int packetFlags() {
    return this.packetFlags;
  }

  public MqttPingResp packetFlags(int packetFlags) {
    return new MqttPingResp(packetFlags);
  }

  @Override
  int bodySize(MqttEncoder mqtt) {
    return 0;
  }

  @Override
  public Encoder<?, MqttPingResp> mqttEncoder(MqttEncoder mqtt) {
    return mqtt.pingRespEncoder(this);
  }

  @Override
  public Encoder<?, MqttPingResp> encodeMqtt(OutputBuffer<?> output, MqttEncoder mqtt) {
    return mqtt.encodePingResp(this, output);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    } else if (other instanceof MqttPingResp) {
      final MqttPingResp that = (MqttPingResp) other;
      return this.packetFlags == that.packetFlags;
    }
    return false;
  }

  private static int hashSeed;

  @Override
  public int hashCode() {
    if (MqttPingResp.hashSeed == 0) {
      MqttPingResp.hashSeed = Murmur3.seed(MqttPingResp.class);
    }
    return Murmur3.mash(Murmur3.mix(MqttPingResp.hashSeed, this.packetFlags));
  }

  @Override
  public <T> Output<T> debug(Output<T> output) {
    output = output.write("MqttPingResp").write('.').write("packet").write('(').write(')');
    if (this.packetFlags != 0) {
      output = output.write('.').write("packetFlags").write('(').debug(this.packetFlags).write(')');
    }
    return output;
  }

  @Override
  public String toString() {
    return Format.debug(this);
  }

  private static MqttPingResp packet;

  public static MqttPingResp packet() {
    if (MqttPingResp.packet == null) {
      MqttPingResp.packet = new MqttPingResp(0);
    }
    return MqttPingResp.packet;
  }

  public static MqttPingResp create(int packetFlags) {
    if (packetFlags == 0) {
      return MqttPingResp.packet();
    } else {
      return new MqttPingResp(packetFlags);
    }
  }

}
