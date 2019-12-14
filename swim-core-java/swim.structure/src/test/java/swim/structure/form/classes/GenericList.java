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

package swim.structure.form.classes;

import java.util.List;
import swim.util.Murmur3;

@SuppressWarnings("checkstyle:VisibilityModifier")
public class GenericList<T> {

  public List<T> list;

  public GenericList(List<T> list) {
    this.list = list;
  }

  public GenericList() {
    // Form.cast constructor
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof GenericList<?>) {
      final GenericList<?> that = (GenericList<?>) other;
      return this.list == null ? that.list == null : this.list.equals(that.list);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Murmur3.hash(this.list);
  }

  @Override
  public String toString() {
    return "GenericList(" + this.list + ")";
  }

}
