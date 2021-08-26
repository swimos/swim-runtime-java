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

import org.testng.annotations.Test;
import swim.structure.Data;
import static swim.mqtt.MqttAssertions.assertEncodes;

public class MqttUnsubscribeSpec {

  @Test
  public void decodeUnsubscribePacketsWithPacketId() {
    assertDecodes(Data.fromBase16("A2027E96"), MqttUnsubscribe.create(0x7E96));
  }

  @Test
  public void encodeUnsubscribePacketsWithPacketId() {
    assertEncodes(MqttUnsubscribe.create(0x7E96), Data.fromBase16("A2027E96"));
  }

  @Test
  public void decodeUnsubscribePacketsWithSingleTopicName() {
    assertDecodes(Data.fromBase16("A2080000000474657374"),
                  MqttUnsubscribe.create(0).topicName("test"));
  }

  @Test
  public void encodeUnsubscribePacketsWithSingleTopicName() {
    assertEncodes(MqttUnsubscribe.create(0).topicName("test"),
                  Data.fromBase16("A2080000000474657374"));
  }

  @Test
  public void decodeUnsubscribePacketsWithMultipleTopicNames() {
    assertDecodes(Data.fromBase16("A21200000004746573740003666F6F0003626172"),
                  MqttUnsubscribe.create(0)
                                 .topicName("test")
                                 .topicName("foo")
                                 .topicName("bar"));
  }

  @Test
  public void encodeUnsubscribePacketsWithMultipleTopicNames() {
    assertEncodes(MqttUnsubscribe.create(0)
                                 .topicName("test")
                                 .topicName("foo")
                                 .topicName("bar"),
                  Data.fromBase16("A21200000004746573740003666F6F0003626172"));
  }

  public static void assertDecodes(Data data, MqttUnsubscribe packet) {
    MqttAssertions.assertDecodesPacket(data, packet);
  }

}
