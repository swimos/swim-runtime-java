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

package swim.math;

import swim.codec.Debug;
import swim.codec.Format;
import swim.codec.Output;
import swim.structure.Kind;
import swim.structure.Value;
import swim.util.Murmur3;

public class PointZ2 extends Z2Shape implements Debug {

  private static int hashSeed;
  private static PointZ2 origin;
  private static Z2Form<PointZ2> form;
  public final long x;
  public final long y;

  public PointZ2(long x, long y) {
    this.x = x;
    this.y = y;
  }

  public static PointZ2 origin() {
    if (origin == null) {
      origin = new PointZ2(0L, 0L);
    }
    return origin;
  }

  public static PointZ2 of(long x, long y) {
    return new PointZ2(x, y);
  }

  @Kind
  public static Z2Form<PointZ2> form() {
    if (form == null) {
      form = new PointZ2Form();
    }
    return form;
  }

  public final PointZ2 plus(VectorZ2 vector) {
    return new PointZ2(this.x + vector.x, this.y + vector.y);
  }

  public final PointZ2 minux(VectorZ2 vector) {
    return new PointZ2(this.x - vector.x, this.y - vector.y);
  }

  public final VectorZ2 minus(PointZ2 that) {
    return new VectorZ2(this.x - that.x, this.y - that.y);
  }

  @Override
  public final long xMin() {
    return this.x;
  }

  @Override
  public final long yMin() {
    return this.y;
  }

  @Override
  public final long xMax() {
    return this.x;
  }

  @Override
  public final long yMax() {
    return this.y;
  }

  @Override
  public boolean contains(Z2Shape shape) {
    return this.x <= shape.xMin() && shape.xMax() <= this.x
        && this.y <= shape.yMin() && shape.yMax() <= this.y;
  }

  @Override
  public boolean intersects(Z2Shape shape) {
    return shape.intersects(this);
  }

  @Override
  public PointR2 transform(Z2ToR2Function f) {
    return new PointR2(f.transformX(this.x, this.y), f.transformY(this.x, this.y));
  }

  @Override
  public Value toValue() {
    return form().mold(this).toValue();
  }

  protected boolean canEqual(PointZ2 that) {
    return true;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    } else if (other instanceof PointZ2) {
      final PointZ2 that = (PointZ2) other;
      return that.canEqual(this) && this.x == that.x && this.y == that.y;
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (hashSeed == 0) {
      hashSeed = Murmur3.seed(PointZ2.class);
    }
    return Murmur3.mash(Murmur3.mix(Murmur3.mix(hashSeed,
        Murmur3.hash(this.x)), Murmur3.hash(this.y)));
  }

  @Override
  public void debug(Output<?> output) {
    output.write("PointZ2").write('.').write("of").write('(')
        .debug(this.x).write(", ").debug(this.y).write(')');
  }

  @Override
  public String toString() {
    return Format.debug(this);
  }

}
