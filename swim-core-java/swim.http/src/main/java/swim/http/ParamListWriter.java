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

package swim.http;

import java.util.Iterator;
import swim.codec.Output;
import swim.codec.Writer;
import swim.codec.WriterException;

final class ParamListWriter extends Writer<Object, Object> {

  final HttpWriter http;
  final Iterator<? extends HttpPart> params;
  final Writer<?, ?> param;
  final int step;

  ParamListWriter(HttpWriter http, Iterator<? extends HttpPart> params,
                  Writer<?, ?> param, int step) {
    this.http = http;
    this.params = params;
    this.param = param;
    this.step = step;
  }

  ParamListWriter(HttpWriter http, Iterator<? extends HttpPart> params) {
    this(http, params, null, 1);
  }

  @Override
  public Writer<Object, Object> pull(Output<?> output) {
    return ParamListWriter.write(output, this.http, this.params, this.param, this.step);
  }

  static Writer<Object, Object> write(Output<?> output, HttpWriter http,
                                      Iterator<? extends HttpPart> params,
                                      Writer<?, ?> param, int step) {
    do {
      if (step == 1) {
        if (param == null) {
          if (!params.hasNext()) {
            return Writer.done();
          } else {
            param = params.next().writeHttp(output, http);
          }
        } else {
          param = param.pull(output);
        }
        if (param.isDone()) {
          param = null;
          if (!params.hasNext()) {
            return Writer.done();
          } else {
            step = 2;
          }
        } else if (param.isError()) {
          return param.asError();
        }
      }
      if (step == 2 && output.isCont()) {
        output = output.write(',');
        step = 3;
      }
      if (step == 3 && output.isCont()) {
        output = output.write(' ');
        step = 1;
        continue;
      }
      break;
    } while (true);
    if (output.isDone()) {
      return Writer.error(new WriterException("truncated"));
    } else if (output.isError()) {
      return Writer.error(output.trap());
    }
    return new ParamListWriter(http, params, param, step);
  }

  static Writer<Object, Object> write(Output<?> output, HttpWriter http,
                                      Iterator<? extends HttpPart> params) {
    return ParamListWriter.write(output, http, params, null, 1);
  }

}
