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

package swim.http.header;

import swim.codec.Input;
import swim.codec.Output;
import swim.codec.Parser;
import swim.codec.Writer;
import swim.collections.FingerTrieSeq;
import swim.http.ContentCoding;
import swim.http.HttpHeader;
import swim.http.HttpParser;
import swim.http.HttpWriter;
import swim.util.Builder;
import swim.util.Murmur3;

public final class AcceptEncoding extends HttpHeader {

  private static int hashSeed;
  final FingerTrieSeq<ContentCoding> codings;

  AcceptEncoding(FingerTrieSeq<ContentCoding> codings) {
    this.codings = codings;
  }

  public static AcceptEncoding from(FingerTrieSeq<ContentCoding> codings) {
    return new AcceptEncoding(codings);
  }

  public static AcceptEncoding from(ContentCoding... codings) {
    return new AcceptEncoding(FingerTrieSeq.of(codings));
  }

  public static AcceptEncoding from(String... codingStrings) {
    final Builder<ContentCoding, FingerTrieSeq<ContentCoding>> codings = FingerTrieSeq.builder();
    for (int i = 0, n = codingStrings.length; i < n; i += 1) {
      codings.add(ContentCoding.parse(codingStrings[i]));
    }
    return new AcceptEncoding(codings.bind());
  }

  public static Parser<AcceptEncoding> parseHttpValue(Input input, HttpParser http) {
    return AcceptEncodingParser.parse(input, http);
  }

  @Override
  public boolean isBlank() {
    return this.codings.isEmpty();
  }

  @Override
  public String lowerCaseName() {
    return "accept-encoding";
  }

  @Override
  public String name() {
    return "Accept-Encoding";
  }

  public FingerTrieSeq<ContentCoding> codings() {
    return this.codings;
  }

  @Override
  public Writer<?, ?> writeHttpValue(Output<?> output, HttpWriter http) {
    return http.writeParamList(this.codings.iterator(), output);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    } else if (other instanceof AcceptEncoding) {
      final AcceptEncoding that = (AcceptEncoding) other;
      return this.codings.equals(that.codings);
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (hashSeed == 0) {
      hashSeed = Murmur3.seed(AcceptEncoding.class);
    }
    return Murmur3.mash(Murmur3.mix(hashSeed, this.codings.hashCode()));
  }

  @Override
  public void debug(Output<?> output) {
    output = output.write("AcceptEncoding").write('.').write("from").write('(');
    final int n = this.codings.size();
    if (n > 0) {
      output.debug(this.codings.head());
      for (int i = 1; i < n; i += 1) {
        output = output.write(", ").debug(this.codings.get(i));
      }
    }
    output = output.write(')');
  }

}
