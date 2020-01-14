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

package swim.dynamic;

import java.util.Collection;

/**
 * A collection of dynamically typed module descriptors representing
 * a host library.
 */
public interface HostLibrary {

  String libraryName();

  HostPackage getHostPackage(String packageName);

  Collection<HostPackage> hostPackages();

  HostType<?> getHostType(String typeName);

  HostType<?> getHostType(Class<?> typeClass);

  Collection<HostType<?>> hostTypes();

}
