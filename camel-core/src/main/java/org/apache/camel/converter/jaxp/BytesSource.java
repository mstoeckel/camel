/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.converter.jaxp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import javax.xml.transform.stream.StreamSource;

import org.apache.camel.util.ObjectHelper;

/**
 * A helper class which provides a JAXP {@link javax.xml.transform.Source Source} from a byte[]
 * which can be read as many times as required.
 *
 * @version 
 */
public class BytesSource extends StreamSource implements Serializable {
    private static final long serialVersionUID = 124123201106542082L;

    private final byte[] data;

    public BytesSource(byte[] data) {
        ObjectHelper.notNull(data, "data");
        this.data = data;
    }

    public BytesSource(byte[] data, String systemId) {
        ObjectHelper.notNull(data, "data");
        this.data = data;
        setSystemId(systemId);
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(data);
    }

    public Reader getReader() {
        return new InputStreamReader(getInputStream());
    }

    public byte[] getData() {
        return data;
    }

    public String toString() {
        return "BytesSource[" + new String(data) + "]";
    }
}