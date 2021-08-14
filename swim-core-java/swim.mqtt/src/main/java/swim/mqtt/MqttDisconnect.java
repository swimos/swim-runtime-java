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

public final class MqttDisconnect extends MqttPacket<Object> implements Debug {

  final int packetFlags;

  MqttDisconnect(int packetFlags) {
    this.packetFlags = packetFlags;
  }

  @Override
  public int packetType() {
    return 14;
  }

  @Override
  public int packetFlags() {
    return this.packetFlags;
  }

  public MqttDisconnect packetFlags(int packetFlags) {
    return new MqttDisconnect(packetFlags);
  }

  @Override
  int bodySize(MqttEncoder mqtt) {
    return 0;
  }

  @Override
  public Encoder<?, MqttDisconnect> mqttEncoder(MqttEncoder mqtt) {
    return mqtt.disconnectEncoder(this);
  }

  @Override
  public Encoder<?, MqttDisconnect> encodeMqtt(OutputBuffer<?> output, MqttEncoder mqtt) {
    return mqtt.encodeDisconnect(this, output);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    } else if (other instanceof MqttDisconnect) {
      final MqttDisconnect that = (MqttDisconnect) other;
      return this.packetFlags == that.packetFlags;
    }
    return false;
  }

  private static int hashSeed;

  @Override
  public int hashCode() {
    if (MqttDisconnect.hashSeed == 0) {
      MqttDisconnect.hashSeed = Murmur3.seed(MqttDisconnect.class);
    }
    return Murmur3.mash(Murmur3.mix(MqttDisconnect.hashSeed, this.packetFlags));
  }

  @Override
  public <T> Output<T> debug(Output<T> output) {
    output = output.write("MqttDisconnect").write('.').write("packet").write('(').write(')');
    if (this.packetFlags != 0) {
      output = output.write('.').write("packetFlags").write('(').debug(this.packetFlags).write(')');
    }
    return output;
  }

  @Override
  public String toString() {
    return Format.debug(this);
  }

  private static MqttDisconnect packet;

  public static MqttDisconnect packet() {
    if (MqttDisconnect.packet == null) {
      MqttDisconnect.packet = new MqttDisconnect(0);
    }
    return MqttDisconnect.packet;
  }

  public static MqttDisconnect create(int packetFlags) {
    if (packetFlags == 0) {
      return MqttDisconnect.packet();
    } else {
      return new MqttDisconnect(packetFlags);
    }
  }

}
