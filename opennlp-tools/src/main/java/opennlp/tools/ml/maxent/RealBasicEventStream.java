/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package opennlp.tools.ml.maxent;

import java.io.IOException;

import opennlp.tools.ml.model.Event;
import opennlp.tools.ml.model.RealValueFileEventStream;
import opennlp.tools.util.ObjectStream;

/**
 * Class for real-valued {@link Event events} as an
 * {@link ObjectStream event stream}.
 * .
 * @see Event
 * @see ObjectStream
 */
public class RealBasicEventStream implements ObjectStream<Event> {

  private final ObjectStream<String> ds;

  public RealBasicEventStream(ObjectStream<String> ds) {
    this.ds = ds;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IOException Thrown if there is an error during reading.
   * @throws RuntimeException Thrown if negative real values are detected in the input data.
   */
  @Override
  public Event read() throws IOException {

    String eventString = ds.read();
    if (eventString != null) {
      return createEvent(eventString);
    }
    return null;
  }

  private Event createEvent(String obs) {
    int si = obs.indexOf(' ');
    if (si == -1)
      return null;
    else {
      String outcome = obs.substring(0, si);
      String[] contexts = obs.substring(si + 1).split("\\s+");
      float[] values = RealValueFileEventStream.parseContexts(contexts);
      return new Event(outcome,contexts,values);
    }
  }

  @Override
  public void reset() throws IOException, UnsupportedOperationException {
    ds.reset();
  }

  @Override
  public void close() throws IOException {
    ds.close();
  }

}
