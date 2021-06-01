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

public class PointR2 extends R2Shape implements Debug {

  private static int hashSeed;
  private static PointR2 origin;
  private static R2Form<PointR2> form;
  public final double x;
  public final double y;

  public PointR2(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public static PointR2 origin() {
    if (origin == null) {
      origin = new PointR2(0.0, 0.0);
    }
    return origin;
  }

  public static PointR2 of(double x, double y) {
    return new PointR2(x, y);
  }

  @Kind
  public static R2Form<PointR2> form() {
    if (form == null) {
      form = new PointR2Form();
    }
    return form;
  }

  public final PointR2 plus(VectorR2 vector) {
    return new PointR2(this.x + vector.x, this.y + vector.y);
  }

  public final PointR2 minux(VectorR2 vector) {
    return new PointR2(this.x - vector.x, this.y - vector.y);
  }

  public final VectorR2 minus(PointR2 that) {
    return new VectorR2(this.x - that.x, this.y - that.y);
  }

  @Override
  public final double xMin() {
    return this.x;
  }

  @Override
  public final double yMin() {
    return this.y;
  }

  @Override
  public final double xMax() {
    return this.x;
  }

  @Override
  public final double yMax() {
    return this.y;
  }

  @Override
  public boolean contains(R2Shape shape) {
    return this.x <= shape.xMin() && shape.xMax() <= this.x
        && this.y <= shape.yMin() && shape.yMax() <= this.y;
  }

  @Override
  public boolean intersects(R2Shape shape) {
    return shape.intersects(this);
  }

  @Override
  public PointZ2 transform(R2ToZ2Function f) {
    return new PointZ2(f.transformX(this.x, this.y), f.transformY(this.x, this.y));
  }

  @Override
  public Value toValue() {
    return form().mold(this).toValue();
  }

  protected boolean canEqual(PointR2 that) {
    return true;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    } else if (other instanceof PointR2) {
      final PointR2 that = (PointR2) other;
      return that.canEqual(this) && this.x == that.x && this.y == that.y;
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (hashSeed == 0) {
      hashSeed = Murmur3.seed(PointR2.class);
    }
    return Murmur3.mash(Murmur3.mix(Murmur3.mix(hashSeed,
        Murmur3.hash(this.x)), Murmur3.hash(this.y)));
  }

  @Override
  public void debug(Output<?> output) {
    output.write("PointR2").write('.').write("of").write('(')
        .debug(this.x).write(", ").debug(this.y).write(')');
  }

  @Override
  public String toString() {
    return Format.debug(this);
  }

}
