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
import swim.http.Http;
import swim.http.HttpParser;
import swim.http.UpgradeProtocol;
import swim.util.Builder;

final class UpgradeParser extends Parser<Upgrade> {

  final HttpParser http;
  final Parser<UpgradeProtocol> protocol;
  final Builder<UpgradeProtocol, FingerTrieSeq<UpgradeProtocol>> protocols;
  final int step;

  UpgradeParser(HttpParser http, Parser<UpgradeProtocol> protocol,
                Builder<UpgradeProtocol, FingerTrieSeq<UpgradeProtocol>> protocols, int step) {
    this.http = http;
    this.protocol = protocol;
    this.protocols = protocols;
    this.step = step;
  }

  UpgradeParser(HttpParser http) {
    this(http, null, null, 1);
  }

  @Override
  public Parser<Upgrade> feed(Input input) {
    return UpgradeParser.parse(input, this.http, this.protocol, this.protocols, this.step);
  }

  static Parser<Upgrade> parse(Input input, HttpParser http, Parser<UpgradeProtocol> protocol,
                               Builder<UpgradeProtocol, FingerTrieSeq<UpgradeProtocol>> protocols, int step) {
    int c = 0;
    if (step == 1) {
      if (protocol == null) {
        protocol = http.parseUpgradeProtocol(input);
      } else {
        protocol = protocol.feed(input);
      }
      if (protocol.isDone()) {
        if (protocols == null) {
          protocols = FingerTrieSeq.builder();
        }
        protocols.add(protocol.bind());
        protocol = null;
        step = 2;
      } else if (protocol.isError()) {
        return protocol.asError();
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
          return Parser.done(Upgrade.create(protocols.bind()));
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
        if (protocol == null) {
          protocol = http.parseUpgradeProtocol(input);
        } else {
          protocol = protocol.feed(input);
        }
        if (protocol.isDone()) {
          protocols.add(protocol.bind());
          protocol = null;
          step = 2;
          continue;
        } else if (protocol.isError()) {
          return protocol.asError();
        }
      }
      break;
    } while (true);
    if (input.isError()) {
      return Parser.error(input.trap());
    }
    return new UpgradeParser(http, protocol, protocols, step);
  }

  static Parser<Upgrade> parse(Input input, HttpParser http) {
    return UpgradeParser.parse(input, http, null, null, 1);
  }

}
