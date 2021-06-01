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

package swim.meta;

import swim.runtime.LaneBinding;
import swim.runtime.NodeContext;
import swim.runtime.agent.AgentNode;

public final class MetaLaneAgent extends AgentNode {

  public final LaneBinding lane;

  public MetaLaneAgent(LaneBinding lane) {
    this.lane = lane;
  }

  @Override
  public void setNodeContext(NodeContext nodeContext) {
    super.setNodeContext(nodeContext);
    this.lane.openMetaLane(this.lane, this);
  }

}
