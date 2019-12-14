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

package swim.uri;

import swim.codec.Output;

final class UriHostName extends UriHost {

  final String address;

  UriHostName(String address) {
    this.address = address;
  }

  @Override
  public String address() {
    return this.address;
  }

  @Override
  public String name() {
    return this.address;
  }

  @Override
  public void debug(Output<?> output) {
    output = output.write("UriHost").write('.').write("name").write('(').debug(this.address).write(')');
  }

  @Override
  public void display(Output<?> output) {
    Uri.writeHost(this.address, output);
  }

  @Override
  public String toString() {
    return this.address;
  }

}
