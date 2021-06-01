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

package swim.collections;

import swim.util.Murmur3;

public final class HashedInteger {

  private final int value;

  public HashedInteger(int value) {
    this.value = value;
  }

  public static HashedInteger valueOf(int value) {
    return new HashedInteger(value);
  }

  public int intValue() {
    return value;
  }

  public long longValue() {
    return (long) value;
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof HashedInteger && value == ((HashedInteger) other).value;
  }

  @Override
  public int hashCode() {
    return Murmur3.mash(value);
  }

  @Override
  public String toString() {
    return Integer.toString(value);
  }

}
