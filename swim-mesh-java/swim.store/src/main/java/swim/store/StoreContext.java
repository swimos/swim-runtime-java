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

package swim.store;

import swim.math.Z2Form;
import swim.structure.Value;

public interface StoreContext {

  StoreBinding openStore(Value name);

  StoreBinding injectStore(StoreBinding storeBinding);

  ListDataBinding openListData(Value name);

  ListDataBinding injectListData(ListDataBinding dataBinding);

  MapDataBinding openMapData(Value name);

  MapDataBinding injectMapData(MapDataBinding dataBinding);

  <S> SpatialDataBinding<S> openSpatialData(Value name, Z2Form<S> shapeForm);

  <S> SpatialDataBinding<S> injectSpatialData(SpatialDataBinding<S> dataBinding);

  ValueDataBinding openValueData(Value name);

  ValueDataBinding injectValueData(ValueDataBinding dataBinding);

  void close();

}
