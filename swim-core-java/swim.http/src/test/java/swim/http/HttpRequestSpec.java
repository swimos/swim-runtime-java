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

import org.testng.annotations.Test;
import swim.http.header.Connection;
import swim.http.header.Host;
import swim.http.header.Origin;
import swim.http.header.RawHeader;
import swim.http.header.SecWebSocketKey;
import swim.http.header.SecWebSocketProtocol;
import swim.http.header.SecWebSocketVersion;
import swim.http.header.Upgrade;
import swim.uri.Uri;
import static swim.http.HttpAssertions.assertWrites;

public class HttpRequestSpec {

  @Test
  public void parseRequestsWithNoHeaders() {
    assertParses("GET / HTTP/1.0\r\n"
               + "\r\n",
                 HttpRequest.create(HttpMethod.GET, Uri.parse("/"), HttpVersion.HTTP_1_0));
  }

  @Test
  public void parseRequestsWithASingleHeader() {
    assertParses("HEAD /path HTTP/1.1\r\n"
               + "Key: Value\r\n"
               + "\r\n",
                 HttpRequest.head(Uri.parse("/path"), RawHeader.create("Key", "Value")));
    assertParses("HEAD /path HTTP/1.1\r\n"
               + "Host: example.com\r\n"
               + "\r\n",
                 HttpRequest.head(Uri.parse("/path"), Host.create("example.com")));
  }

  @Test
  public void parseRequestsWithMultipleHeaders() {
    assertParses("GET /chat HTTP/1.1\r\n"
               + "Host: example.com\r\n"
               + "Upgrade: websocket\r\n"
               + "Connection: Upgrade\r\n"
               + "Sec-WebSocket-Key: dGhlIHNhbXBsZSBub25jZQ==\r\n"
               + "Origin: http://example.com\r\n"
               + "Sec-WebSocket-Protocol: chat, superchat\r\n"
               + "Sec-WebSocket-Version: 13\r\n"
               + "\r\n",
                 HttpRequest.get(Uri.parse("/chat"),
                                 Host.create("example.com"),
                                 Upgrade.create("websocket"),
                                 Connection.create("Upgrade"),
                                 SecWebSocketKey.create("dGhlIHNhbXBsZSBub25jZQ=="),
                                 Origin.create("http://example.com"),
                                 SecWebSocketProtocol.create("chat", "superchat"),
                                 SecWebSocketVersion.create(13)));
  }

  @Test
  public void writeRequestsWithNoHeaders() {
    assertWrites(HttpRequest.create(HttpMethod.GET, Uri.parse("/"), HttpVersion.HTTP_1_0),
                 "GET / HTTP/1.0\r\n"
               + "\r\n");
  }

  @Test
  public void writeRequestsWithASingleHeader() {
    assertWrites(HttpRequest.head(Uri.parse("/path"), RawHeader.create("Key", "Value")),
                 "HEAD /path HTTP/1.1\r\n"
               + "Key: Value\r\n"
               + "\r\n");
    assertWrites(HttpRequest.head(Uri.parse("/path"), Host.create("example.com")),
                 "HEAD /path HTTP/1.1\r\n"
               + "Host: example.com\r\n"
               + "\r\n");
  }

  @Test
  public void writeRequestsWithMultipleHeaders() {
    assertWrites(HttpRequest.get(Uri.parse("/chat"),
                                 Host.create("example.com"),
                                 Upgrade.create("websocket"),
                                 Connection.create("Upgrade"),
                                 SecWebSocketKey.create("dGhlIHNhbXBsZSBub25jZQ=="),
                                 Origin.create("http://example.com"),
                                 SecWebSocketProtocol.create("chat", "superchat"),
                                 SecWebSocketVersion.create(13)),
                 "GET /chat HTTP/1.1\r\n"
               + "Host: example.com\r\n"
               + "Upgrade: websocket\r\n"
               + "Connection: Upgrade\r\n"
               + "Sec-WebSocket-Key: dGhlIHNhbXBsZSBub25jZQ==\r\n"
               + "Origin: http://example.com\r\n"
               + "Sec-WebSocket-Protocol: chat, superchat\r\n"
               + "Sec-WebSocket-Version: 13\r\n"
               + "\r\n");
  }

  public static <T> void assertParses(String string, HttpRequest<T> request) {
    HttpAssertions.assertParses(Http.standardParser().<T>requestParser(), string, request);
  }

}
