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
import swim.codec.Parser;
import swim.collections.FingerTrieSeq;
import swim.http.Http;
import swim.http.HttpParser;
import swim.http.Product;
import swim.util.Builder;

final class ServerParser extends Parser<Server> {

  final HttpParser http;
  final Parser<Product> product;
  final Builder<Product, FingerTrieSeq<Product>> products;
  final int step;

  ServerParser(HttpParser http, Parser<Product> product,
               Builder<Product, FingerTrieSeq<Product>> products, int step) {
    this.http = http;
    this.product = product;
    this.products = products;
    this.step = step;
  }

  ServerParser(HttpParser http) {
    this(http, null, null, 1);
  }

  @Override
  public Parser<Server> feed(Input input) {
    return ServerParser.parse(input, this.http, this.product, this.products, this.step);
  }

  static Parser<Server> parse(Input input, HttpParser http, Parser<Product> product,
                              Builder<Product, FingerTrieSeq<Product>> products, int step) {
    int c = 0;
    if (step == 1) {
      if (product == null) {
        product = http.parseProduct(input);
      } else {
        product = product.feed(input);
      }
      if (product.isDone()) {
        if (products == null) {
          products = FingerTrieSeq.builder();
        }
        products.add(product.bind());
        product = null;
        step = 2;
      } else if (product.isError()) {
        return product.asError();
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
        if (input.isCont() && Http.isTokenChar(c)) {
          step = 3;
        } else if (!input.isEmpty()) {
          return Parser.done(Server.create(products.bind()));
        }
      }
      if (step == 3) {
        if (product == null) {
          product = http.parseProduct(input);
        } else {
          product = product.feed(input);
        }
        if (product.isDone()) {
          products.add(product.bind());
          product = null;
          step = 2;
          continue;
        } else if (product.isError()) {
          return product.asError();
        }
      }
      break;
    } while (true);
    if (input.isError()) {
      return Parser.error(input.trap());
    }
    return new ServerParser(http, product, products, step);
  }

  static Parser<Server> parse(Input input, HttpParser http) {
    return ServerParser.parse(input, http, null, null, 1);
  }

}
