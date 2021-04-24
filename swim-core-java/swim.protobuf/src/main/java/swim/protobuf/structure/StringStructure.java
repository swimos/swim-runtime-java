// Copyright 2015-2020 SWIM.AI inc.
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

package swim.protobuf.structure;

import swim.codec.Input;
import swim.codec.Output;
import swim.codec.Parser;
import swim.codec.Unicode;
import swim.protobuf.schema.ProtobufStringType;
import swim.structure.Text;
import swim.structure.Value;

final class StringStructure extends ProtobufStringType<Value> {

  @SuppressWarnings("unchecked")
  @Override
  public Parser<Value> parseString(Input input) {
    return Unicode.parseOutput((Output<Value>) (Output<?>) Text.output(), input);
  }

}
