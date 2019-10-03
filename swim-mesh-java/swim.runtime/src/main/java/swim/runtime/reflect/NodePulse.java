// Copyright 2015-2019 SWIM.AI inc.
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

package swim.runtime.reflect;

import swim.structure.Form;
import swim.structure.Item;
import swim.structure.Kind;
import swim.structure.Record;
import swim.structure.Value;

public class NodePulse extends Pulse {
  protected final long nodeCount;
  protected final AgentPulse agentPulse;
  protected final WarpDownlinkPulse downlinkPulse;
  protected final WarpUplinkPulse uplinkPulse;

  public NodePulse(long nodeCount, AgentPulse agentPulse,
                   WarpDownlinkPulse downlinkPulse, WarpUplinkPulse uplinkPulse) {
    this.nodeCount = nodeCount;
    this.agentPulse = agentPulse;
    this.downlinkPulse = downlinkPulse;
    this.uplinkPulse = uplinkPulse;
  }

  @Override
  public boolean isDefined() {
    return this.nodeCount != 0L || this.agentPulse.isDefined()
        || this.downlinkPulse.isDefined() || this.uplinkPulse.isDefined();
  }

  public final long nodeCount() {
    return this.nodeCount;
  }

  public final AgentPulse agentPulse() {
    return this.agentPulse;
  }

  public final WarpDownlinkPulse downlinkPulse() {
    return this.downlinkPulse;
  }

  public final WarpUplinkPulse uplinkPulse() {
    return this.uplinkPulse;
  }

  @Override
  public Value toValue() {
    return form().mold(this).toValue();
  }

  private static Form<NodePulse> form;

  @Kind
  public static Form<NodePulse> form() {
    if (form == null) {
      form = new NodePulseForm();
    }
    return form;
  }
}

final class NodePulseForm extends Form<NodePulse> {
  @Override
  public Class<?> type() {
    return NodePulse.class;
  }

  @Override
  public Item mold(NodePulse pulse) {
    if (pulse != null) {
      final Record record = Record.create(4);
      if (pulse.nodeCount > 0L) {
        record.slot("nodeCount", pulse.nodeCount);
      }
      if (pulse.agentPulse.isDefined()) {
        record.slot("agents", pulse.agentPulse.toValue());
      }
      if (pulse.downlinkPulse.isDefined()) {
        record.slot("downlinks", pulse.downlinkPulse.toValue());
      }
      if (pulse.uplinkPulse.isDefined()) {
        record.slot("uplinks", pulse.uplinkPulse.toValue());
      }
      return record;
    } else {
      return Item.extant();
    }
  }

  @Override
  public NodePulse cast(Item item) {
    final Value value = item.toValue();
    final long nodeCount = value.get("nodeCount").longValue(0L);
    final AgentPulse agentPulse = value.get("agents").coerce(AgentPulse.form());
    final WarpDownlinkPulse downlinkPulse = value.get("downlinks").coerce(WarpDownlinkPulse.form());
    final WarpUplinkPulse uplinkPulse = value.get("uplinks").coerce(WarpUplinkPulse.form());
    return new NodePulse(nodeCount, agentPulse, downlinkPulse, uplinkPulse);
  }
}
