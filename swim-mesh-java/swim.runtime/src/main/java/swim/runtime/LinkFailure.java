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

package swim.runtime;

import swim.structure.Record;
import swim.structure.Value;

public abstract class LinkFailure {
  public abstract Value toValue();

  public static LinkFailure meshNotFound() {
    return new LinkFailureMeshNotFound();
  }

  public static LinkFailure partNotFound() {
    return new LinkFailurePartNotFound();
  }

  public static LinkFailure hostNotFound() {
    return new LinkFailureHostNotFound();
  }

  public static LinkFailure nodeNotFound() {
    return new LinkFailureNodeNotFound();
  }

  public static LinkFailure laneNotFound() {
    return new LinkFailureLaneNotFound();
  }

  public static LinkFailure unsupported() {
    return new LinkFailureUnsupported();
  }
}

final class LinkFailureMeshNotFound extends LinkFailure {
  @Override
  public Value toValue() {
    return Record.create(1).attr("meshNotFound");
  }
}

final class LinkFailurePartNotFound extends LinkFailure {
  @Override
  public Value toValue() {
    return Record.create(1).attr("partNotFound");
  }
}

final class LinkFailureHostNotFound extends LinkFailure {
  @Override
  public Value toValue() {
    return Record.create(1).attr("hostNotFound");
  }
}

final class LinkFailureNodeNotFound extends LinkFailure {
  @Override
  public Value toValue() {
    return Record.create(1).attr("nodeNotFound");
  }
}

final class LinkFailureLaneNotFound extends LinkFailure {
  @Override
  public Value toValue() {
    return Record.create(1).attr("laneNotFound");
  }
}

final class LinkFailureUnsupported extends LinkFailure {
  @Override
  public Value toValue() {
    return Record.create(1).attr("unsupported");
  }
}
