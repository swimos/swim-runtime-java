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

import swim.codec.Diagnostic;
import swim.codec.Input;
import swim.codec.Parser;
import swim.collections.FingerTrieSeq;
import swim.http.ContentCoding;
import swim.http.Http;
import swim.http.HttpParser;
import swim.util.Builder;

final class AcceptEncodingParser extends Parser<AcceptEncoding> {

  final HttpParser http;
  final Parser<ContentCoding> coding;
  final Builder<ContentCoding, FingerTrieSeq<ContentCoding>> codings;
  final int step;

  AcceptEncodingParser(HttpParser http, Parser<ContentCoding> coding,
                       Builder<ContentCoding, FingerTrieSeq<ContentCoding>> codings, int step) {
    this.http = http;
    this.coding = coding;
    this.codings = codings;
    this.step = step;
  }

  AcceptEncodingParser(HttpParser http) {
    this(http, null, null, 1);
  }

  @Override
  public Parser<AcceptEncoding> feed(Input input) {
    return AcceptEncodingParser.parse(input, this.http, this.coding, this.codings, this.step);
  }

  static Parser<AcceptEncoding> parse(Input input, HttpParser http, Parser<ContentCoding> coding,
                                      Builder<ContentCoding, FingerTrieSeq<ContentCoding>> codings, int step) {
    int c = 0;
    if (step == 1) {
      if (coding == null) {
        coding = http.parseContentCoding(input);
      } else {
        coding = coding.feed(input);
      }
      if (coding.isDone()) {
        if (codings == null) {
          codings = FingerTrieSeq.builder();
        }
        codings.add(coding.bind());
        coding = null;
        step = 2;
      } else if (coding.isError()) {
        return coding.asError();
      }
    }
    do {
      if (step == 2) {
        while (input.isCont()) {
          c = input.head();
          if (Http.isSpace(c)) {
            input = input.step();
          } else {
            break;
          }
        }
        if (input.isCont() && c == ',') {
          input = input.step();
          step = 3;
        } else if (!input.isEmpty()) {
          return Parser.done(AcceptEncoding.create(codings.bind()));
        }
      }
      if (step == 3) {
        while (input.isCont()) {
          c = input.head();
          if (Http.isSpace(c)) {
            input = input.step();
          } else {
            break;
          }
        }
        if (input.isCont()) {
          step = 4;
        } else if (input.isDone()) {
          return Parser.error(Diagnostic.unexpected(input));
        }
      }
      if (step == 4) {
        if (coding == null) {
          coding = http.parseContentCoding(input);
        } else {
          coding = coding.feed(input);
        }
        if (coding.isDone()) {
          codings.add(coding.bind());
          coding = null;
          step = 2;
          continue;
        } else if (coding.isError()) {
          return coding.asError();
        }
      }
      break;
    } while (true);
    if (input.isError()) {
      return Parser.error(input.trap());
    }
    return new AcceptEncodingParser(http, coding, codings, step);
  }

  static Parser<AcceptEncoding> parse(Input input, HttpParser http) {
    return AcceptEncodingParser.parse(input, http, null, null, 1);
  }

}
