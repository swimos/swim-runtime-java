// Copyright 2015-2021 Swim inc.
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

public final class MqttPubComp extends MqttPacket<Object> implements Debug {

  private static int hashSeed;
  final int packetFlags;
  final int packetId;

  MqttPubComp(int packetFlags, int packetId) {
    this.packetFlags = packetFlags;
    this.packetId = packetId;
  }

  public static MqttPubComp from(int packetFlags, int packetId) {
    return new MqttPubComp(packetFlags, packetId);
  }

  public static MqttPubComp from(int packetId) {
    return new MqttPubComp(0, packetId);
  }

  @Override
  public int packetType() {
    return 7;
  }

  @Override
  public int packetFlags() {
    return this.packetFlags;
  }

  public MqttPubComp packetFlags(int packetFlags) {
    return new MqttPubComp(packetFlags, this.packetId);
  }

  public int packetId() {
    return this.packetId;
  }

  public MqttPubComp packetId(int packetId) {
    return new MqttPubComp(this.packetFlags, packetId);
  }

  @Override
  int bodySize(MqttEncoder mqtt) {
    return 2;
  }

  @Override
  public Encoder<?, MqttPubComp> mqttEncoder(MqttEncoder mqtt) {
    return mqtt.pubCompEncoder(this);
  }

  @Override
  public Encoder<?, MqttPubComp> encodeMqtt(OutputBuffer<?> output, MqttEncoder mqtt) {
    return mqtt.encodePubComp(this, output);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    } else if (other instanceof MqttPubComp) {
      final MqttPubComp that = (MqttPubComp) other;
      return this.packetFlags == that.packetFlags && this.packetId == that.packetId;
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (hashSeed == 0) {
      hashSeed = Murmur3.seed(MqttPubComp.class);
    }
    return Murmur3.mash(Murmur3.mix(Murmur3.mix(hashSeed, this.packetFlags), this.packetId));
  }

  @Override
  public void debug(Output<?> output) {
    output = output.write("MqttPubComp").write('.').write("from").write('(')
        .debug(this.packetId).write(')');
    if (this.packetFlags != 0) {
      output = output.write('.').write("packetFlags").write('(').debug(this.packetFlags).write(')');
    }
  }

  @Override
  public String toString() {
    return Format.debug(this);
  }

}
