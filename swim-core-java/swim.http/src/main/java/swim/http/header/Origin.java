// Copyright 2015-2021 Swim Inc.
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

package swim.http.header;

import swim.codec.Input;
import swim.codec.Output;
import swim.codec.Parser;
import swim.codec.Unicode;
import swim.codec.Writer;
import swim.collections.FingerTrieSeq;
import swim.http.HttpHeader;
import swim.http.HttpParser;
import swim.http.HttpWriter;
import swim.uri.Uri;
import swim.util.Builder;
import swim.util.Murmur3;

public final class Origin extends HttpHeader {

  final FingerTrieSeq<Uri> origins;

  Origin(FingerTrieSeq<Uri> origins) {
    this.origins = origins;
  }

  @Override
  public String lowerCaseName() {
    return "origin";
  }

  @Override
  public String name() {
    return "Origin";
  }

  public FingerTrieSeq<Uri> origins() {
    return this.origins;
  }

  @Override
  public Writer<?, ?> writeHttpValue(Output<?> output, HttpWriter http) {
    if (this.origins.isEmpty()) {
      return Unicode.writeString("null", output);
    } else {
      return OriginWriter.write(output, this.origins.iterator());
    }
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    } else if (other instanceof Origin) {
      final Origin that = (Origin) other;
      return this.origins.equals(that.origins);
    }
    return false;
  }

  private static int hashSeed;

  @Override
  public int hashCode() {
    if (Origin.hashSeed == 0) {
      Origin.hashSeed = Murmur3.seed(Origin.class);
    }
    return Murmur3.mash(Murmur3.mix(Origin.hashSeed, this.origins.hashCode()));
  }

  @Override
  public <T> Output<T> debug(Output<T> output) {
    output = output.write("Origin").write('.');
    final int n = this.origins.size();
    if (n > 0) {
      output = output.write("create").write('(').debug(this.origins.head().toString());
      for (int i = 1; i < n; i += 1) {
        output = output.write(", ").write(this.origins.get(i).toString());
      }
      output = output.write(')');
    } else {
      output = output.write("empty").write('(').write(')');
    }
    return output;
  }

  private static Origin empty;

  public static Origin empty() {
    if (Origin.empty == null) {
      Origin.empty = new Origin(FingerTrieSeq.<Uri>empty());
    }
    return Origin.empty;
  }

  public static Origin create(FingerTrieSeq<Uri> origins) {
    if (origins.isEmpty()) {
      return Origin.empty();
    } else {
      return new Origin(origins);
    }
  }

  public static Origin create(Uri... origins) {
    if (origins.length == 0) {
      return Origin.empty();
    } else {
      return new Origin(FingerTrieSeq.of(origins));
    }
  }

  public static Origin create(String... originStrings) {
    if (originStrings.length == 0) {
      return Origin.empty();
    } else {
      final Builder<Uri, FingerTrieSeq<Uri>> origins = FingerTrieSeq.builder();
      for (int i = 0, n = originStrings.length; i < n; i += 1) {
        origins.add(Uri.parse(originStrings[i]));
      }
      return new Origin(origins.bind());
    }
  }

  public static Parser<Origin> parseHttpValue(Input input, HttpParser http) {
    return OriginParser.parse(input);
  }

}
