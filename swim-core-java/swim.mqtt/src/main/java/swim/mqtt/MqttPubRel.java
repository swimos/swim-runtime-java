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

public final class MqttPubRel extends MqttPacket<Object> implements Debug {

  final int packetFlags;
  final int packetId;

  MqttPubRel(int packetFlags, int packetId) {
    this.packetFlags = packetFlags;
    this.packetId = packetId;
  }

  @Override
  public int packetType() {
    return 6;
  }

  @Override
  public int packetFlags() {
    return this.packetFlags;
  }

  public MqttPubRel packetFlags(int packetFlags) {
    return new MqttPubRel(packetFlags, this.packetId);
  }

  public int packetId() {
    return this.packetId;
  }

  public MqttPubRel packetId(int packetId) {
    return new MqttPubRel(this.packetFlags, packetId);
  }

  @Override
  int bodySize(MqttEncoder mqtt) {
    return 2;
  }

  @Override
  public Encoder<?, MqttPubRel> mqttEncoder(MqttEncoder mqtt) {
    return mqtt.pubRelEncoder(this);
  }

  @Override
  public Encoder<?, MqttPubRel> encodeMqtt(OutputBuffer<?> output, MqttEncoder mqtt) {
    return mqtt.encodePubRel(this, output);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    } else if (other instanceof MqttPubRel) {
      final MqttPubRel that = (MqttPubRel) other;
      return this.packetFlags == that.packetFlags && this.packetId == that.packetId;
    }
    return false;
  }

  private static int hashSeed;

  @Override
  public int hashCode() {
    if (MqttPubRel.hashSeed == 0) {
      MqttPubRel.hashSeed = Murmur3.seed(MqttPubRel.class);
    }
    return Murmur3.mash(Murmur3.mix(Murmur3.mix(MqttPubRel.hashSeed, this.packetFlags), this.packetId));
  }

  @Override
  public <T> Output<T> debug(Output<T> output) {
    output = output.write("MqttPubRel").write('.').write("create").write('(').debug(this.packetId).write(')');
    if (this.packetFlags != 2) {
      output = output.write('.').write("packetFlags").write('(').debug(this.packetFlags).write(')');
    }
    return output;
  }

  @Override
  public String toString() {
    return Format.debug(this);
  }

  public static MqttPubRel create(int packetFlags, int packetId) {
    return new MqttPubRel(packetFlags, packetId);
  }

  public static MqttPubRel create(int packetId) {
    return new MqttPubRel(2, packetId);
  }

}
